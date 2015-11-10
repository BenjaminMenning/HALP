package HALP;

// Simple echo client.

import static HALP.HALP.FLAG_OFFSET;
import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HALPClient extends HALP implements HALPClientInterface
{
    private static final int SERVER_PORT = 54001;  
    private static final int IG_PORT = 54001;
    
    public HALPClient() throws SocketException
    {
        clntSocket = new DatagramSocket();
    }
    
    public HALPClient(int igPN, int servPN) throws SocketException
    {
        igPortNum = igPN;
        servPortNum = servPN;
        clntSocket = new DatagramSocket();
    }
    
    @Override
    public void inputIGIP() 
    {
        System.out.println("Please enter the internet gateway IP address: ");
        String igIPAddress = console.nextLine();
        setIGIP(igIPAddress);
    }

    @Override
    public void inputServIP() 
    {
        System.out.println("Please enter the server IP address: ");
        String servIPAddress = console.nextLine();
        setServerIP(servIPAddress);
    }
    
    public byte[] setDestIP(byte[] headerBytes, String destIP)
    {
        // for future use?
//        byte tempHdrBytes[] = new byte[HEDR_LEN];
//        tempHdrBytes = headerBytes;
        
        byte tempHdrBytes[] = headerBytes;
        byte tempIPBytes[] = new byte[DESTIP_LEN];
        tempIPBytes = convertIPToBytes(destIP);
//        tempIPBytes = destIN.getAddress();
//        tempHdrBytes[DESTIP_OFFSET] = tempIPBytes;
        System.arraycopy(tempIPBytes, 0, tempHdrBytes, DESTIP_OFFSET, 
                DESTIP_LEN);   
        return tempHdrBytes;
        
//        byte tempHdrBytes[] = headerBytes;
//        byte tempFlagBytes[] = Arrays.copyOfRange(headerBytes, FLAG_OFFSET, 
//            (FLAG_OFFSET + FLAG_LEN));
//        byte tempFlagByte = tempFlagBytes[0];
//        
//        // If the ACK flag is not already set to desired value, flip bit
//        if(isSet != isACKFlagSet(headerBytes))
//        {
//            tempFlagByte ^= 1 << 2;
//            tempFlagBytes[0] = tempFlagByte; 
//        }
//        
//        tempHdrBytes[FLAG_OFFSET] = tempFlagByte;
//        return tempHdrBytes;
    }
    
    public byte[] setDestPN(byte[] headerBytes, int portNum)
    {
        byte tempHdrBytes[] = headerBytes;
        byte tempPNBytes[] = new byte[DESTPN_LEN];
        tempPNBytes = convertPNToBytes(portNum);
//        tempHdrBytes[DESTIP_OFFSET] = tempIPBytes;
        System.arraycopy(tempPNBytes, 0, tempHdrBytes, DESTPN_OFFSET, 
                DESTPN_LEN);   
        return tempHdrBytes;
    }

    @Override
    public byte[] convertIPToBytes(String ipAddr) 
    {
        try {
            InetAddress testINAddr = InetAddress.getByName(ipAddr);
            byte[] tempIPBytes = testINAddr.getAddress();
            return tempIPBytes;
        } catch (UnknownHostException ex) {
            Logger.getLogger(HALPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] blankBytes = new byte[4];
        return blankBytes;
    }

    @Override
    public byte[] convertPNToBytes(int portNum) 
    {
        // Creates a string containing the binary string of the
        // port number integer
        byte[] tempPNBytes = new byte[2];
        String serverPortBin = Integer.toBinaryString(portNum);

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
        tempPNBytes[0] = (byte) part1;
        tempPNBytes[1] = (byte) part2;
        return tempPNBytes;
    }
    
    public void initiateConnection() 
    {
        // User input
//        inputIGIP();
//        inputServIP();

        
        byte[] tempHeader = new byte[HEDR_LEN];

        // Hard coded values
        setIGIP(testIGIP);
        setServerIP(testServIP);
        tempHeader = setDestIP(tempHeader, servINAddr);
        tempHeader = setDestPN(tempHeader, servPortNum);
//        convertDestIPToBytes();
//        convertDestPNToBytes();
        setData();
//        setSYNFlag(,true);
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
    
    @Override
    public void run()
    {
        
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
        byte[] receivedData = new byte[MSG_SIZE];

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
        
    @Override
    public void closeConnection() 
    {
        clntSocket.close();
    }

    public static void main (String args[]) throws Exception 
    {	
        Scanner console = new Scanner(System.in);
        HALPClient halpClient = new HALPClient(IG_PORT, SERVER_PORT);
        halpClient.initiateConnection();
    }
}