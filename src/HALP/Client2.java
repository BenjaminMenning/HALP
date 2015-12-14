/** 
 * Author:          Benjamin Menning, John Blacketer
 * 
 * Date:            12/15/2015 
 *                
 * Course:          CS 413 - Advanced Networking
 * 
 * Assignment:      Final Project - HALP Protocol
 * 
 * Description:     This program is a program that performs a simple file 
 *                  transfer utilizing our own protocol, HALP. It includes 
 *                  three devices: a client, an internet gateway (IG), and a
 *                  server. The client initiates a file download or upload from
 *                  or to the server, and the file transfer process begins, 
 *                  while the internet gateway passes messages between them. 
 *                  It follows our protocol to provide reliability for the data
 *                  transfer process. All three devices follow the protocol to
 *                  manipulate the header data fields and can print out 
 *                  information to trace and log the connection process. Our 
 *                  program / protocol utilizes use of positive acknowledgment,
 *                  retransmission, timeout, and sequence numbers to provide 
 *                  reliability. 
 * 
 */
package HALP;

import java.util.Scanner;

/**
 * This class serves as a second client in the testing of concurrent 
 * connections.
 * 
 * @author Ben
 */
public class Client2 
{
    private static final int IG_PORT = 43001;
    private static final int SERVER_PORT = 49000;

    public static void main (String args[]) throws Exception 
    {	
        Scanner console = new Scanner(System.in);
        HALPClient halpClient = new HALPClient(IG_PORT, SERVER_PORT);
        
        // Hard coded IP addresses for testing
        String homeTestIP = "192.168.0."; // for testing at home
//        String testIGIP = homeTestIP + "110"; // for manual entry
        String testIGIP = halpClient.getLocalIP(); // for automatic entry
        String testServIP = homeTestIP + "113";
        String testFile1 = "alice.txt";
        String testFile2 = "mission0.txt";
        String testFile3 = "mission1.txt";
        String testFile4 = "mission2.txt";
        String testFile5 = "allan.txt";
        String testFile6 = "ebZqtsI.gif";
        String testFile7 = "4g9ttdk.gif";
        String testFile8 = "cCPh3fg.jpg";
        String testFileName = testFile3; // swap out test files here
        boolean testIsUpload = true;
        boolean testIsTrace = true;
        int testDataRate = 8;
        
        // Hard coded input for testing
        halpClient.setIGIP(testIGIP);
        halpClient.setServerIP(testServIP);
        halpClient.setTransferDirection(testIsUpload);
        halpClient.setFileName(testFileName);
        halpClient.setDataRate(testDataRate);
        halpClient.setTrace(true);
        
        
        // For user input
//        halpClient.inputIGIP();
//        halpClient.inputServIP();
//        halpClient.inputFileName();
//        halpClient.inputTransferDirection();
//        halpClient.inputDataRate();
//        halpClient.inputTrace();
                
        halpClient.initiateConnection();
    }
}