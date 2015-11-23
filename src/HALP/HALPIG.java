package HALP;


import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
    private InetAddress ingoingIN;
    private InetAddress outgoingIN;
    
    private static final int IG_PORT = 54001;
    
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
        msgSize = HEDR_LEN + maxDataRate;
        System.out.println("Internet gateway has started.");
        while(true)
        {
            try {
                byte[] rcvdMsg = receiveMessage();
                boolean isSyn = isSYNFlagSet(rcvdMsg);
                boolean isAck = isACKFlagSet(rcvdMsg);
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
                sendMessage(rcvdMsg);
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
        String sentMessage = new String(msgBytes, 0, sendPacket.getLength());
        System.out.println("Message sent is: [" + sentMessage + "]");
        printMessage(msgBytes);
    }
    
    @Override
    public boolean errorGenerator()
    {
        Random random = new Random();
        int chanceMax = 100;
        int randomChance = random.nextInt(chanceMax) + 1;
        if(randomChance <= errorRate)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
     
    @Override
    public boolean errorType(int n){
        Random randomQ = new Random();
        int q = randomQ.nextInt(100)/100;
        if(q > n){
            return true;  //if true then a random bit is flipped and messaged sent 
        }
        else{
            return false;   //if false we do nothing with the packet, simulates a lost packet
        }
    }
    
    @Override
    public int randomIndex()
    {
        Random random = new Random();
        int indexMax = 18;
        int randomIndex = random.nextInt(indexMax);
        return randomIndex;
    }
    
    @Override
    public byte generateByteError(byte oldByte)
    {
        Random random = new Random();
        int bitMax = 7;
        int randomBit = random.nextInt(bitMax);
        byte errorByte = oldByte;
        errorByte ^= 1 << randomBit; 
//        System.out.println(errorByte);
        return errorByte;
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
    public void setErrorRate(int rate)
    {
        errorRate = rate;
    }
    
    @Override
    public int getErrorRate()
    {
        return errorRate;
    }
    
    @Override
    public int getExpectedRetransmissions() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
            
    public static void main(String args[]) throws Exception
    {
        Scanner console = new Scanner(System.in);
        HALPIG halpIG = new HALPIG(IG_PORT);
        halpIG.setMaxDataRate(65000);
        halpIG.run();
    }
}