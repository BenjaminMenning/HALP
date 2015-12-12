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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;

/** 
 * This interface provides methods and functionality to be implemented by the 
 * HALPClient class. It is not only for a client, but can be run as either a
 * client or a server. It describes methods for inputting necessary client 
 * information like file name, data rate, as well as methods for running the
 * device as a client or a server.
 * 
 * @author Benjamin Menning, John Blacketer
 * @version 12/15/2015
*/
public interface HALPClientInterface extends HALPInterface
{
   
    /**
     * This is a client method that requests the user to enter the IG's IP
     * address.
     * 
     */
    public void inputIGIP();
    
    /**
     * This is a client method that requests the user to enter the server's IP
     * address.
     * 
     */
    public void inputServIP();
    
    public void inputTransferDirection();
    
    public void inputFileName();
    
    public void inputDataRate();
    
    public void setTransferDirection(boolean trfrDir);
    
    public void setFileName(String fileNameStr);
    
    public void setDataRate(int rate);
    
    public byte[] setFileNameField(byte[] messageBytes, String fileNameStr);
    
    public byte[] setDestIP(byte[] headerBytes, String destIP);
    
    public byte[] setDestPN(byte[] headerBytes, int portNum);
        
    public byte[] convertIPToBytes(String ipAddr);
    
    public byte[] convertPNToBytes(int portNum);
    
    public void initiateConnection();
    
    public void runAsServer();
    
    public void runAsSender() throws FileNotFoundException, IOException, Exception;

    public void runAsReceiver(byte[] firstMsg) throws FileNotFoundException, IOException, Exception;
}