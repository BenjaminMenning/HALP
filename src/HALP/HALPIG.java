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
    private InetAddress outgoingIN;
    
    private static final int IG_PORT = 54001;
    
    public HALPIG() throws SocketException
    {
        igSocket = new DatagramSocket();
    }
    
    public HALPIG(int igPN) throws SocketException
    {
        igPortNum = igPN;
        igSocket = new DatagramSocket(igPN);
    }

    @Override
    public void run() 
    {
        while(true)
        {
            try {
                byte[] rcvdMsg = receiveMessage();
                if(isSYNFlagSet(rcvdMsg)) 
                {
                    ingoingIP = currDtgm.getAddress().getHostAddress();
                    ingoingPN = currDtgm.getPort();
                    outgoingIP = getDestinationIP(rcvdMsg);
                    outgoingPN = getDestinationPort(rcvdMsg);
                    outgoingIN = InetAddress.getByName(outgoingIP);
                }
//                else if(isSYNFlagSet(rcvdMsg))
                printMessage(rcvdMsg);
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
    public void sendMessage(byte[] messageBytes) throws Exception 
    {
        byte[] msgBytes = messageBytes;
        int msgLen = msgBytes.length;
        DatagramPacket sendPacket = 
                new DatagramPacket(msgBytes, msgLen, outgoingIN, outgoingPN);

        // Send a message
        igSocket.send(sendPacket);
        
        // Display the message
        String sentMessage = new String(msgBytes, 0, sendPacket.getLength());
        System.out.println("Message sent is: [" + sentMessage + "]");	
    }
    
    public byte[] receiveMessage()
    {
        byte[] receivedData = new byte[MSG_SIZE];

        // Create a datagram
        DatagramPacket receivedDatagram = 
                new DatagramPacket(receivedData, receivedData.length);

        try 
        {
            // Receive a message
            igSocket.receive(receivedDatagram);
        } 
        catch (IOException ex) {
            Logger.getLogger(HALP.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
        currDtgm = receivedDatagram;
//        currMsg = receivedDatagram.getData();
        return receivedData;
    }
    
    @Override
    public void closeConnection() 
    {
        igSocket.close();
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
        halpIG.run();
    }
}