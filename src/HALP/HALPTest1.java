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
       
       // For testing SYN flag
       halpIG.assembleMessage();
       byte testMessage[] = halpIG.getMessage();
       halpIG.setSYNFlag(testMessage, true);
       halpIG.assembleMessage();
       testMessage = halpIG.getMessage();
       System.out.println(halpIG.isSYNFlagSet(testMessage));
       
       halpIG.setSYNFlag(testMessage, false);
       halpIG.assembleMessage();
       testMessage = halpIG.getMessage();
       System.out.println(halpIG.isSYNFlagSet(testMessage));
       
       halpIG.setSYNFlag(testMessage, false);
       halpIG.assembleMessage();
       testMessage = halpIG.getMessage();
       System.out.println(halpIG.isSYNFlagSet(testMessage));
       
       halpIG.setSYNFlag(testMessage, true);
       halpIG.assembleMessage();
       testMessage = halpIG.getMessage();
       System.out.println(halpIG.isSYNFlagSet(testMessage));

       halpIG.setSYNFlag(testMessage, true);
       halpIG.assembleMessage();
       testMessage = halpIG.getMessage();
       System.out.println(halpIG.isSYNFlagSet(testMessage));
       
       halpIG.setSYNFlag(testMessage, false);
       halpIG.assembleMessage();
       testMessage = halpIG.getMessage();
       System.out.println(halpIG.isSYNFlagSet(testMessage));
       
       halpIG.setSYNFlag(testMessage, false);
       halpIG.assembleMessage();
       testMessage = halpIG.getMessage();
       System.out.println(halpIG.isSYNFlagSet(testMessage));
       
       // Testing ACK flag
       System.out.println("Testing ACK flag: ");
       halpIG.setACKFlag(testMessage, true);
       halpIG.assembleMessage();
       testMessage = halpIG.getMessage();
       System.out.println(halpIG.isACKFlagSet(testMessage));
       
       halpIG.setACKFlag(testMessage, false);
       halpIG.assembleMessage();
       testMessage = halpIG.getMessage();
       System.out.println(halpIG.isACKFlagSet(testMessage));
       
       halpIG.setACKFlag(testMessage, false);
       halpIG.assembleMessage();
       testMessage = halpIG.getMessage();
       System.out.println(halpIG.isACKFlagSet(testMessage));
       
       halpIG.setACKFlag(testMessage, true);
       halpIG.assembleMessage();
       testMessage = halpIG.getMessage();
       System.out.println(halpIG.isACKFlagSet(testMessage));

       halpIG.setACKFlag(testMessage, true);
       halpIG.assembleMessage();
       testMessage = halpIG.getMessage();
       System.out.println(halpIG.isACKFlagSet(testMessage));
       
       halpIG.setACKFlag(testMessage, false);
       halpIG.assembleMessage();
       testMessage = halpIG.getMessage();
       System.out.println(halpIG.isACKFlagSet(testMessage));
       
       halpIG.setACKFlag(testMessage, false);
       halpIG.assembleMessage();
       testMessage = halpIG.getMessage();
       System.out.println(halpIG.isACKFlagSet(testMessage));
   }
}