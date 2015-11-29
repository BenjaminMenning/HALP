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
    
    protected InetAddress clntINAddr;
    protected InetAddress servINAddr;
    protected InetAddress igINAddr;
    
    protected int clntPortNum = 0;
    protected int igPortNum = 0;
    protected int servPortNum = 0;
    
    // For datagrams received
    protected int msgSize = 0;
    
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
    
//    protected byte[] currMsg; // message to be sent
//    protected byte[] rcvdMsg; // received message
    protected ArrayList<byte[]> messageQueue = new ArrayList<byte[]>();
//    protected CRC16 crc16 = new CRC16();
    
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
    protected static final int HEDR_OFFSET = 0;
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
    
//    protected DatagramSocket clntSocket;
//    protected DatagramSocket igSocket;
//    protected DatagramSocket servSocket;
    protected DatagramSocket deviceSocket;

    
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
    public byte[] setDataRateField(byte[] messageBytes, int rate)
    {
        byte tempMsgBytes[] = messageBytes;
        byte tempDRBytes[] = new byte[DTRT_LEN];
        tempDRBytes = convertPNToBytes(rate);
//        tempHdrBytes[DESTIP_OFFSET] = tempIPBytes;
        System.arraycopy(tempDRBytes, 0, tempMsgBytes, DTRT_OFFSET, 
                DTRT_LEN);   
        return tempMsgBytes;
    }
    
    @Override
    public byte[] setChecksum(byte[] messageBytes)
    {
        CRC16 crc16 = new CRC16();
        byte tempMsgBytes[] = messageBytes;
        crc16.update(tempMsgBytes, 0, tempMsgBytes.length);
        int checksum = (int) crc16.getValue();
        byte tempChkSumBytes[] = convertPNToBytes(checksum);
        System.arraycopy(tempChkSumBytes, 0, tempMsgBytes, CRC_OFFSET, 
                CRC_LEN);   
        return tempMsgBytes;
        
//        crc.update(testMessage, 0, testMessage.length);
//        System.out.println(crc.getValue());
//        byte blankByte = 0;
//        byte errorByte = halpIG.generateByteError(blankByte);
////        testMessage[0] = errorByte;
//        crc2.update(testMessage, 0, testMessage.length);
//        System.out.println(crc2.getValue());
//        
//                byte tempMsgBytes[] = messageBytes;
//        byte tempDRBytes[] = new byte[DTRT_LEN];
//        tempDRBytes = convertPNToBytes(rate);
////        tempHdrBytes[DESTIP_OFFSET] = tempIPBytes;
//        System.arraycopy(tempDRBytes, 0, tempMsgBytes, DTRT_OFFSET, 
//                DTRT_LEN);   
//        return tempMsgBytes;
//
    }
    
    @Override
    public byte[] getHeader(byte[] messageBytes) {
        byte[] header = new byte[HEDR_LEN];
        System.arraycopy(messageBytes, 0, header, 0, HEDR_LEN);
        return header;
    }

    @Override
    public byte[] getData(byte[] messageBytes) 
    {
        int originalLength = Array.getLength(messageBytes);
        int dataLen = originalLength - HEDR_LEN;
        byte [] data = new byte[dataLen];
        System.arraycopy(messageBytes, HEDR_LEN, data, 0, dataLen);
        return data;
    }
    
    @Override
    public String getDestinationIP(byte[] headerBytes) {
        byte[] destIP = new byte[4];
        System.arraycopy(headerBytes, 0, destIP, 0, DESTIP_LEN );
        
        int byte1 = (destIP[0] & 0xff);
        int byte2 = (destIP[1] & 0xff);
        int byte3 = (destIP[2] & 0xff);
        int byte4 = (destIP[3] & 0xff);
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
    
    @Override
    public String getFileNameField(byte[] messageBytes)
    {
        int fileNameLen = messageBytes.length - HEDR_LEN - DTRT_LEN;
        byte fileNameBytes[] = Arrays.copyOfRange(messageBytes, FILE_OFFSET, 
                (FILE_OFFSET + fileNameLen));
        String fileNameStr = new String(fileNameBytes);
        return fileNameStr;
    }
    
    @Override
    public int getDataRateField(byte[] messageBytes) 
    {
        // Create and assign port bytes
        byte[] portBytes = Arrays.copyOfRange(messageBytes, DTRT_OFFSET, 
                (DTRT_OFFSET + DTRT_LEN));

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
    public int getChecksum(byte[] messageBytes) 
    {
        // Create and assign port bytes
        byte[] portBytes = Arrays.copyOfRange(messageBytes, CRC_OFFSET, 
                (CRC_OFFSET + CRC_LEN));

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
    public boolean isChecksumValid(byte[] messageBytes) 
    {
        CRC16 crc16 = new CRC16();
        byte[] tempMsgBytes = new byte[messageBytes.length];
        System.arraycopy(messageBytes, 0, tempMsgBytes, 0, messageBytes.length);
        int checksum = getChecksum(tempMsgBytes);
        byte emptyBytes[] = new byte[2];
        System.arraycopy(emptyBytes, 0, tempMsgBytes, CRC_OFFSET, 
                CRC_LEN);   
        crc16.update(tempMsgBytes, 0, tempMsgBytes.length);
        int newChecksum = (int) crc16.getValue();
        
        if(checksum == newChecksum)
        {
            return true;
        }
        else
        {
            return false;
        }
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
        return crcBytes; // placeholder
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
    public void sendMessage(byte[] messageBytes) throws Exception 
    {
        byte[] msgBytes = messageBytes;
        int msgLen = msgBytes.length;
        DatagramPacket sendPacket = 
                new DatagramPacket(msgBytes, msgLen, igINAddr, igPortNum);

        // Send a message
        deviceSocket.send(sendPacket);
        
        // Display the message
//        String sentMessage = new String(msgBytes, 0, sendPacket.getLength());
        System.out.println("Message sent is: ");	
        printMessage(msgBytes);
    }
    
    @Override
    public byte[] receiveMessage()
    {
        byte[] receivedData = new byte[msgSize];

        // Create a datagram
        DatagramPacket receivedDatagram = 
                new DatagramPacket(receivedData, receivedData.length);

        try 
        {
            // Receive a message
            deviceSocket.receive(receivedDatagram);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(HALP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        byte[] data = new byte[receivedDatagram.getLength()];
        System.arraycopy(receivedDatagram.getData(), 
                receivedDatagram.getOffset(), data, 0, 
                receivedDatagram.getLength());        
        currDtgm = receivedDatagram;
        
        // Display the message
//        String echodMessage = new String(data, 0, receivedDatagram.getLength());
        System.out.println("Message received is: ");
        printMessage(data);
        return data;
    }
    
    @Override
    public void closeConnection() 
    {
        deviceSocket.close();
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
        String flagInfo = "Destination Port Number: ";
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
    public void printDataRateField(byte[] messageBytes)
    {
        String flagInfo = "Data rate: ";
        int dataRateStr = getDataRateField(messageBytes);
        flagInfo += dataRateStr;
        flagInfo += " KBps";
        System.out.println(flagInfo);
    }
    
    public void printHeaderField(byte[] messageBytes)
    {
        String headerInfo = "Header: [";
        byte tempHedrBytes[] = Arrays.copyOfRange(messageBytes, HEDR_OFFSET, 
                (HEDR_OFFSET + HEDR_LEN));
        String headerStr = new String(tempHedrBytes, 0, Array.getLength(tempHedrBytes));
        headerInfo += headerStr + "]";
        System.out.println(headerInfo);
    }
    
    @Override
    public void printDataField(byte[] messageBytes)
    {
        String dataInfo = "Data: [";
        int dataLen = messageBytes.length - HEDR_LEN;
        byte tempDataBytes[] = Arrays.copyOfRange(messageBytes, DATA_OFFSET, 
                (DATA_OFFSET + dataLen));
        String dataStr = new String(tempDataBytes, 0, Array.getLength(tempDataBytes));
        dataInfo += dataStr + "]";
        System.out.println(dataInfo);
    }
    
    @Override
    public void printSequenceNumber(byte[] headerBytes){
        int printSequenceNum= (headerBytes[SEQ_OFFSET]<<24)&0xff000000|
       (headerBytes[SEQ_OFFSET + 1]<<16)&0x00ff0000|
       (headerBytes[SEQ_OFFSET + 2]<< 8)&0x0000ff00|
       (headerBytes[SEQ_OFFSET + 3])&0x000000ff;
        System.out.println("Sequence number: " + printSequenceNum);
    }
    
    @Override
     public void printAcknowledgmentNumber(byte[] headerBytes){
         int printAcknowledgment = (headerBytes[ACK_OFFSET]<<24)&0xff000000|
       (headerBytes[ACK_OFFSET + 1]<<16)&0x00ff0000|
       (headerBytes[ACK_OFFSET + 2]<< 8)&0x0000ff00|
       (headerBytes[ACK_OFFSET + 3])&0x000000ff;
         
        System.out.println("Acknowledgment number: " + printAcknowledgment);
    }
        
    @Override
    public void printChecksum(byte[] messageBytes)
    {
        int checksum = getChecksum(messageBytes);
        boolean isChkSumValid = isChecksumValid(messageBytes);
        System.out.println("Checksum value: " + checksum);
        System.out.println("Checksum valid: " + isChkSumValid);
    }
    
    @Override
    public void printMessage(byte[] messageBytes) 
    {
        int msgLen = Array.getLength(messageBytes);
        int dataLen = msgLen - HEDR_LEN;
        printHeaderField(messageBytes);
        printDataField(messageBytes);
        printDestIPField(messageBytes);
        printDestPNField(messageBytes);
        printChecksum(messageBytes);
        printSequenceNumber(messageBytes);
        printAcknowledgmentNumber(messageBytes);
        printFlagField(messageBytes);
        System.out.println("Message length: " + msgLen + " bytes");
        System.out.println("Length of data field: " + dataLen + " bytes");
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
    
    @Override
    public void addMaxTransmission(int maxTransmission)
    {
        maxTransmissions.add(maxTransmission);
    }
    
    @Override
    public int getMaxTransmission()
    {
        Collections.sort(maxTransmissions, new maxTransmissionsComparator());
        int maxTransmission = maxTransmissions.get(0);
        return maxTransmission;
    }
    
    @Override
    public boolean isMaxTransmissionsEmpty()
    {
        boolean isEmpty = maxTransmissions.isEmpty();
        return isEmpty;
    }
    
    @Override
    public long generateSequenceNumber(){
        Random ran = new Random();
        long sequence = Math.abs(ran.nextInt());//max int = 2147483647
        long seq = (sequence * 2)+1;
          
               
       return seq;
                     
   }
    
    @Override
   public long incrementSequence(long sequence){
       long seq = sequence++;
       if(seq == (2147483647*2) +2){
           seq = 0;
       }
       return seq;
   }
    
    @Override
   public byte[] setSequenceNumber(byte[] headerBytes, long number){ //takes incremented sequence number and places in a 4 byte array to be copied into header
//        headerBytes[SEQ_OFFSET] = (byte) ((number>>24) & 0xFF);                 
//        headerBytes[SEQ_OFFSET + 1] = (byte) ((number>>16) & 0xFF);
//        headerBytes[SEQ_OFFSET + 2] = (byte) ((number>>8) & 0xFF);
//        headerBytes[SEQ_OFFSET + 3] = (byte) (number & 0xFF);
//       
//       return headerBytes;
       
       String sequenceNumber = Long.toBinaryString(number);
       
       if(sequenceNumber.length() < 32)
        {
            int zeroCount = 16 - sequenceNumber.length();
            while (zeroCount > 0)
            {
                sequenceNumber = "0" + sequenceNumber;
                zeroCount--;
            }
        }
       
         //Parses binary string into two integers for two
        // separate bytes and assigns them
        long part1 = Long.parseLong(sequenceNumber.substring(0, 8), 2);
        long part2 = Long.parseLong(sequenceNumber.substring(8, 16),2);
        long part3 = Long.parseLong(sequenceNumber.substring(16, 24),2);
        long part4 = Long.parseLong(sequenceNumber.substring(24, 32),2);
        headerBytes[SEQ_OFFSET] = (byte) part1;               
        headerBytes[SEQ_OFFSET + 1] = (byte) part2;
        headerBytes[SEQ_OFFSET + 2] = (byte) part3;
        headerBytes[SEQ_OFFSET + 3] = (byte) part4;
   
        return headerBytes;
   }
    
    @Override
   public long getSequenceNumber(byte[] headerBytes){
        
//        long sequenceNum= (headerBytes[SEQ_OFFSET]<<24)&0xff000000|
//       (headerBytes[SEQ_OFFSET + 1]<<16)&0x00ff0000|
//       (headerBytes[SEQ_OFFSET + 2]<< 8)&0x0000ff00|
//       (headerBytes[SEQ_OFFSET + 3])&0x000000ff;
////        System.out.println("Sequence number: " + sequenceNum);
//        return sequenceNum;
            
        // Create and assign port bytes
        byte[] sequenceBytes = Arrays.copyOfRange(headerBytes, SEQ_OFFSET, 
                (SEQ_OFFSET + SEQ_LEN));

        // Assign port number bytes as ints
        long firstLong = sequenceBytes[0];
        long secondLong = sequenceBytes[1];
        long thirdLong = sequenceBytes[2];
        long fourthLong = sequenceBytes[3];

        // Convert byte ints to strings
        String firstLongStr = Long.toBinaryString((long) firstLong);
        String secondLongStr = Long.toBinaryString((long) secondLong);
        String thirdLongStr = Long.toBinaryString((long) thirdLong);
        String fourthLongStr = Long.toBinaryString((long) fourthLong);

        // Add 0's if length is less than 8 bits
        if(firstLongStr.length() < 8){
            long zeroCount = 8 - firstLongStr.length();
            while (zeroCount > 0){
                firstLongStr = "0" + firstLongStr;
                zeroCount--;
            }
        }
        if(secondLongStr.length() < 8){
            long zeroCount = 8 - secondLongStr.length();
            while (zeroCount > 0){
                secondLongStr = "0" + secondLongStr;
                zeroCount--;
            }
        }
        
        if(thirdLongStr.length() < 8){
            long zeroCount = 8 - thirdLongStr.length();
            while (zeroCount > 0){
                thirdLongStr = "0" + thirdLongStr;
                zeroCount--;
            }
        }
        if(fourthLongStr.length() < 8){
            long zeroCount = 8 - fourthLongStr.length();
            while (zeroCount > 0){
                fourthLongStr = "0" + fourthLongStr;
                zeroCount--;
            }
        }
        
         // Parse to 8 bit strings
        String firstLongBits = firstLongStr.substring(firstLongStr.length() - 8);
        String secondLongBits = secondLongStr.substring(secondLongStr.length() - 8);
        String thirdLongBits = thirdLongStr.substring(thirdLongStr.length() - 8);
        String fourthLongBits = fourthLongStr.substring(fourthLongStr.length() - 8);

        // Combines 8 bit strings into one complete string
        String completeLongStr = firstLongBits + "" + secondLongBits+ "" + thirdLongBits+ "" + fourthLongBits;

        // Converts complete String from unsigned integer to int
       // int completePN = Integer.parseUnsignedInt(completePNStr, 2);  //requres java 8 to run this coding
        Long sequenceNum= (Long) Long.parseLong(completeLongStr, 2);    //equivalent of above coding for non java8
        return sequenceNum;
   }
   
    @Override  //pass in the header bytes from received message, pass in header bytes of message that will be sent. Extracts
               // sequence number from recieved header and incruments it by one and then places inside acknowledgement feild 
               // in header for the next message to be sent
   public byte[] setAcknowledgmentNumber(byte[] headerBytes, long acknowledgment){
        headerBytes[ACK_OFFSET] = (byte) ((acknowledgment>>24) & 0xFF);                 
        headerBytes[ACK_OFFSET + 1] = (byte) ((acknowledgment>>16) & 0xFF);
        headerBytes[ACK_OFFSET + 2] = (byte) ((acknowledgment>>8) & 0xFF);
        headerBytes[ACK_OFFSET + 3] = (byte) (acknowledgment & 0xFF);
       
       return headerBytes;
   }
       
    @Override
  public long generateAcknowledgement(long sequence){
      long acknowledgment;
       if(sequence == 4294967296L){
           acknowledgment = 0;
       }
       else{
           acknowledgment = sequence++; 
       }
       return acknowledgment;
  } 
        
   
    @Override
   public long getAcknowledgmentNumber(byte[] headerBytes){
       
       long acknowledgment = (headerBytes[ACK_OFFSET]<<24)&0xff000000|
       (headerBytes[ACK_OFFSET + 1]<<16)&0x00ff0000|
       (headerBytes[ACK_OFFSET + 2]<< 8)&0x0000ff00|
       (headerBytes[ACK_OFFSET + 3])&0x000000ff;
       
       return acknowledgment;
   }
}
