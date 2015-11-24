/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HALP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 *
 * @author Ben
 */
public interface HALPIGInterface extends HALPInterface
{
    
//    public void convertBytesToDestIP();
//
//    public void convertBytesToDestPN();
    
    public void inputDataRate();
    
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
    
    /**
     * This method retrieves the expected number of retransmissions.
     * 
     * @return
     */
    public int getExpectedRetransmissions();
}