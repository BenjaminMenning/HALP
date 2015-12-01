package HALP;


import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HALPIG extends HALP implements HALPIGInterface
{
    private String ingoingIP;
    private String outgoingIP;
    private int ingoingPN;
    private int outgoingPN;
    private int maxDataRate;
    protected double errorRate = 0; // p
    protected double corruptRate = 0; // q
    protected double lossRate = 0; // q - 1
    private InetAddress ingoingIN;
    private InetAddress outgoingIN;
    
    private static final int IG_PORT = 43000;
    
     private PrintWriter IGLog = null;
  
    
    public static void main(String args[]) throws Exception
    {
        Scanner console = new Scanner(System.in);
        HALPIG halpIG = new HALPIG(IG_PORT);
        
        double errRate = 0.1;
        double crptRate = 1.0;
        halpIG.setMaxDataRate(10000);
        halpIG.setErrorRate(errRate);
        halpIG.setCorruptRate(crptRate);
        halpIG.run();
    }
    
    public HALPIG() throws SocketException
    {
        deviceSocket = new DatagramSocket();
    }
    
    public HALPIG(int igPN) throws SocketException
    {
        igPortNum = igPN;
        deviceSocket = new DatagramSocket(igPN);
    }

    @Override
    public void run() 
    {
       //creating log for internet gateway
        String IGStr = System.getProperty("user.home") + "/Desktop/";
        String IGName = "IG_Log.txt";
        File IGFile = new File(IGStr + IGName);
        try {
            IGLog = new PrintWriter(new FileWriter(IGFile, true)); //new PrintWriter(IGFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HALPIG.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HALPIG.class.getName()).log(Level.SEVERE, null, ex);
        }
        IGLog.write("Internet Gateways' Logging: \n");
        IGLog.close();
        
        
        
        msgSize = HEDR_LEN + maxDataRate;
        boolean isError;
        boolean isCorrupt;
        boolean isLost;
        System.out.println("Internet gateway has started.");
        while(true)
        {
            try {
                byte[] rcvdMsg = receiveMessage();
                boolean isSyn = isSYNFlagSet(rcvdMsg);
                boolean isAck = isACKFlagSet(rcvdMsg);
                
                // Error variables
                isError = false;
                isCorrupt = false;
                isLost = false;
                
                if(isSyn && !isAck) 
                {
                    // Retrieves the ingoing connection info from the datagram
                    ingoingIP = currDtgm.getAddress().getHostAddress();
                    ingoingPN = currDtgm.getPort();
                    ingoingIN = InetAddress.getByName(ingoingIP);
                    
                    // Assigns ingoing info to the client
                    clntIPAddr = ingoingIP;
                    clntPortNum = ingoingPN;
                    clntINAddr = ingoingIN;
                    
                    // Retrieves the outgoing connection info from the datagram
                    outgoingIP = getDestinationIP(rcvdMsg);
                    outgoingPN = getDestinationPort(rcvdMsg);
                    outgoingIN = InetAddress.getByName(outgoingIP);

                    // Assigns the outgoing info to the client
                    servIPAddr = outgoingIP;
                    servPortNum = outgoingPN;
                    servINAddr = outgoingIN;
                    
                    // Retrieves client data rate and changes if above max
                    int clntDataRate = getDataRateField(rcvdMsg);
                    if(clntDataRate > maxDataRate)
                    {
                        rcvdMsg = setDataRateField(rcvdMsg, maxDataRate);
                    }
                }
                else if(isSyn && isAck)
                {
                    outgoingIN = clntINAddr;
                    outgoingPN = clntPortNum;
//                    ingoingIP = currDtgm.getAddress().getHostAddress();
//                    ingoingPN = currDtgm.getPort();
//                    outgoingIP = getDestinationIP(rcvdMsg);
//                    outgoingPN = getDestinationPort(rcvdMsg);
//                    outgoingIN = InetAddress.getByName(outgoingIP);
                }
                else if(currDtgm.getAddress().equals(servINAddr))
                {
                    outgoingIN = clntINAddr;
                    outgoingPN = clntPortNum;
                }
                else if(currDtgm.getAddress().equals(clntINAddr))
                {
                    outgoingIN = servINAddr;
                    outgoingPN = servPortNum;
                }
                
//                else if(isSYNFlagSet(rcvdMsg))
                
                isError = errorGenerator();
                System.out.println("Is error: " + isError);
                if(isError)
                {
                    IGLog.append(errorGeneratedLog(rcvdMsg) + "\n");
                    IGLog.close();
                    
                    isCorrupt = isCorrupt();
                    System.out.println("Is corrupt: " + isCorrupt);
                    if(isCorrupt)
                    {
                        rcvdMsg = generateByteError(rcvdMsg);
                    }
                    else
                    {
                        isLost = true;
                    }
                }
                
                if(!isLost)
                {
                    sendMessage(rcvdMsg);
                }
                else
                {
                    // do nothing
                }
            } 
            catch (Exception ex)
            {
                Logger.getLogger(HALPIG.class.getName()).log(Level.SEVERE, null,
                        ex);
            }
        }
    }
    
    @Override
    public void inputDataRate()
    {
        // Requests user to input transfer direction
        System.out.println("Please enter the preferred data rate for the "
                + "connection: ");
        int tempDataRate = console.nextInt();
        setMaxDataRate(tempDataRate);
    }
    
    @Override
    public void setMaxDataRate(int dataRate)
    {
        maxDataRate = dataRate;
    }
    
    @Override
    public void sendMessage(byte[] messageBytes) throws Exception 
    {
        byte[] msgBytes = messageBytes;
        int msgLen = msgBytes.length;
        DatagramPacket sendPacket = 
                new DatagramPacket(msgBytes, msgLen, outgoingIN, outgoingPN);

        // Send a message
        deviceSocket.send(sendPacket);
        
        // Display the message
//        String sentMessage = new String(msgBytes, 0, sendPacket.getLength());
        System.out.println("Message sent is: ");
        printMessage(msgBytes);
    }
    
    @Override
    public boolean errorGenerator()
    {
        Random random = new Random();
        int chanceMax = 100;
        int randomChance = random.nextInt(chanceMax) + 1;
        if(randomChance <= (errorRate * 100))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
     
    @Override
    public boolean isCorrupt()
    {
        Random random = new Random();
        int chanceMax = 100;
        int randomChance = random.nextInt(chanceMax) + 1;
        if(randomChance <= (corruptRate * 100))
        {
            return true;  //if true then a random bit is flipped and messaged sent 
        }
        else
        {
            return false;   //if false we do nothing with the packet, simulates a lost packet
        }
    }
    
    @Override
    public int randomIndex(int index)
    {
        Random random = new Random();
        int indexMax = index;
        int randomIndex = random.nextInt(indexMax);
        return randomIndex;
    }
    
    @Override
    public byte[] generateByteError(byte[] messageBytes)
    {
        byte[] tempMsgBytes = messageBytes;
        int msgLen = Array.getLength(messageBytes);
        int maxIndex = msgLen - 1;
        int randomIndex = randomIndex(maxIndex);
        byte errorByte = tempMsgBytes[randomIndex];
        Random random = new Random();
        int bitMax = 7;
        int randomBit = random.nextInt(bitMax);
        errorByte ^= 1 << randomBit; 
        System.out.println(errorByte);
        tempMsgBytes[randomIndex] = errorByte;
        return tempMsgBytes;
    }
    
    @Override
    public int errorNumber()
    {
        int errorNum;
        Random random = new Random();
        int chanceMax = 100;
        int randomChance = random.nextInt(chanceMax) + 1;
        if(randomChance <= 70)
        {
            errorNum = 1;
            return errorNum;
        }
        else
        {
            errorNum = 2;
            return errorNum;
        }
    }
    
    @Override
    public void setErrorRate(double rate)
    {
        errorRate = rate;
    }
    
    @Override
    public double getErrorRate()
    {
        return errorRate;
    }
    
    @Override
    public void setCorruptRate(double rate)
    {
        corruptRate = rate;
        lossRate = corruptRate - 1;
    }
    
    @Override
    public double getCorruptRate()
    {
        return corruptRate;
    }
    
    @Override
    public int getExpectedRetransmissions() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}