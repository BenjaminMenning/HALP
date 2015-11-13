/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HALP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static java.lang.System.console;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ben
 */
public abstract class HALP implements HALPInterface
{
    protected Scanner console = new Scanner(System.in);
    
    ArrayList<Integer> maxTransmissions = new ArrayList<>();
    
    protected boolean connectionActive = false;
    protected boolean isTraceOn = false;
        
    protected String clntIPAddr = "";
    protected String igIPAddr = "";
    protected String servIPAddr = "";
    
    protected InetAddress servINAddr;
    protected InetAddress igINAddr;
    
    protected int clntPortNum = 0;
    protected int igPortNum = 0;
    protected int servPortNum = 0;
    protected int errorRate = 0;
    
    // For datagrams received
    protected static final int MSG_SIZE = 84;
    
    protected int currMsgLen = 0;
    
    // Byte arrays for message fields
    protected byte[] destIPBytes = new byte[DESTIP_LEN];
    protected byte[] destPNBytes = new byte[DESTPN_LEN];
    protected byte[] crcBytes = new byte[CRC_LEN];
    protected byte[] seqBytes = new byte[SEQ_LEN];
    protected byte[] ackBytes = new byte[ACK_LEN];
    protected byte[] flagBytes = new byte[FLAG_LEN];
    protected byte[] rsvdBytes = new byte[RSVD_LEN];
    protected byte[] hedrBytes = new byte[HEDR_LEN];
    protected byte[] dtrtBytes = new byte[DTRT_LEN];
    protected byte[] fileBytes = new byte[10]; // placeholder value
    protected byte[] dataBytes = new byte[DTRT_LEN];
    
    protected byte[] currMsg; // message to be sent
    protected byte[] rcvdMsg; // received message
    protected ArrayList<byte[]> messageQueue = new ArrayList<byte[]>();
    
    // Constants for header field lengths in bytes
    protected static final int DESTIP_LEN = 4;
    protected static final int DESTPN_LEN = 2;
    protected static final int CRC_LEN = 2;
    protected static final int SEQ_LEN = 4;
    protected static final int ACK_LEN = 4;
    protected static final int FLAG_LEN = 1;
    protected static final int RSVD_LEN = 3;
    protected static final int HEDR_LEN = 20;
    protected static final int DTRT_LEN = 2;

    // Constants for header field byte offsets
    protected static final int DESTIP_OFFSET = 0;
    protected static final int DESTPN_OFFSET = 4;
    protected static final int CRC_OFFSET = 6;
    protected static final int SEQ_OFFSET = 8;
    protected static final int ACK_OFFSET = 12;
    protected static final int FLAG_OFFSET = 16;
    protected static final int RSVD_OFFSET = 17; 
    protected static final int DATA_OFFSET = 20;
    protected static final int DTRT_OFFSET = 20; // data rate
    protected static final int FILE_OFFSET = 22;
    
    protected DatagramPacket currDtgm; // received datagram?
    
    protected DatagramSocket clntSocket;
    protected DatagramSocket igSocket;
    protected DatagramSocket servSocket;

    
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
    public byte[] setRSTFlag(byte[] headerBytes, boolean isSet)
    {
        return headerBytes;
    }
    
    @Override
    public byte[] setDRTFlag(byte[] headerBytes, boolean isSet)
    {
        byte tempHdrBytes[] = headerBytes;
        byte tempFlagBytes[] = Arrays.copyOfRange(headerBytes, FLAG_OFFSET, 
            (FLAG_OFFSET + FLAG_LEN));
        byte tempFlagByte = tempFlagBytes[0];
        
        // If the DRT flag is not already set to desired value, flip bit
        if(isSet != isDRTFlagSet(headerBytes))
        {
            tempFlagByte ^= 1 << 3;
            tempFlagBytes[0] = tempFlagByte; 
        }
        
        tempHdrBytes[FLAG_OFFSET] = tempFlagByte;
        return tempHdrBytes;
    }
    
    @Override
    public byte[] setACKFlag(byte[] headerBytes, boolean isSet)
    {
        byte tempHdrBytes[] = headerBytes;
        byte tempFlagBytes[] = Arrays.copyOfRange(headerBytes, FLAG_OFFSET, 
            (FLAG_OFFSET + FLAG_LEN));
        byte tempFlagByte = tempFlagBytes[0];
        
        // If the ACK flag is not already set to desired value, flip bit
        if(isSet != isACKFlagSet(headerBytes))
        {
            tempFlagByte ^= 1 << 2;
            tempFlagBytes[0] = tempFlagByte; 
        }
        
        tempHdrBytes[FLAG_OFFSET] = tempFlagByte;
        return tempHdrBytes;
    }
    
    @Override
    public byte[] setSYNFlag(byte[] headerBytes, boolean isSet)
    {
        byte tempHdrBytes[] = headerBytes;
        byte tempFlagBytes[] = Arrays.copyOfRange(headerBytes, FLAG_OFFSET, 
            (FLAG_OFFSET + FLAG_LEN));
        byte tempFlagByte = tempFlagBytes[0];
        
        // If the SYN flag is not already set to desired value, flip bit
        if(isSet != isSYNFlagSet(headerBytes))
        {
            tempFlagByte ^= 1 << 1; // 1 on the right is the bit position in byte
            tempFlagBytes[0] = tempFlagByte; 
        }
        
        tempHdrBytes[FLAG_OFFSET] = tempFlagByte;
        return tempHdrBytes;
    }
    
    @Override
    public byte[] setFINFlag(byte[] headerBytes, boolean isSet)
    {
        byte tempHdrBytes[] = headerBytes;
        byte tempFlagBytes[] = Arrays.copyOfRange(headerBytes, FLAG_OFFSET, 
            (FLAG_OFFSET + FLAG_LEN));
        byte tempFlagByte = tempFlagBytes[0];
        
        // If the FIN flag is not already set to desired value, flip bit
        if(isSet != isFINFlagSet(headerBytes))
        {
            tempFlagByte ^= 1 << 0;
            tempFlagBytes[0] = tempFlagByte; 
        }
        
        tempHdrBytes[FLAG_OFFSET] = tempFlagByte;
        return tempHdrBytes;
    }
    
    @Override
    public byte[] getHeader(byte[] messageBytes) {
        byte[] header = new byte[20];
        System.arraycopy(messageBytes, 0, header, 0, 20);
        return header;
    }

    @Override
    public byte[] getData(byte[] messageBytes) 
    {
        int originalLength = Array.getLength(messageBytes);
        byte [] data = new byte[originalLength];
        System.arraycopy(messageBytes, 20, data, 0, originalLength-20);
        return data;
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
        byte[] portBytes = Arrays.copyOfRange(messageBytes, DESTPN_OFFSET, 
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
    
    public String getFileNameField(byte[] messageBytes)
    {
        int fileNameLen = messageBytes.length - HEDR_LEN - DTRT_LEN;
        byte fileNameBytes[] = Arrays.copyOfRange(messageBytes, FILE_OFFSET, 
                (FILE_OFFSET + fileNameLen));
        String fileNameStr = new String(fileNameBytes);
        return fileNameStr;
    }
    
    @Override
    public boolean isChecksumValid(byte[] headerBytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRSTFlagSet(byte[] headerBytes) {
        byte flagBytes[] = Arrays.copyOfRange(headerBytes, FLAG_OFFSET, 
                (FLAG_OFFSET + FLAG_LEN));

        int bitPosition = 4 % 8;  // Position of this bit in a byte

        return (flagBytes[0] >> bitPosition & 1) == 1;
    }

    @Override
    public boolean isDRTFlagSet(byte[] headerBytes) 
    {
        byte flagBytes[] = Arrays.copyOfRange(headerBytes, FLAG_OFFSET, 
                (FLAG_OFFSET + FLAG_LEN));

        int bitPosition = 3 % 8;  // Position of this bit in a byte

        return (flagBytes[0] >> bitPosition & 1) == 1;
    }

    @Override
    public boolean isACKFlagSet(byte[] headerBytes) 
    {
        byte tempFlagBytes[] = Arrays.copyOfRange(headerBytes, FLAG_OFFSET, 
                (FLAG_OFFSET + FLAG_LEN));

        int bitPosition = 2 % 8;  // Position of this bit in a byte

        return (tempFlagBytes[0] >> bitPosition & 1) == 1;
    }

    @Override
    public boolean isSYNFlagSet(byte[] headerBytes) 
    {
        byte tempFlagBytes[] = Arrays.copyOfRange(headerBytes, FLAG_OFFSET, 
                (FLAG_OFFSET + FLAG_LEN));

        int bitPosition = 1 % 8;  // Position of this bit in a byte

        return (tempFlagBytes[0] >> bitPosition & 1) == 1;
    }

    @Override
    public boolean isFINFlagSet(byte[] headerBytes) 
    {
        byte flagBytes[] = Arrays.copyOfRange(headerBytes, FLAG_OFFSET, 
                (FLAG_OFFSET + FLAG_LEN));

        int bitPosition = 0 % 8;  // Position of this bit in a byte

        return (flagBytes[0] >> bitPosition & 1) == 1;
    }
    
    @Override
    public byte[] getMessage()
    {
        return currMsg;
    }
    
    @Override
    public byte[] assembleMessage(byte[] headerBytes, byte[] dataBytes) 
    {
        byte[] tempHdrBytes = headerBytes;
        byte[] tempDtaBytes = dataBytes;
        byte[] tempMsgBytes = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
//            outputStream.write(destIPBytes);
//            outputStream.write(destPNBytes);
//            outputStream.write(crcBytes);
//            outputStream.write(seqBytes);
//            outputStream.write(ackBytes);
//            outputStream.write(flagBytes);
//            outputStream.write(rsvdBytes);
//            outputStream.write(dtrtBytes);
            outputStream.write(tempHdrBytes);
            outputStream.write(dataBytes);
            tempMsgBytes = outputStream.toByteArray();
//            currMsgLen = currMsg.length; // remove later?
//            messageQueue.add(currMsg); // remove later?
        } catch (IOException ex) {
            Logger.getLogger(HALP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tempMsgBytes;
    }
    
    @Override
    public boolean setTrace(boolean isTraceSet) {
        boolean trace = isTraceSet;
        return trace;
    }

    @Override
    public double getFileSize() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long startTransferTimer() {
        long start = System.currentTimeMillis()/1000;
        return start;
    }

    @Override
    public long stopTransferTimer() {
        long end = System.currentTimeMillis()/1000;
        return end;
    }

    @Override
    public String getTransferTime(long start, long end) {
        long time = end - start;
        String output = "The time to transfer file was " + time + " secounds.";
        return output;
    }

    @Override
    public int getMessagesGenerated() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getUDPDatagramsTransmitted() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getTotalRetransmissions() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getPercentageOfRetransmissions() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void printTraceStats() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void printDestIPField(byte[] headerBytes)
    {
        String flagInfo = "Destination IP: ";
        String destIPStr = getDestinationIP(headerBytes);
        flagInfo += destIPStr;
//        byte tempFlagBytes[] = Arrays.copyOfRange(headerBytes, DESTIP_OFFSET, 
//            (DESTIP_OFFSET + DESTIP_LEN));
//        byte tempFlagByte = tempFlagBytes[0];
//        flagInfo += Integer.toBinaryString((int)tempFlagByte);
        System.out.println(flagInfo);
    }
    
    @Override
    public void printDestPNField(byte[] headerBytes)
    {
        String flagInfo = "Destination IP: ";
        int destIPStr = getDestinationPort(headerBytes);
        flagInfo += destIPStr;
//        byte tempFlagBytes[] = Arrays.copyOfRange(headerBytes, DESTIP_OFFSET, 
//            (DESTIP_OFFSET + DESTIP_LEN));
//        byte tempFlagByte = tempFlagBytes[0];
//        flagInfo += Integer.toBinaryString((int)tempFlagByte);
        System.out.println(flagInfo);
    }

    @Override
    public void printFlagField(byte[] headerBytes)
    {
        String flagInfo = "Flag Bits: ";
        byte tempFlagBytes[] = Arrays.copyOfRange(headerBytes, FLAG_OFFSET, 
            (FLAG_OFFSET + FLAG_LEN));
        byte tempFlagByte = tempFlagBytes[0];
        flagInfo += Integer.toBinaryString((int)tempFlagByte);
        System.out.println(flagInfo);
    }
    
    @Override
    public void printFileNameField(byte[] messageBytes)
    {
        String flNmInfo = "File name: ";
        int fileNameLen = messageBytes.length - HEDR_LEN - DTRT_LEN;
        byte tempFlNmBytes[] = Arrays.copyOfRange(messageBytes, FILE_OFFSET, 
                (FILE_OFFSET + fileNameLen));
        String fileNameStr = new String(tempFlNmBytes);
        flNmInfo += fileNameStr;
        System.out.println(flNmInfo);
    }
    
    @Override
    public void printDataField(byte[] messageBytes)
    {
        String dataInfo = "Data: ";
        int dataLen = messageBytes.length - HEDR_LEN;
        byte tempDataBytes[] = Arrays.copyOfRange(messageBytes, DATA_OFFSET, 
                (DATA_OFFSET + dataLen));
        dataInfo += tempDataBytes;
        System.out.println(dataInfo);
    }
    
    @Override
    public void printMessage(byte[] messageBytes) 
    {
        printDestIPField(messageBytes);
        printDestPNField(messageBytes);
        printFlagField(messageBytes);
        System.out.println();
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
}