/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HALP;

import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

/**
 *
 * @author bmenning13
 */
public class HALPTest1 
{
   public static void main(String[] args) throws SocketException, UnknownHostException
   {
        HALPClient halpClient = new HALPClient();
        HALPIG halpIG = new HALPIG();
        CRC16 crc = new CRC16();
        CRC16 crc2 = new CRC16();

        byte[] testHeader = new byte[20];
        byte[] testData = new byte[100];
        byte[] testMsg = null;
        
        
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
        
        testMessage = halpClient.assembleMessage(testMessage, testData);
        halpClient.printMessage(testMessage);
        testData = halpClient.getData(testMessage);
        String dataStr = new String(testData, 0, Array.getLength(testData));
        System.out.println(dataStr);
        
        crc.update(testMessage, 6, 2);
        System.out.println(crc.getValue());
        testMessage[0] = halpIG.generateByteError(testMessage[0]);
        crc2.update(testMessage, 6, 2);
        System.out.println(crc2.getValue());

        
//        
//        System.out.println("\n\nTesting FIN...");
//        testHeader = halpIG.setFINFlag(testHeader, true);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isFINFlagSet(testHeader));
//
//        testHeader = halpIG.setFINFlag(testHeader, false);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isFINFlagSet(testHeader));
//
//        testHeader = halpIG.setFINFlag(testHeader, false);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isFINFlagSet(testHeader));
//
//        testHeader = halpIG.setFINFlag(testHeader, true);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isFINFlagSet(testHeader));
//
//        testHeader = halpIG.setFINFlag(testHeader, true);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isFINFlagSet(testHeader));
//
//        testHeader = halpIG.setFINFlag(testHeader, false);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isFINFlagSet(testHeader));
//
//        testHeader = halpIG.setFINFlag(testHeader, false);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isFINFlagSet(testHeader));
//
//        
//        System.out.println("\n\nTesting SYN...");
//        testHeader = halpIG.setSYNFlag(testHeader, true);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isSYNFlagSet(testHeader));
//
//        testHeader = halpIG.setSYNFlag(testHeader, false);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isSYNFlagSet(testHeader));
//
//        testHeader = halpIG.setSYNFlag(testHeader, false);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isSYNFlagSet(testHeader));
//
//        testHeader = halpIG.setSYNFlag(testHeader, true);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isSYNFlagSet(testHeader));
//
//        testHeader = halpIG.setSYNFlag(testHeader, true);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isSYNFlagSet(testHeader));
//
//        testHeader = halpIG.setSYNFlag(testHeader, false);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isSYNFlagSet(testHeader));
//
//        testHeader = halpIG.setSYNFlag(testHeader, false);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isSYNFlagSet(testHeader));
//        
//        
//        System.out.println("\n\nTesting ACK...");
//        testHeader = halpIG.setACKFlag(testHeader, true);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isACKFlagSet(testHeader));
//
//        testHeader = halpIG.setACKFlag(testHeader, false);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isACKFlagSet(testHeader));
//
//        testHeader = halpIG.setACKFlag(testHeader, false);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isACKFlagSet(testHeader));
//
//        testHeader = halpIG.setACKFlag(testHeader, true);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isACKFlagSet(testHeader));
//
//        testHeader = halpIG.setACKFlag(testHeader, true);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isACKFlagSet(testHeader));
//
//        testHeader = halpIG.setACKFlag(testHeader, false);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isACKFlagSet(testHeader));
//
//        testHeader = halpIG.setACKFlag(testHeader, false);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isACKFlagSet(testHeader));
//        
//        
//        System.out.println("\n\nTesting DRT...");
//        testHeader = halpIG.setDRTFlag(testHeader, true);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isDRTFlagSet(testHeader));
//
//        testHeader = halpIG.setDRTFlag(testHeader, false);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isDRTFlagSet(testHeader));
//
//        testHeader = halpIG.setDRTFlag(testHeader, false);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isDRTFlagSet(testHeader));
//
//        testHeader = halpIG.setDRTFlag(testHeader, true);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isDRTFlagSet(testHeader));
//
//        testHeader = halpIG.setDRTFlag(testHeader, true);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isDRTFlagSet(testHeader));
//
//        testHeader = halpIG.setDRTFlag(testHeader, false);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isDRTFlagSet(testHeader));
//
//        testHeader = halpIG.setDRTFlag(testHeader, false);
//        halpIG.printMessage(testHeader);
//        System.out.println(halpIG.isDRTFlagSet(testHeader));
   }
}