/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HALP;

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

        byte[] testHeader = new byte[20];
        byte[] testData = new byte[100];
        
        
        
//        // testing for creation of random number to start sequence numbers-STILL WORKING ON
//        Random ran = new Random();
//        byte[] testSequ = new byte[4];
//        
//        
//        
////        int value = .... ;
////        byte b = new byte[4];
////        for (int i=0; i < 4; ++i)
////         {
////          b[i] = (byte) (value & 0xFF);
////             value = value >> 8;
////          }
//        
//        long sequence = 4294967296; //Math.abs(ran.nextInt());
//        System.out.println("sequence = " + sequence);
//        
//               for(int j=0; j<4; j++){
//                   testSequ[j] = (byte) (sequence & 0xff);
//                   System.out.println(" h " + sequence);
//                   sequence = sequence <<8;
//               }
//        
//               
//        
//        int sequence1= (testSequ[0]<<24)&0xff000000|
//       (testSequ[1]<<16)&0x00ff0000|
//       (testSequ[2]<< 8)&0x0000ff00|
//       (testSequ[3])&0x000000ff;
//        System.out.println(" sequence number: " + sequence1);
//        
        
//        
//        InetAddress testINAddr;
//        testINAddr = InetAddress.getByName(testIPAddr);
//        System.out.println(testINAddr.getCanonicalHostName());
//        
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