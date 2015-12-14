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
    
    /**
     * This is a client method that requests the user to the direction of the transfer, upload or download
     * 
     */
    public void inputTransferDirection();
    
    /**
     * This is a client method that requests the user to enter the name of the file to be transfered
     * 
     */
    public void inputFileName();
    
    /**
     * This is a client method that requests the user to enter the desired data rate of the transfer
     * 
     */
    public void inputDataRate();
    
    /**
     * This is a client method used to set which direction the transfer is 
     * 
     * @param trfrDir if set to true this will represent that it is an upload 
     */
    public void setTransferDirection(boolean trfrDir);
    
    /**
     * This is a client method that is used to set the name of the file that is to be transfered
     * 
     * @param fileNameStr string that is what the user entered for the name of th file to be transfered
     */
    public void setFileName(String fileNameStr);
    
    /**
     * This is a client method that is sued to set the data rate of the file transfer
     * 
     * @param rate this is the data rate that the user entered
     */
    public void setDataRate(int rate);
    
    /**
     * This is a client method that is used to set the file name field in a byte array
     * 
     * @param messageBytes byte array that will be passed in and the file name field is set into
     * @param fileNameStr string of the file name
     * @return  byte array that contains the file name of the file that is to be transfered
     */
    public byte[] setFileNameField(byte[] messageBytes, String fileNameStr);
    
    /**
     * This is a client method used to set the destinations ip address into the header of the segment
     * that is going to be sent
     * 
     * @param headerBytes byte array that is for the header information of the segment being sent
     * @param destIP the destination ip of where the segment is to be sent to
     * @return byte array that contains the destination ip in it 
     */
    public byte[] setDestIP(byte[] headerBytes, String destIP);
    
    /**
     * This is a client method used to set the destination port number into the header of the segment
     * that is going to be sent
     * 
     * @param headerBytes byte array that is for the header information of the segment is to be sent to
     * @param portNum the port number that is for the destination where the segment is to be sent
     * @return byte array that contains the destination port number in it
     */
    public byte[] setDestPN(byte[] headerBytes, int portNum);
       
    /**
     * This is a client method used to convert a string ip address into a bytes representation
     * 
     * @param ipAddr ip address in string form that is to be converted to bytes
     * @return byte array that contains the byte form of the passed in string ip address
     */
    public byte[] convertIPToBytes(String ipAddr);
    
    /**
     * This is a client method used to convert an integer port number into bytes
     * 
     * @param portNum integer that is the port number for a device
     * @return byte array that contains the byte form of the passed in integer port number
     */
    public byte[] convertPNToBytes(int portNum);
    
    /**
     * This is a client method used to initiate a connection from the client to the server
     * will send a request to the internet gateway to create a connection to the server 
     * 
     */
    public void initiateConnection();
    
    /**
     * This is a client method that defines a device to act as a server and to send the data
     * of the specified file to the other device in a connection object
     * 
     */
    public void runAsServer();
    
    /**
     * This is a client method used to process a connection request from another device
     * and establish whether the device should act as a receiver of a file or a sender of 
     * a file
     * 
     * @throws java.io.FileNotFoundException
     */
    public void runAsSender() throws FileNotFoundException, IOException, Exception;

    /**
     * This is a client method that defines a device to act as a receiver and to write the data
     * of the specified file that is coming from the other device in a connection object
     * 
     * @param firstMsg byte array passed to the receiver, need to determine if its the first message or
     * not. If not first message then wait for incoming messages from sender.
     * @throws java.io.FileNotFoundException
     */
    public void runAsReceiver(byte[] firstMsg) throws FileNotFoundException, IOException, Exception;
}