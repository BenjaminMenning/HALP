/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HALP;

import java.net.SocketException;

/**
 *
 * @author bmenning13
 */
public class HALPTest1 
{
   public static void main(String[] args) throws SocketException
   {
        HALPClient halpClient = new HALPClient();
        HALPIG halpIG = new HALPIG();

        byte[] testHeader = new byte[20];
        
        
        System.out.println("\n\nTesting FIN...");
        testHeader = halpIG.setFINFlag(testHeader, true);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isFINFlagSet(testHeader));

        testHeader = halpIG.setFINFlag(testHeader, false);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isFINFlagSet(testHeader));

        testHeader = halpIG.setFINFlag(testHeader, false);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isFINFlagSet(testHeader));

        testHeader = halpIG.setFINFlag(testHeader, true);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isFINFlagSet(testHeader));

        testHeader = halpIG.setFINFlag(testHeader, true);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isFINFlagSet(testHeader));

        testHeader = halpIG.setFINFlag(testHeader, false);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isFINFlagSet(testHeader));

        testHeader = halpIG.setFINFlag(testHeader, false);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isFINFlagSet(testHeader));

        
        System.out.println("\n\nTesting SYN...");
        testHeader = halpIG.setSYNFlag(testHeader, true);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isSYNFlagSet(testHeader));

        testHeader = halpIG.setSYNFlag(testHeader, false);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isSYNFlagSet(testHeader));

        testHeader = halpIG.setSYNFlag(testHeader, false);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isSYNFlagSet(testHeader));

        testHeader = halpIG.setSYNFlag(testHeader, true);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isSYNFlagSet(testHeader));

        testHeader = halpIG.setSYNFlag(testHeader, true);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isSYNFlagSet(testHeader));

        testHeader = halpIG.setSYNFlag(testHeader, false);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isSYNFlagSet(testHeader));

        testHeader = halpIG.setSYNFlag(testHeader, false);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isSYNFlagSet(testHeader));
        
        
        System.out.println("\n\nTesting ACK...");
        testHeader = halpIG.setACKFlag(testHeader, true);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isACKFlagSet(testHeader));

        testHeader = halpIG.setACKFlag(testHeader, false);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isACKFlagSet(testHeader));

        testHeader = halpIG.setACKFlag(testHeader, false);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isACKFlagSet(testHeader));

        testHeader = halpIG.setACKFlag(testHeader, true);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isACKFlagSet(testHeader));

        testHeader = halpIG.setACKFlag(testHeader, true);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isACKFlagSet(testHeader));

        testHeader = halpIG.setACKFlag(testHeader, false);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isACKFlagSet(testHeader));

        testHeader = halpIG.setACKFlag(testHeader, false);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isACKFlagSet(testHeader));
        
        
        System.out.println("\n\nTesting DRT...");
        testHeader = halpIG.setDRTFlag(testHeader, true);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isDRTFlagSet(testHeader));

        testHeader = halpIG.setDRTFlag(testHeader, false);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isDRTFlagSet(testHeader));

        testHeader = halpIG.setDRTFlag(testHeader, false);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isDRTFlagSet(testHeader));

        testHeader = halpIG.setDRTFlag(testHeader, true);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isDRTFlagSet(testHeader));

        testHeader = halpIG.setDRTFlag(testHeader, true);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isDRTFlagSet(testHeader));

        testHeader = halpIG.setDRTFlag(testHeader, false);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isDRTFlagSet(testHeader));

        testHeader = halpIG.setDRTFlag(testHeader, false);
        halpIG.printMessage(testHeader);
        System.out.println(halpIG.isDRTFlagSet(testHeader));
   }
}