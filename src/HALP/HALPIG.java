package HALP;


import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HALPIG extends HALP implements HALPIGInterface
{

    static DatagramSocket igSocket;
    private static final int PORT = 54001;
    
    public HALPIG() throws SocketException
    {
        clntSocket = new DatagramSocket();
        igSocket = new DatagramSocket();
        servSocket = new DatagramSocket();
    }
    
    public HALPIG(int clntPN, int igPN, int servPN) throws SocketException
    {
        clntPortNum = clntPN;
        igPortNum = igPN;
        servPortNum = servPN;
        clntSocket = new DatagramSocket();
        igSocket = new DatagramSocket();
        servSocket = new DatagramSocket();
    }
    
    @Override
    public void convertBytesToDestIP()
    {
        
    }
    
    @Override
    public void convertBytesToDestPN()
    {
        
    }
    
    @Override
    public String getDestinationIP(byte[] messageBytes) {
         String destinationIP = messageBytes[0] + "." + messageBytes[1] + "." +messageBytes[2] + "." +messageBytes[3];
        return destinationIP;
    }

    @Override
    public int getDestinationPort(byte[] messageBytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void sendMessage() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        
    }
    
    @Override
    public void closeConnection() 
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

    public static void main(String args[]) throws Exception
    {
        Scanner console = new Scanner(System.in);
        InetAddress clientDestination = null;
        int clientPortNumber = 0;
        InetAddress serverIP = null;
        int serverPort = 0;
        
//        HALP halpIG = new HALP(PORT, PORT, PORT);
//        halpIG.runIG();


    // Request user input to retrieve the IG port number
//        System.out.println("Please enter the IG port number:");
//        int inputPort = console.nextInt();
            try {
                    // Open a UDP datagram socket with a specified port number
//                        int igPortNumber = inputPort; // for user input
                    int igPortNumber = PORT;

                    igSocket = new DatagramSocket(igPortNumber);

                    System.out.println("Internet Gateway starts ...");

                    // Create a buffer for receiving
                    byte[] receivedData = new byte[4096];
                    // Run forever
                    while (true) {
                            // Create a datagram
                            DatagramPacket receivedDatagram =
                                    new DatagramPacket(receivedData, receivedData.length);

                            // Receive a message			
                            igSocket.receive(receivedDatagram);

                            int incomingPortNumber = receivedDatagram.getPort();

                            if(incomingPortNumber != serverPort){ //receiving from client, send on to server
                                // Prepare for sending an echo message
                                clientDestination = receivedDatagram.getAddress();   //IP of sender
                                clientPortNumber = receivedDatagram.getPort();
                                int lengthOfMessage = receivedDatagram.getLength();	
                                byte[] data = new byte[lengthOfMessage]; 
                                String message = new String(receivedData, 6, receivedDatagram.getLength());

                                // Display received message and client address		 
                                System.out.println("The received message is: " + message);
                                System.out.println("The client address is: " + clientDestination);
                                System.out.println("The client port number is: " + clientPortNumber);

                                // Create a buffer for sending
                                data = receivedDatagram.getData();

                                // Extract server IP address and server port number from client datagram payload
                                InetAddress addr = null;
                                byte [] ipAddrBytes = new byte[] {data[0],data[1],data[2],data[3]};

                                try {
                                    addr = InetAddress.getByAddress(ipAddrBytes);
                                } 
                                catch (UnknownHostException impossible) {
                                    System.out.println("Unable to determine the host by address!");
                                }

                                serverIP = addr;

                                InetAddress serverDestination = serverIP;

                                // Create and assign port bytes
                                byte[] portBytes = new byte[] {data[4], data[5]};

                                // Assign port number bytes as ints
                                int firstPNByteInt = portBytes[0];
                                int secondPNByteInt = portBytes[1];

                                // Convert byte ints to strings
                                String firstPNByteStr = Integer.toBinaryString((int) firstPNByteInt);
                                String secondPNByteStr = Integer.toBinaryString((int) secondPNByteInt);

                                // Add 0's if length is less than 8 bits
                                if(firstPNByteStr.length() < 8){
                                    int zeroCount = 8 - firstPNByteStr.length();
                                    while (zeroCount > 0){
                                        firstPNByteStr = "0" + firstPNByteStr;
                                        zeroCount--;
                                    }
                                }
                                if(secondPNByteStr.length() < 8){
                                    int zeroCount = 8 - secondPNByteStr.length();
                                    while (zeroCount > 0){
                                        secondPNByteStr = "0" + secondPNByteStr;
                                        zeroCount--;
                                    }
                                }

                                // Parse to 8 bit strings
                                String firstPNByteBits = firstPNByteStr.substring(firstPNByteStr.length() - 8);
                                String secondPNByteBits = secondPNByteStr.substring(secondPNByteStr.length() - 8);

                                // Combines 8 bit strings into one complete string
                                String completePNStr = firstPNByteBits + "" + secondPNByteBits;

                                // Converts complete String from unsigned integer to int
                               // int completePN = Integer.parseUnsignedInt(completePNStr, 2);  //requres java 8 to run this coding
                                int completePN= (int) Long.parseLong(completePNStr, 2);    //equivalent of above coding for non java8
                                serverPort = completePN;

                                //make a copy of the message bytes and put into a new byte array
                                byte[] data2 = new byte[(data.length - 6)];
                                int k = 6;
                                      for(int i =0; i<data2.length;i++){
                                          data2[i] = data[k];   
                                          k++;
                                      }

                                int totalLength = data2.length;


                                // Create a datagram
                                DatagramPacket datagram = 
                                            new DatagramPacket(data2, totalLength, serverDestination, serverPort);

                                // Send a message			
                                igSocket.send(datagram);
                            }

                            else{  //receiving from server, send on to client
                               // Prepare for sending an echo message
                                InetAddress senderDestination = receivedDatagram.getAddress();//IP of sender
                                int senderPortNumber = receivedDatagram.getPort();
                                int lengthOfMessage = receivedDatagram.getLength();			
                                String message = new String(receivedData, 0, receivedDatagram.getLength());

                                // Display received message and client address
                                System.out.println("The received message from server is: " + message);
                                System.out.println("The server address is: " + senderDestination);
                                System.out.println("The server port number is: " + senderPortNumber);

                                // Create a buffer for sending
                                byte[] data = new byte[lengthOfMessage];
                                data = message.getBytes();

                                // Create a datagram
                                DatagramPacket datagram = 
                                            new DatagramPacket(data, lengthOfMessage, clientDestination, clientPortNumber);

                                // Send a message			
                                igSocket.send(datagram);
                            }

                    }
            } 
            catch (IOException ioEx) {
                    ioEx.printStackTrace();
            } 
            finally {
                    // Close the socket 
                    igSocket.close();
            }
    }
}