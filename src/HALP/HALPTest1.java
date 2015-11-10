/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HALP;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

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
        
//        InetAddress testINAddr;
//        testINAddr = InetAddress.getByName(testIPAddr);
//        System.out.println(testINAddr.getCanonicalHostName());
        
        String testIPAddr = "127.0.0.1";
        System.out.println("\n\nTesting Destination IP...");
        testHeader = halpClient.setDestIP(testHeader, testIPAddr);
        halpIG.printMessage(testHeader);  
        
        int testPortNum = 55555;
        System.out.println("\n\nTesting Destination Port Number...");
        testHeader = halpClient.setDestPN(testHeader, testPortNum);
        halpIG.printMessage(testHeader);
        
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