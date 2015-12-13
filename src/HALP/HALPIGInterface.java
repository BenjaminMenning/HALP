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
    
    public void inputMaxDataRate();
    
    public void inputErrorRate();
    
    public void inputCorruptRate();
    
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
     * @param n the int between 0 and 1 that IG creates when starting up
     * @return true for corrupted data, false for lost packet
     */
    public boolean isCorrupt();
    
    /**
     * This method returns a random index for a byte in the frame for an 
     * error to be placed.
     * 
     * @return int  returns an int containing the byte index for the error
     */
    public int randomIndex(int index);

    /**
     * This error generates an error within a byte by flipping a random bit 
     * inside of the byte.
     * 
     * @param oldByte the byte where the error is to be generated
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
     * @param rate  the int to be assigned as the error rate
     */
    public void setErrorRate(double rate);
    
    /**
     * This method retrieves the error rate for the data transmission
     * 
     * @return int  returns the int assigned as the error rate
     */
    public double getErrorRate();
    
    /**
     * This method assigns the error rate for the data transmission.
     * 
     * @param rate  the int to be assigned as the error rate
     */
    public void setCorruptRate(double rate);
    
    /**
     * This method retrieves the error rate for the data transmission
     * 
     * @return int  returns the int assigned as the error rate
     */
    public double getCorruptRate();
}