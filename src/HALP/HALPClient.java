package HALP;

// Simple echo client.

import java.io.*;
import java.net.*;
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
    
    public HALPClient(int clntPN, int igPN, int servPN) throws SocketException
    {
        clntPortNum = clntPN;
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

    @Override
    public void convertDestIPToBytes() 
    {
        destIPBytes = servINAddr.getAddress();
    }

    @Override
    public void convertDestPNToBytes() 
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
    
    @Override
    public void run() 
    {
        // User input
//        clntInputIGIP();
//        clntInputServIP();
        
        // Hard coded values
        setIGIP(testIGIP);
        setServerIP(testServIP);
        
        convertDestIPToBytes();
        convertDestPNToBytes();
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
        
    @Override
    public void closeConnection() 
    {
        clntSocket.close();
    }

    public static void main (String args[]) throws Exception 
    {	
        Scanner console = new Scanner(System.in);
        HALPClient halpClient = new HALPClient(IG_PORT, IG_PORT, SERVER_PORT);
        halpClient.run();
    }

}