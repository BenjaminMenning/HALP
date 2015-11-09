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
    ArrayList<Integer> maxTransmissions = new ArrayList<>();
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
    public void convertBytesToDestIP()
    {
        
    }
    
    @Override
    public void convertBytesToDestPN()
    {
        
    }
    
    @Override
    public String getDestinationIP(byte[] headerBytes) {
        int byte1 = (headerBytes[0] & 0xff);
        int byte2 = (headerBytes[1] & 0xff);
        int byte3 = (headerBytes[2] & 0xff);
        int byte4 = (headerBytes[3] & 0xff);
         String destinationIP = byte1 + "." + byte2 + "." + byte3 + "." + byte4;
        return destinationIP;
    }

    @Override
    public int getDestinationPort(byte[] messageBytes) 
    {
        // Create and assign port bytes
        byte[] portBytes = Arrays.copyOfRange(currMsg, DESTPN_OFFSET, 
                (DESTPN_OFFSET + DESTPN_LEN));

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
        return completePN;
    }
    
    @Override
    public void sendMessage() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() 
    {
        while(true)
        {
            receiveMessage();
            clntIPAddr = currDtgm.getAddress().getHostAddress();
            servIPAddr = getDestinationIP(currMsg);
            servPortNum = getDestinationPort(currMsg);
        }
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
            igSocket.receive(receivedDatagram);
        } catch (IOException ex) {
            Logger.getLogger(HALP.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
        currDtgm = receivedDatagram;
        currMsg = receivedDatagram.getData();
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
            
    /** 
     * This class is a comparator that compares transmissions. It orders them 
     * in ascending order.
     * 
     * @author Benjamin Menning
     * @version 04/21/2015
     */
    public class maxTransmissionsComparator implements Comparator<Integer>
    {
        @Override
        public int compare(Integer x, Integer y)
        {
            if (x < y)
            {
                return 1;
            }
            if (x > y)
            {
                return -1;
            }
            return 0;
        }
    }    
    
    public void addMaxTransmission(int maxTransmission)
    {
        maxTransmissions.add(maxTransmission);
    }
    
    public int getMaxTransmission()
    {
        Collections.sort(maxTransmissions, new maxTransmissionsComparator());
        int maxTransmission = maxTransmissions.get(0);
        return maxTransmission;
    }
    
    public boolean isMaxTransmissionsEmpty()
    {
        boolean isEmpty = maxTransmissions.isEmpty();
        return isEmpty;
    }

    public static void main(String args[]) throws Exception
    {
        Scanner console = new Scanner(System.in);
        HALPIG halpIG = new HALPIG(IG_PORT);
        halpIG.run();
    }
}