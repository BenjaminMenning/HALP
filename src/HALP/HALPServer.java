package HALP;

// Simple echo server.  Modified from some example on the Internet.

import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HALPServer extends HALPClient implements HALPServerInterface
{
    static DatagramSocket serverSocket;

    private static final int SERVER_PORT = 54001;
    
    public HALPServer() throws SocketException
    {
        deviceSocket = new DatagramSocket();
    }
    
    public HALPServer(int servPN) throws SocketException
    {
        servPortNum = servPN;
        deviceSocket = new DatagramSocket(servPN);
    }
    
    @Override
    public void run() 
    {
        boolean placeholderCondition = false;
        System.out.println("Server has started.");
        while(placeholderCondition == false)
        {
            try {
                byte[] rcvdMsg = receiveMessage();
                fileName = "dl-" + getFileNameField(rcvdMsg);
                boolean isSyn = isSYNFlagSet(rcvdMsg);
                boolean isAck = isACKFlagSet(rcvdMsg);
                if(isSyn && !isAck) 
                {
                    rcvdMsg = setACKFlag(rcvdMsg, true);
                    
                    // Retrieves the outgoing connection info from the datagram
                    igIPAddr = currDtgm.getAddress().getHostAddress();
                    igINAddr = InetAddress.getByName(igIPAddr);
                    igPortNum = currDtgm.getPort();
                }
                sendMessage(rcvdMsg);
                runAsReceiver();
                placeholderCondition = true;
            } 
            catch (Exception ex)
            {
                Logger.getLogger(HALPIG.class.getName()).log(Level.SEVERE, null,
                        ex);
            }
        }
    }
    
//    @Override
//    public void runAsSender() 
//    {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void runAsReceiver() 
//    {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    public static void main(String args[]) throws Exception
    {
        Scanner console = new Scanner(System.in);
        HALPServer halpServer = new HALPServer(SERVER_PORT);
        halpServer.run();
//        Scanner console = new Scanner(System.in);
//        
//        // Request user input to retrieve the port number
//        System.out.println("Please enter the port number:");
//        String portNumberStr = console.nextLine();
//		try {
//			// Open a UDP datagram socket with a specified port number
//			int portNumber = PORT;
////                        int portNumber = Integer.parseInt(portNumberStr);
//
//			serverSocket = new DatagramSocket(portNumber);
//
//			System.out.println("Echo server starts ...");
//
//			// Create a buffer for receiving
//			byte[] receivedData = new byte[4096];
//			// Run forever
//			while (true) {
//				// Create a datagram
//				DatagramPacket receivedDatagram =
//					new DatagramPacket(receivedData, receivedData.length);
//
//				// Receive a message			
//				serverSocket.receive(receivedDatagram);
//
//				// Prepare for sending an echo message
//				InetAddress destination = receivedDatagram.getAddress();			
//				int clientPortNumber = receivedDatagram.getPort();
//				int lengthOfMessage = receivedDatagram.getLength();			
//				String message = new String(receivedData, 0, receivedDatagram.getLength());
//
//				// Display received message and client address		 
//				System.out.println("The received message is: " + message);
//				System.out.println("The client address is: " + destination);
//				System.out.println("The client port number is: " + clientPortNumber);
//
//				// Create a buffer for sending
//				byte[] data = new byte[lengthOfMessage];
//				data = message.getBytes();
//                                
//				// Create a datagram
//				DatagramPacket datagram = 
//					new DatagramPacket(data, lengthOfMessage, destination, clientPortNumber);
//
//				// Send a message			
//				serverSocket.send(datagram);
//			}
//		} 
//		catch (IOException ioEx) {
//			ioEx.printStackTrace();
//		} 
//		finally {
//			// Close the socket 
//			serverSocket.close();
//		}
    }
}