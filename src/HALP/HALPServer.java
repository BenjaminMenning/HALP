package HALP;

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

import java.io.*;
import java.net.*;
import java.lang.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class serves as a server in the testing of connections. It was 
 * originally going to be its own separate class with its own methods and 
 * functions but is no longer being implemented that way. Instead, the 
 * Server is a HALPClient object that runs as a server.
 * 
 * @author Ben
 */
public class HALPServer
{
    private static final int SERVER_PORT = 43124;
    
    public static void main(String args[]) throws Exception
    {
        Scanner console = new Scanner(System.in);

        // For new server implementation
        HALPClient halpServer = new HALPClient(SERVER_PORT, SERVER_PORT);
        
        // For testing
//        halpServer.setTrace(true);
        
        // For user input
        halpServer.inputSrvPortNum();
        halpServer.inputTrace();
        
        halpServer.runAsServer();
    }
}