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
 * This class serves as a second server in the testing of concurrent 
 * connections.
 * 
 * @author Ben
 */
public class Server2 
{
    private static final int SERVER_PORT = 49000;
    
    public static void main(String args[]) throws Exception
    {
        Scanner console = new Scanner(System.in);
        
        // For old server implmentation, don't delete
//        HALPServer halpServer = new HALPServer(SERVER_PORT);
//        halpServer.startDevice();

        // For new server implementation
        HALPClient halpServer = new HALPClient(SERVER_PORT, SERVER_PORT);
        
        // For testing
        halpServer.setTrace(true);
        
        // For user input
//        halpServer.inputTrace();
        
        halpServer.runAsServer();
    }
}