package HALP.Lab2Reference;

// Simple echo client.

import java.io.*;
import java.net.*;
import java.lang.*;
import static java.lang.System.console;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class EchoClient2 {
    static DatagramSocket clientSocket;
        private static final int SERVER_PORT = 56789;  
        private static final int IGPORT = 45666;
        
        
	public static void main (String args[]) throws Exception 
	{	
            Scanner console = new Scanner(System.in);
           
            // Request user input to retrieve the IP addresses of gateway and server
            System.out.println("Please enter the internet gateway IP address: ");
            String ipAddress = console.nextLine();
            System.out.println("Please enter the server IP address: ");
            String SERVER_IP_ADDRESS = console.nextLine();
            
           
		try {
			// Open a UDP datagram socket
			clientSocket = new DatagramSocket();
                        

			// Prepare for transmission
			// Determine gateway IP address
			InetAddress destination = InetAddress.getByName(ipAddress);		

			// Determine gateway port number
                        int portNumber = IGPORT;
                        
                        // Determine server IP address
                        InetAddress serverIP = InetAddress.getByName(SERVER_IP_ADDRESS);
                        
			// Message and its length		
//			String message = "Hello World!";
                        String message = "Yoyoyoyoyoyo";
			int lengthOfMessage = message.getBytes().length + 6;
			byte[] data = new byte[lengthOfMessage];
                        byte[] serverIPBytes = new byte[4];
                        byte[] serverPNBytes = new byte[2];
                        byte[] messageBytes = new byte[lengthOfMessage];
                        
                        // Splits IP address up into an array of segments
                        String[] segments = SERVER_IP_ADDRESS.split("\\."); 

                        // Goes through array and parses String segments to ints
                        // and assigns to bytes
                        for (int i = 0; i < 4; i++) {
                            int temp = Integer.parseInt(segments[i]);
                            serverIPBytes[i] = (byte) temp;
                        }
                        
                        serverIPBytes = serverIP.getAddress();
                        
                        // Creates a string containing the binary string of the
                        // port number integer
                        String serverPortBin = Integer.toBinaryString(SERVER_PORT);
                        
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
                        serverPNBytes[0] = (byte) part1;
                        serverPNBytes[1] = (byte) part2;
                       
                       
                        // Writes bytes to output stream to insert into byte array
                        messageBytes = message.getBytes();
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
                        outputStream.write(serverIPBytes);
                        outputStream.write(serverPNBytes);
                        outputStream.write(messageBytes);
                        data = outputStream.toByteArray();
                       
                        
			// Create a datagram
			DatagramPacket datagram = 
				new DatagramPacket(data, lengthOfMessage, destination, portNumber);

			// Send a message
			clientSocket.send(datagram);

			// Print out the message sent
			System.out.println("Message sent is:   [" + message + "]");

			// Prepare for receiving
			// Create a buffer for receiving
			byte[] receivedData = new byte[4096];

			// Create a datagram
			DatagramPacket receivedDatagram = 
				new DatagramPacket(receivedData, receivedData.length);

			// Receive a message
			clientSocket.receive(receivedDatagram);

			// Display the message
			String echodMessage = new String(receivedData, 0, receivedDatagram.getLength());
			System.out.println("Message echoed is: [" + echodMessage + "]");	
		} 
		catch (IOException ioEx) {
			ioEx.printStackTrace();
		} 
		finally {
			// Close the socket 
			clientSocket.close();
		}
	}
}