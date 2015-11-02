/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HALP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static java.lang.System.console;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ben
 */
public class HALP implements HALPInterface
{
    private Scanner console = new Scanner(System.in);
    
    private boolean connectionActive = false;
    
    // Hard coded IP addresses for testing
    private String homeTestIP = "192.168.0."; // for testing at home
    private String testIGIP = homeTestIP + "110";
    private String testServIP = homeTestIP + "114";
    
    private String clntIPAddr = "";
    private String igIPAddr = "";
    private String servIPAddr = "";
    
    private InetAddress servINAddr;
    private InetAddress igINAddr;
    
    private int clntPortNum = 0;
    private int igPortNum = 0;
    private int servPortNum = 0;
    private int errorRate = 0;
    
    private int currMsgLen = 0;
    
    // Byte arrays for message fields
    private byte[] destIPBytes = new byte[DESTIP_LEN];
    private byte[] destPNBytes = new byte[DESTPN_LEN];
    private byte[] crcBytes = new byte[CRC_LEN];
    private byte[] seqBytes = new byte[SEQ_LEN];
    private byte[] ackBytes = new byte[ACK_LEN];
    private byte[] flagBytes = new byte[FLAG_LEN];
    private byte[] rsvdBytes = new byte[RSVD_LEN];
    private byte[] hedrBytes = new byte[HEDR_LEN];
    private byte[] dtrtBytes = new byte[DTRT_LEN];
    private byte[] fileBytes = new byte[10]; // placeholder value
    private byte[] dataBytes = new byte[DTRT_LEN];
    private byte[] currMsg;
    private ArrayList<byte[]> messageQueue = new ArrayList<byte[]>();
    
    // Constants for header field lengths in bytes
    private static final int DESTIP_LEN = 4;
    private static final int DESTPN_LEN = 2;
    private static final int CRC_LEN = 2;
    private static final int SEQ_LEN = 4;
    private static final int ACK_LEN = 4;
    private static final int FLAG_LEN = 1;
    private static final int RSVD_LEN = 3;
    private static final int HEDR_LEN = 20;
    private static final int DTRT_LEN = 2;

    // Constants for header field byte offsets
    private static final int DESTIP_OFFSET = 0;
    private static final int DESTPN_OFFSET = 4;
    private static final int CRC_OFFSET = 6;
    private static final int SEQ_OFFSET = 8;
    private static final int ACK_OFFSET = 12;
    private static final int FLAG_OFFSET = 16;
    private static final int RSVD_OFFSET = 17; 
    private static final int DATA_OFFSET = 20;
    private static final int DTRT_OFFSET = 20; // data rate
    private static final int FILE_OFFSET = 22;
    
    private DatagramSocket clntSocket;
    private DatagramSocket igSocket;
    private DatagramSocket servSocket;

    public HALP() throws SocketException
    {
        clntSocket = new DatagramSocket();
        igSocket = new DatagramSocket();
        servSocket = new DatagramSocket();
    }
    
    public HALP(int clntPN, int igPN, int servPN) throws SocketException
    {
        clntPortNum = clntPN;
        igPortNum = igPN;
        servPortNum = servPN;
        clntSocket = new DatagramSocket();
        igSocket = new DatagramSocket();
        servSocket = new DatagramSocket();
    }
    
    @Override
    public void setClientIP(String ipAddr) 
    {
        clntIPAddr = ipAddr;
    }

    @Override
    public void setClientPort(int portNum) 
    {
        clntPortNum = portNum;
    }

    @Override
    public String getClientIP() 
    {
        return clntIPAddr;
    }

    @Override
    public int getClientPort() 
    {
        return clntPortNum;
    }

    @Override
    public void setIGIP(String ipAddr) 
    {
        igIPAddr = ipAddr;
        try {
            igINAddr = InetAddress.getByName(igIPAddr);
        } catch (UnknownHostException ex) {
            Logger.getLogger(HALP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setIGPort(int portNum) 
    {
        igPortNum = portNum;
    }

    @Override
    public String getIGIP() 
    {
        return igIPAddr;
    }

    @Override
    public int getIGPort() 
    {
        return igPortNum;
    }

    @Override
    public void setServerIP(String ipAddr) 
    {
        servIPAddr = ipAddr;
        try {
            servINAddr = InetAddress.getByName(servIPAddr);
        } catch (UnknownHostException ex) {
            Logger.getLogger(HALP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setServerPort(int portNum) 
    {
        servPortNum = portNum;
    }

    @Override
    public String getServerIP() 
    {
        return servIPAddr;
    }

    @Override
    public int getServerPort() 
    {
        return servPortNum;
    }
    
    @Override
    public void setData() 
    {
        String dataStr = "Yoyoyoyoyo";
        dataBytes = dataStr.getBytes();
    }

    
    @Override
    public byte[] getHeader(byte[] messageBytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] getData(byte[] messageBytes) 
    {
        byte[] placeholder = new byte[1];
        return placeholder;
    }

    @Override
    public String getDestinationIP(byte[] messageBytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getDestinationPort(byte[] messageBytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isChecksumValid(byte[] headerBytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRSTFlagSet(byte[] headerBytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isDRTFlagSet(byte[] headerBytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isAckFlagSet(byte[] headerBytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isSynFlagSet(byte[] headerBytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isFinFlagSet(byte[] headerBytes) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public void clntInputIGIP() 
    {
        System.out.println("Please enter the internet gateway IP address: ");
        String igIPAddress = console.nextLine();
        setIGIP(igIPAddress);
    }

    @Override
    public void clntInputServIP() 
    {
        System.out.println("Please enter the server IP address: ");
        String servIPAddress = console.nextLine();
        setServerIP(servIPAddress);
    }

    @Override
    public void clntConvertDestIPToBytes() 
    {
        destIPBytes = servINAddr.getAddress();
    }

    @Override
    public void clntConvertDestPNToBytes() 
    {
        // Creates a string containing the binary string of the
        // port number integer
        String serverPortBin = Integer.toBinaryString(servPortNum);

        // Add 0's if length is less than 16 bits
        if(serverPortBin.length() < 16)
        {
            int zeroCount = 16 - serverPortBin.length();
            while (zeroCount > 0)
            {
                serverPortBin = "0" + serverPortBin;
                zeroCount--;
            }
        }

        // Parses binary string into two integers for two
        // separate bytes and assigns them
        int part1 = Integer.parseInt(serverPortBin.substring(0, 8), 2);
        int part2 = Integer.parseInt(serverPortBin.substring(8, 16),2);
        destPNBytes[0] = (byte) part1;
        destPNBytes[1] = (byte) part2;
    }
    
    public void igConvertBytesToDestIP()
    {
        
    }
    
    public void igConvertBytesToDestPN()
    {
        
    }

    @Override
    public void assembleMessage() 
    {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
            outputStream.write(destIPBytes);
            outputStream.write(destPNBytes);
            outputStream.write(crcBytes);
            outputStream.write(seqBytes);
            outputStream.write(ackBytes);
            outputStream.write(flagBytes);
            outputStream.write(rsvdBytes);
            outputStream.write(dtrtBytes);
            outputStream.write(dataBytes);
            currMsg = outputStream.toByteArray();
            currMsgLen = currMsg.length;
            messageQueue.add(currMsg);
        } catch (IOException ex) {
            Logger.getLogger(HALP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void sendMessage() throws Exception 
    {
        DatagramPacket sendPacket = 
                new DatagramPacket(currMsg, currMsgLen, igINAddr, igPortNum);

        // Send a message
        clntSocket.send(sendPacket);
        
        // Display the message
        String sentMessage = new String(currMsg, 0, sendPacket.getLength());
        System.out.println("Message echoed is: [" + sentMessage + "]");	
    }
    
    @Override
    public void receiveMessage()
    {
        byte[] receivedData = new byte[4096];

        // Create a datagram
        DatagramPacket receivedDatagram = 
                new DatagramPacket(receivedData, receivedData.length);

        try {
            // Receive a message
            clntSocket.receive(receivedDatagram);
        } catch (IOException ex) {
            Logger.getLogger(HALP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Display the message
        String echodMessage = new String(receivedData, 0, receivedDatagram.getLength());
        System.out.println("Message echoed is: [" + echodMessage + "]");	
    }
    
    public void igReceiveMessage()
    {
        byte[] receivedData = new byte[4096];

        // Create a datagram
        DatagramPacket receivedDatagram = 
                new DatagramPacket(receivedData, receivedData.length);

        try {
            // Receive a message
            clntSocket.receive(receivedDatagram);
        } catch (IOException ex) {
            Logger.getLogger(HALP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void runClient() 
    {
        // User input
//        clntInputIGIP();
//        clntInputServIP();
        
        // Hard coded values
        setIGIP(testIGIP);
        setServerIP(testServIP);
        
        clntConvertDestIPToBytes();
        clntConvertDestPNToBytes();
        setData();
        assembleMessage();
        try {
            sendMessage();
            receiveMessage();
        } catch (Exception ex) {
            Logger.getLogger(HALP.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            closeConnection();
        }
    }
    
    public void runIG()
    {
        try {
            receiveMessage();
            
        } catch (Exception ex) {
            Logger.getLogger(HALP.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            closeConnection();
        }
    }
    
    public void runServer()
    {
        
    }
    
    public void closeConnection()
    {
        clntSocket.close();
    }
}