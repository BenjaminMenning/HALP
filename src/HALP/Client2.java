/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HALP;

import java.util.Scanner;

/**
 *
 * @author Ben
 */
public class Client2 
{
    private static final int IG_PORT = 43000;
    private static final int SERVER_PORT = 49000;

    public static void main (String args[]) throws Exception 
    {	
        Scanner console = new Scanner(System.in);
        HALPClient halpClient = new HALPClient(IG_PORT, SERVER_PORT);
        
        // Hard coded IP addresses for testing
        String homeTestIP = "192.168.0."; // for testing at home
//        String testIGIP = homeTestIP + "110"; // for manual entry
        String testIGIP = halpClient.getLocalIP(); // for automatic entry
        String testServIP = homeTestIP + "111";
        String testFile1 = "alice.txt";
        String testFile2 = "mission0.txt";
        String testFile3 = "mission1.txt";
        String testFile4 = "mission2.txt";
        String testFileName = testFile4; // swap out test files here
        boolean testIsUpload = true;
        int testDataRate = 16;
        
        // Hard coded values for testing
        halpClient.setIGIP(testIGIP);
        halpClient.setServerIP(testServIP);
        halpClient.setFileName(testFileName);
        halpClient.setTransferDirection(testIsUpload);
        halpClient.setDataRate(testDataRate);
        
        
        // For user input
//        halpClient.inputIGIP();
//        halpClient.inputServIP();
//        halpClient.inputFileName();
//        halpClient.inputTransferDirection();
//        halpClient.inputDataRate();
                
        halpClient.initiateConnection();
    }
}