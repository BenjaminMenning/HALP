/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HALP;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bmenning13
 */
public class HALPTest1 
{
   public static void main(String[] args) throws SocketException, UnknownHostException, IOException
   {
        HALPClient halpClient = new HALPClient();
        HALPIG halpIG = new HALPIG();
        CRC16 crc = new CRC16();
        CRC16 crc2 = new CRC16();

        byte[] testHeader = new byte[20];
        byte[] testData = new byte[100];
        byte[] testMsg = null;
        
        PrintWriter IGLog =null;

        
        //to test the sequence generating code
//        Random ran = new Random();
//        long sequence = Math.abs(ran.nextInt());//max int = 2147483647
//        System.out.println(sequence);
//        long seq = ((sequence * 2)+1);
//        System.out.println(seq);
//          System.out.println("seq = " + seq);
//          System.out.println("seq = 4294967295");
      
        
          
       Connection tempCon1 = new Connection("172.1.2.3", 1234, "172.2.3.6",2345,5);
       Connection tempCon2 = new Connection("172.1.2.4", 5678, "172.2.3.7",6789,7);
       Connection tempCon3 = new Connection("172.1.2.5", 9123, "172.2.3.8",123,9);
       
       
       ConnectionTable tempTable = new ConnectionTable();
       tempTable.addConnection(tempCon1);
       tempTable.addConnection(tempCon2);
       tempTable.addConnection(tempCon3);
       
       System.out.println("Printing out to test conToString method.... \n");
       System.out.println(tempCon1.conToString());
       
       System.out.println("Printing connection table out.... \n");
       System.out.println(tempTable.printTable());
       
       System.out.println("should find: ");
       Connection conTest = new Connection();
       conTest = tempTable.getCorrespondingConnection("172.1.2.5", 9123);
       System.out.println(conTest.tempToString() + "\n");
       
        System.out.println("should find: ");
       Connection test2 = new Connection();
       test2 = tempTable.getCorrespondingConnection("172.2.3.7", 6789);
       System.out.println(test2.tempToString() + "\n");
       
       System.out.println("wont find: ");
       Connection test1 = new Connection();
       test1 = tempTable.getCorrespondingConnection("172.1.2.3", 5678);
       System.out.println(test1.tempToString() + "\n");
       
       System.out.println("Printing connection table out after 2nd connection was removed.... \n");
       tempTable.removeConnection(tempCon2);
       System.out.println(tempTable.printTable() + "\n");
       
       //test for searching if connection exists
       boolean found = tempTable.searchTable("172.2.3.6",2345);
       System.out.println("Found connection? " + found +"\n");
       
       found = tempTable.searchTable("172.1.2.3", 5678);
       System.out.println("Found connection? " + found + "\n");
       
//        
//        InetAddress testINAddr;
//        testINAddr = InetAddress.getByName(testIPAddr);
//        System.out.println(testINAddr.getCanonicalHostName());
        
        String testIPAddr = "127.0.0.1";
        System.out.println("\n\nTesting Destination IP...");
        testHeader = halpClient.setDestIP(testHeader, testIPAddr);
        halpIG.printMessage(testHeader);  
        System.out.println(halpIG.getDestinationIP(testHeader));
        
        int testPortNum = 55555;
        System.out.println("\n\nTesting Destination Port Number...");
        testHeader = halpClient.setDestPN(testHeader, testPortNum);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.getDestinationPort(testHeader));
        
        System.out.println("\n\nTesting Flags...");
        testHeader = halpIG.setFINFlag(testHeader, true);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isFINFlagSet(testHeader));
        
        testHeader = halpIG.setSYNFlag(testHeader, true);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isSYNFlagSet(testHeader));

        testHeader = halpIG.setACKFlag(testHeader, true);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isACKFlagSet(testHeader));
        
        testHeader = halpIG.setDRTFlag(testHeader, true);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isDRTFlagSet(testHeader));
        
        long randSeqNum = halpClient.generateSequenceNumber();
        System.out.println("\n THIS IS GENERATED SEQUENCE NUMBER: " + randSeqNum + "\n");
        testHeader = halpClient.setSequenceNumber(testHeader, randSeqNum);
        halpClient.printMessage(testHeader);
        System.out.println("\n THIS IS GENERATED SEQUENCE NUMBER AFTER PULLING FROM ARRAY: " +halpClient.getSequenceNumber(testHeader));
        
        long ackNum = halpClient.getSequenceNumber(testHeader);
        System.out.println("sequence number: " + ackNum+"\n");
        System.out.println("Here is with using the new setAcknowledgment() and  the generateAcknowledgment() methods \n");
        testHeader = halpClient.setAcknowledgmentNumber(testHeader, halpClient.generateAcknowledgement(halpClient.getSequenceNumber(testHeader)));
        halpClient.printMessage(testHeader);
        System.out.println("USING NEW CODING: " + halpClient.getAcknowledgmentNumber(testHeader));
        
        byte[] testMessage = halpClient.assembleMessage(testHeader, testData);
        
        String testFileName = "TestFile.txt";
        System.out.println("\n\nTesting File Name...");
        testMessage = halpClient.setFileNameField(testMessage, testFileName);
        halpClient.printFileNameField(testMessage);
        System.out.println(halpIG.getFileNameField(testMessage));
        
        int testDataRate = 1024;
        System.out.println("\n\nTesting Data Rate...");
        testMessage = halpClient.setDataRateField(testMessage, testDataRate);
        halpClient.printDataRateField(testMessage);
        System.out.println(halpIG.getDataRateField(testMessage));
        
        testMessage = halpClient.assembleMessage(testMessage, testData);
        halpClient.printMessage(testMessage);
        testData = halpClient.getData(testMessage);
        String dataStr = new String(testData, 0, Array.getLength(testData));
        System.out.println(dataStr);
        
        crc.update(testMessage, 0, testMessage.length);
        System.out.println(crc.getValue());
        System.out.println("\n\nTesting Checksum...");
        testMessage = halpClient.setChecksum(testMessage);
        boolean test = halpClient.isChecksumValid(testMessage);
        System.out.println(halpClient.getChecksum(testMessage));
        halpClient.printMessage(testMessage);
        System.out.println(halpClient.getChecksum(testMessage));
        crc2.update(testMessage, 0, testMessage.length);
        System.out.println(crc2.getValue());
//        halpClient.printMessage(testMessage);
        
        // Error testing
        System.out.println("\n\nTesting Error Rate...");
        double errRate = .9;
        double crptRate = .9;
        boolean isError = false;
        boolean isCorrupt = false;
        halpIG.setErrorRate(errRate);
        halpIG.setCorruptRate(crptRate);
        
        isError = halpIG.errorGenerator();
        System.out.println("Is error: " + isError);
        if(isError)
        {
            isCorrupt = halpIG.isCorrupt();
            System.out.println("Is corrupt: " + isCorrupt);
            if(isCorrupt)
            {
                testMessage = halpIG.generateByteError(testMessage);
            }
            else
            {
                // do nothing
            }
        }
        
        crc2.update(testMessage, 0, testMessage.length);
        System.out.println(crc2.getValue());
        System.out.println("\n\nTesting checksum again...");
//        testMessage = halpClient.setChecksum(testMessage);
        test = halpClient.isChecksumValid(testMessage);
        System.out.println(halpClient.getChecksum(testMessage));
        halpClient.printMessage(testMessage);
        System.out.println(halpClient.getChecksum(testMessage));
        crc2.update(testMessage, 0, testMessage.length);
        System.out.println(crc2.getValue());

//        int i = 0;
//        while(i < 1000)
//        {
//            isError = halpIG.errorGenerator();
//            isCorrupt = halpIG.isCorrupt();
//            System.out.println("Is error: " + isError);
//            System.out.println("Is corrupt: " + isCorrupt);
//        }

        
        
//        byte blankByte = 0;
//        testMessage[6] = blankByte;
//        testMessage[7] = blankByte;
//        byte errorByte = halpIG.generateByteError(blankByte);
//        testMessage[0] = errorByte;
//        crc2.update(testMessage, 0, testMessage.length);
//        System.out.println(crc2.getValue());
        
        
//        //testing of messages for logging
//        System.out.println(halpClient.messageLog(testMessage));
//        
//        System.out.println(halpClient.resendLog(testMessage));
//        
//        System.out.println(halpClient.errorGeneratedLog(testMessage));
//        
//        System.out.println(halpClient.errorDetectedLog(testMessage));
        
        //Code for testing log writing and appending needed for the IG
//          // Initialize file location and set file name for logging
//        String IGStr = System.getProperty("user.home") + "/Desktop/";
//        String IGName = "IG_Log.txt";
//        File IGFile = new File(IGStr + IGName);
//        try {
//            IGLog = new PrintWriter(new FileWriter(IGFile, true)); //new PrintWriter(IGFile);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(HALPIG.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        IGLog.println("Internet Gateways' Logging: \n");
//        IGLog.println("howdy" + "\n");
//        
//        IGLog.close();
//        
//       try {
//            IGLog = new PrintWriter(new FileWriter(IGFile, true)); //new PrintWriter(IGFile);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(HALPIG.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        IGLog.append("after file close");
//        IGLog.close();
//        
               
   }  
   
}
