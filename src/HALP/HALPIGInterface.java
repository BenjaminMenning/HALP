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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/** 
 * This interface provides methods and functionality to be implemented by the 
 * HALPIG class. It describes methods for generating errors and setting the 
 * max data rate.
 * 
 * @author Benjamin Menning, John Blacketer
 * @version 12/15/2015
*/
public interface HALPIGInterface extends HALPInterface
{
    
//    public void convertBytesToDestIP();
//
//    public void convertBytesToDestPN();
    
    /**
     * This method is for the user to specify the max data rate for the internet gateway
     * 
     */
    public void inputMaxDataRate();
    
    /**
     * This method is for the user to specify error data rate for the internet gateway
     * 
     */
    public void inputErrorRate();
    
    /**
     * This method is for the user to specify the corruption rate for the internet gateway
     * 
     */
    public void inputCorruptRate();
    
    /**
     * This method prompts the user to enter in the port number for the 
     * connection.
     * 
     */
    public void inputPortNum();
    
    /**
     * This method is used for setting the max data rate for the internet gateway 
     * 
     * @param dataRate integer for the max data rate of the internet gateway
     */
    public void setMaxDataRate(int dataRate);
    
    /**
     * This method generates errors based off of the error rate entered by the
     * user.
     * 
     * @return boolean  returns true if error, false if otherwise
     */
    public boolean errorGenerator();
    
    /**
     * This method returns what type of error is to occur if there is to be an error
     * 
     * @return true for corrupted data, false for lost packet
     */
    public boolean isCorrupt();
    
    /**
     * This method returns a random index for a byte in the frame for an 
     * error to be placed.
     * 
     * @param index the index of the byte that is to be modified to represent an error
     * @return int  returns an int containing the byte index for the error
     */
    public int randomIndex(int index);

    /**
     * This error generates an error within a byte by flipping a random bit 
     * inside of the byte.
     * 
     * @param messageBytes byte array that is the bytes of the message being sent
     * @return byte returns a byte containing the new error.
     */
    public byte[] generateByteError(byte[] messageBytes);
    
    /**
     * This method generates the number of errors based on the chance of the
     * errors occurring.
     * 
     * @return int  returns an int containing the number of errors
     */
    public int errorNumber();
    
    /**
     * This method assigns the error rate for the data transmission.
     * 
     * @param rate  the double to be assigned as the error rate
     */
    public void setErrorRate(double rate);
    
    /**
     * This method retrieves the error rate for the data transmission
     * 
     * @return double  returns the double assigned as the error rate
     */
    public double getErrorRate();
    
    /**
     * This method assigns the error rate for the data transmission.
     * 
     * @param rate  the double to be assigned as the error rate
     */
    public void setCorruptRate(double rate);
    
    /**
     * This method retrieves the error rate for the data transmission
     * 
     * @return double  returns the double assigned as the error rate
     */
    public double getCorruptRate();
}