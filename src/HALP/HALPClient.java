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
        // Hard coded IP addresses for testing
    protected String homeTestIP = "192.168.0."; // for testing at home
    protected String testIGIP = homeTestIP + "110";
    protected String testServIP = homeTestIP + "101";
    protected String testFileName = "alice.txt";
    
    private static final int SERVER_PORT = 54001;  
    private static final int IG_PORT = 54001;
    private String fileName = "";
    
    public HALPClient() throws SocketException
    {
        deviceSocket = new DatagramSocket();
    }
    
    public HALPClient(int igPN, int servPN) throws SocketException
    {
        igPortNum = igPN;
        servPortNum = servPN;
        deviceSocket = new DatagramSocket();
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
    
    public void inputTransferDirection()
    {
        
    }
   
    public void inputFileName()
    {
        System.out.println("Please enter the name of the file you wish to "
                + "download / upload: ");
        String tempFileName = console.nextLine();
        setFileName(tempFileName);
    }
    
    public void inputDataRate()
    {
        
    }
    
    public void setTransferDirection()
    {
        
    }
    
    public void setFileName(String fileNameStr)
    {
        fileName = fileNameStr;
    }
    
    public void setDataRate()
    {
        
    }
    
    public byte[] setFileNameField(byte[] messageBytes, String fileNameStr)
    {
        byte tempMsgBytes[] = messageBytes;
        byte tempFlNmBytes[] = fileNameStr.getBytes();
        int fileNameLen = fileNameStr.length();
        System.arraycopy(tempFlNmBytes, 0, tempMsgBytes, FILE_OFFSET, 
                fileNameLen);   
        return tempMsgBytes;
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
        byte[] tempHeader = new byte[HEDR_LEN];
        byte[] tempData = null; // change later
        byte[] tempMsg = null;
        
        // User input
//        inputIGIP();
//        inputServIP();
        
        // Hard coded values for testing
        setIGIP(testIGIP);
        setServerIP(testServIP);
        setFileName(testFileName);
        
        tempHeader = setDestIP(tempHeader, servIPAddr);
        tempHeader = setDestPN(tempHeader, servPortNum);
        tempHeader = setSYNFlag(tempHeader, true);
        printMessage(tempHeader);
        
        int dataLen = fileName.length() + DTRT_LEN;
        tempData = new byte[dataLen];
        System.out.println("Length of data field: " + dataLen);
        tempMsg = assembleMessage(tempHeader, tempData);
        tempMsg = setFileNameField(tempMsg, fileName);
        printFileNameField(tempMsg);
        
        try 
        {
            sendMessage(tempMsg);
            byte[] rcvdMsg = receiveMessage();
        } 
        catch (Exception ex) 
        {
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

    public static void main (String args[]) throws Exception 
    {	
        Scanner console = new Scanner(System.in);
        HALPClient halpClient = new HALPClient(IG_PORT, SERVER_PORT);
        halpClient.initiateConnection();
    }
}