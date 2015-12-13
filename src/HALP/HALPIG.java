package HALP;

/** 
 * Author:          Benjamin Menning, John Blacketer
 * 
 * Date:            12/15/2015 
 *                
 * Course:          CS 413 Advanced Networking
 * 
 * Assignment:      Final Project - HALP Protocol
 * 
 * Description:     This program is a program that performs a simple file 
 *                  transfer utilizing our own protocol, HALP. It includes 
 *                  three devices: a client, an internet gateway (IG), and a
 *                  server. The client initiates a file download or upload from
 *                  or to the server, and the file transfer process begins, 
 *                  while the internet gateway passes messages between them. 
 *                  It follows our protocol to provide reliability for the data
 *                  transfer process. All three devices follow the protocol to
 *                  manipulate the header data fields and can print out 
 *                  information to trace and log the connection process. Our 
 *                  program / protocol utilizes use of positive acknowledgment,
 *                  retransmission, timeout, and sequence numbers to provide 
 *                  reliability. 
 * 
 */

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

/** 
 * This class implements methods and functionality laid out by the 
 * HALPIG interface. It defines methods for generating errors and setting the 
 * max data rate.
 * 
 * @author Benjamin Menning, John Blacketer
 * @version 12/15/2015
*/
public class HALPIG extends HALP implements HALPIGInterface
{
    private String ingoingIP;
    private String outgoingIP;
    private int ingoingPN;
    private int outgoingPN;
    private int maxDataRate;
    private InetAddress ingoingIN;
    private InetAddress outgoingIN;
    private int outgoingRate = 0;
    
    protected ConnectionTable connTable = new ConnectionTable();
    
    private static final int IG_PORT = 43001;
    
//     private PrintWriter IGLog = null;
  
    
    public static void main(String args[]) throws Exception
    {
        Scanner console = new Scanner(System.in);
        HALPIG halpIG = new HALPIG(IG_PORT);
        
        // Hard coded values for testing
        double testErrRate0 = 0.0;
        double testErrRate1 = 1.0;
        double testErrRate2 = 0.1;
        double testErrRate3 = 0.3;
        double testErrRate4 = 0.5;
        double testErrRate5 = 0.7;
        double testErrRate6 = 0.9;
        
        double testCrptRate0 = 0.0;
        double testCrptRate1 = 1.0;
        double testCrptRate2 = 0.1;
        double testCrptRate3 = 0.3;
        double testCrptRate4 = 0.5;
        double testCrptRate5 = 0.7;
        double testCrptRate6 = 0.9;
        
        double errRate = 0.0;
        double crptRate = 0.0;
        
        // Hard coded input for testing
        halpIG.setMaxDataRate(10000);
        halpIG.setErrorRate(errRate);
        halpIG.setCorruptRate(crptRate);
        halpIG.setTrace(false);
        
        // For user input
//        halpIG.inputMaxDataRate();
//        halpIG.inputErrorRate();
//        halpIG.inputCorruptRate();
//        halpIG.inputTrace();
        
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
        if(isTraceOn)
        {
            //creating log for IG
            try 
            {
                String desktopStr = System.getProperty("user.home") + "/Desktop/";
                String deviceStr = "IG's Log.txt";
                File deviceFile = new File(desktopStr + deviceStr);
                deviceLog = new PrintWriter(deviceFile);
                deviceLog.println("IG's Log: \n");
            } 
            catch (FileNotFoundException ex) 
            {
                Logger.getLogger(HALPIG.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
//       //creating log for internet gateway
//        String IGStr = System.getProperty("user.home") + "/Desktop/";
//        String IGName = "IG_Log.txt";
//        File IGFile = new File(IGStr + IGName);
//        try {
//            IGLog = new PrintWriter(new FileWriter(IGFile, true)); //new PrintWriter(IGFile);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(HALPIG.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(HALPIG.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        IGLog.write("Internet Gateways' Logging: \n");
//        IGLog.close();
        
        
        
        msgSize = HEDR_LEN + maxDataRate;
        boolean isError;
        boolean isCorrupt;
        boolean isLost;
        boolean isInTable;
        
        int tempMaxDataRate = maxDataRate;
        System.out.println("The internet gateway has started.");
        if(isTraceOn)
        {
            deviceLog.println("The internet gateway has started.");
        }
        while(true)
        {
            try {
//                maxDataRate = tempMaxDataRate;
                byte[] rcvdMsg = receiveMessage();
                boolean isSyn = isSYNFlagSet(rcvdMsg);
                boolean isAck = isACKFlagSet(rcvdMsg);
                
                // Error variables
                isError = false;
                isCorrupt = false;
                isLost = false;
                isInTable = false;
                
                Connection tempConn = null;
                Connection outgoingConn = null;
                InetAddress tempINAddr = null;
                String tempIPAddr = null;
                int tempPortNum = 0;
                
                tempIPAddr = currDtgm.getAddress().getHostAddress();
                tempPortNum = currDtgm.getPort();
                tempINAddr = InetAddress.getByName(tempIPAddr);
                
                isInTable = connTable.searchTable(tempIPAddr, tempPortNum);
                
                if(isSyn && !isAck && !isInTable) 
                {
//                    // Retrieves the ingoing connection info from the datagram
//                    ingoingIP = tempIPAddr;
//                    ingoingPN = tempPortNum;
//                    ingoingIN = tempINAddr;
//                    
//                    // Retrieves the outgoing connection info from the datagram
//                    outgoingIP = getDestinationIP(rcvdMsg);
//                    outgoingPN = getDestinationPort(rcvdMsg);
//                    outgoingIN = InetAddress.getByName(outgoingIP);
                    
                    // Assigns ingoing info to the client
                    clntIPAddr = tempIPAddr;
                    clntPortNum = tempPortNum;
                    clntINAddr = tempINAddr;

                    // Assigns the outgoing info to the client
                    servIPAddr = getDestinationIP(rcvdMsg);
                    servPortNum = getDestinationPort(rcvdMsg);
                    servINAddr = InetAddress.getByName(servIPAddr);
                                        
//                    isInTable = connTable.searchTable(clntIPAddr, clntPortNum);
                    
//                    if(!isInTable)
//                    {
                        // Retrieves client data rate and changes if above max
                        int clntDataRate = getDataRateField(rcvdMsg);
                        if(clntDataRate > maxDataRate)
                        {
                            clntDataRate = maxDataRate;
                            rcvdMsg = setDataRateField(rcvdMsg, maxDataRate);
                        
                            // Reset checksum
                            byte emptyBytes[] = new byte[2];
                            System.arraycopy(emptyBytes, 0, rcvdMsg, CRC_OFFSET, 
                                    CRC_LEN);   
                            rcvdMsg = setChecksum(rcvdMsg);
                        }
                        
                        tempConn = new Connection(clntIPAddr, clntPortNum, 
                                servIPAddr, servPortNum, clntDataRate);
                        connTable.addConnection(tempConn);
//                    }
                }
                else if(isSyn && !isAck && isInTable)
                {
                    // Retrieves client data rate and changes if above max
                    int clntDataRate = getDataRateField(rcvdMsg);
                    if(clntDataRate > maxDataRate)
                    {
                        clntDataRate = maxDataRate;
                        rcvdMsg = setDataRateField(rcvdMsg, maxDataRate);
                       
                        // Reset checksum
                        byte emptyBytes[] = new byte[2];
                        System.arraycopy(emptyBytes, 0, rcvdMsg, CRC_OFFSET, 
                                CRC_LEN);   
                        rcvdMsg = setChecksum(rcvdMsg);
                    }

                    tempConn = new Connection(clntIPAddr, clntPortNum, 
                            servIPAddr, servPortNum, clntDataRate);
                    connTable.addConnection(tempConn);
                }
                
                outgoingConn = connTable.getCorrespondingConnection(tempIPAddr, 
                        tempPortNum);
                outgoingRate = outgoingConn.getTempRate();
                outgoingPN = outgoingConn.getTempPort();
                outgoingIP = outgoingConn.getTempIP();
                outgoingIN = InetAddress.getByName(outgoingIP);
//                maxDataRate = outgoingRate;
                
                                
                isError = errorGenerator();
                if(isTraceOn)
                {
                    System.out.println("Error generated: " + isError);
                    deviceLog.println("Error generated: " + isError);
                }
                if(isError)
                {
//                    IGLog.append(errorGeneratedLog(rcvdMsg) + "\n");
//                    IGLog.close();
                    
                    isCorrupt = isCorrupt();
                    if(isTraceOn)
                    {
                        System.out.println("Corrupted message: " + isCorrupt);
                        deviceLog.println("Corrupted message: " + isCorrupt);
                    }
                    if(isCorrupt)
                    {
                        rcvdMsg = generateByteError(rcvdMsg);
                    }
                    else
                    {
                        isLost = true;
                        if(isTraceOn)
                        {
                            System.out.println("Lost message: " + isLost);
                            deviceLog.println("Lost message: " + isLost);
                        }
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
            
            // Old connection table code
//                if(isSyn && !isAck) 
//                {
//                    // Retrieves the ingoing connection info from the datagram
//                    ingoingIP = currDtgm.getAddress().getHostAddress();
//                    ingoingPN = currDtgm.getPort();
//                    ingoingIN = InetAddress.getByName(ingoingIP);
//                    
//                    // Assigns ingoing info to the client
//                    clntIPAddr = ingoingIP;
//                    clntPortNum = ingoingPN;
//                    clntINAddr = ingoingIN;
//                    
//                    // Retrieves the outgoing connection info from the datagram
//                    outgoingIP = getDestinationIP(rcvdMsg);
//                    outgoingPN = getDestinationPort(rcvdMsg);
//                    outgoingIN = InetAddress.getByName(outgoingIP);
//
//                    // Assigns the outgoing info to the client
//                    servIPAddr = outgoingIP;
//                    servPortNum = outgoingPN;
//                    servINAddr = outgoingIN;
//                    
//                    // Retrieves client data rate and changes if above max
//                    int clntDataRate = getDataRateField(rcvdMsg);
//                    if(clntDataRate > maxDataRate)
//                    {
//                        rcvdMsg = setDataRateField(rcvdMsg, maxDataRate);
//                    }
//                }
//                else if(isSyn && isAck)
//                {
//                    outgoingIN = clntINAddr;
//                    outgoingPN = clntPortNum;
////                    ingoingIP = currDtgm.getAddress().getHostAddress();
////                    ingoingPN = currDtgm.getPort();
////                    outgoingIP = getDestinationIP(rcvdMsg);
////                    outgoingPN = getDestinationPort(rcvdMsg);
////                    outgoingIN = InetAddress.getByName(outgoingIP);
//                }
//                else if(currDtgm.getAddress().equals(servINAddr))
//                {
//                    outgoingIN = clntINAddr;
//                    outgoingPN = clntPortNum;
//                }
//                else if(currDtgm.getAddress().equals(clntINAddr))
//                {
//                    outgoingIN = servINAddr;
//                    outgoingPN = servPortNum;
//                }
        }
    }
    
    @Override
    public void inputMaxDataRate()
    {
        // Requests user to input transfer direction
        System.out.println("Please enter the maximum data rate for the "
                + "internet gateway connection (in up to 65,535 bytes): ");
        int tempDataRate = console.nextInt();
        setMaxDataRate(tempDataRate);
    }
    
    @Override
    public void inputErrorRate()
    {
        System.out.println("Please enter the error rate for the connection in "
                + "percentage (i.e. XX.XX%): ");
        double tempErrorRate = console.nextDouble();
        tempErrorRate = tempErrorRate / 100;
        this.setErrorRate(tempErrorRate);
    }
    
    @Override
    public void inputCorruptRate()
    {
        System.out.println("Please enter the packet corrupt rate for the "
                + "connection: ");
        double tempCorruptRate = console.nextDouble();
        this.setCorruptRate(tempCorruptRate);
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
        dtgmTransNum2++;
        
        // Display the message
//        String sentMessage = new String(msgBytes, 0, sendPacket.getLength());
        if(isTraceOn)
        {
            System.out.println("Message sent is: ");
            deviceLog.println("Message sent is: ");
            printMessage(msgBytes);
        }
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
}