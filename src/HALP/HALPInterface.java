/** 
 * Author:          Benjamin Menning, John Blacketer
 * 
 * Date:            12/15/2015 
 *                
 * Course:          CS 413 Advanced Networking
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

import java.net.SocketTimeoutException;

/** 
 * This interface provides methods and functionality to be implemented by the 
 * HALP class. It describes methods for retrieving values from header fields, 
 * assigning values to header fields, and printing out header fields of HALP
 * messages. It also includes many other methods for things like generating 
 * sequence numbers and calculating checksums.
 * 
 * @author Benjamin Menning, John Blacketer
 * @version 12/15/2015
*/
public interface HALPInterface 
{

    /**
     *
     * @param ipAddr
     */
    public void setClientIP(String ipAddr);
    
    /**
     *
     * @param portNum
     */
    public void setClientPort(int portNum);
    
    /**
     *
     * @return
     */
    public String getClientIP();
    
    /**
     *
     * @return
     */
    public int getClientPort();
    
    /**
     *
     * @param ipAddr
     */
    public void setIGIP(String ipAddr);
    
    /**
     *
     * @param portNum
     */
    public void setIGPort(int portNum);
    
    /**
     *
     * @return
     */
    public String getIGIP();
    
    /**
     *
     * @return
     */
    public int getIGPort();
    
    /**
     *
     * @param ipAddr
     */
    public void setServerIP(String ipAddr);
    
    /**
     *
     * @param portNum
     */
    public void setServerPort(int portNum);
            
    /**
     *
     * @return
     */
    public String getServerIP();
    
    /**
     *
     * @return
     */
    public int getServerPort();
    
    /**
     * Just for testing right now.
     * 
     * @return
     */
    public byte[] getMessage();
    
    public byte[] assembleMessage(byte[] headerBytes, byte[] dataBytes);
    
    public void sendMessage(byte[] messageBytes) throws Exception;
    
    public byte[] receiveMessage() throws SocketTimeoutException;
    
    public void run();

    public void closeConnection();
    
    public byte[] convertPNToBytes(int portNum);
    
    public void setData();
    
    // Do not implement RST unless we find we need it
    public byte[] setRSTFlag(byte[] headerBytes, boolean isSet);
    
    public byte[] setDRTFlag(byte[] headerBytes, boolean isSet);
    
    public byte[] setACKFlag(byte[] headerBytes, boolean isSet);
    
    public byte[] setSYNFlag(byte[] headerBytes, boolean isSet);
    
    public byte[] setFINFlag(byte[] headerBytes, boolean isSet);
    
    public byte[] setDataRateField(byte[] messageBytes, int rate);
    
    public byte[] setChecksum(byte[] messageBytes);
    
    /**
     * This method extracts the header from a message.
     * 
     * @param messageBytes  the byte array containing the message
     * @return  byte[]  returns byte array containing the header bytes
     */
    public byte[] getHeader(byte[] messageBytes);
    
    /**
     * This method extracts the data from a message.
     * 
     * @param messageBytes  the byte array containing the message
     * @return  byte[]  returns byte array containing the data bytes
     */
    public byte[] getData(byte[] messageBytes);
    
    
    /**
     * This method extracts the destination IP address formatted as a String 
     * from a message. This method might need to be changed later on if we 
     * alter our header format.
     * 
     * @param messageBytes  the byte array containing the message
     * @return  String  returns String containing the IP address
     */
    public String getDestinationIP(byte[] messageBytes);
    
    /**
     * This method extracts the destination port number formatted as an integer
     * from a message. This method might need to be changed later on if we 
     * alter our header format.
     * 
     * @param messageBytes  the byte array containing the message
     * @return  int returns int containing the port number
     */
    public int getDestinationPort(byte[] messageBytes);  
    
    public String getFileNameField(byte[] messageBytes);
    
    public int getDataRateField(byte[] messageBytes); 
    
    public int getChecksum(byte[] messageBytes);
    
    /**
     * This method determines if the checksum in the message is valid
     * 
     * @param messageBytes   the byte array containing the message information
     * @return  returns true if valid, false otherwise
     */
    public boolean isChecksumValid(byte[] messageBytes);
        
    /**
     * This method determines if the RST flag has been set.
     * 
     * @param headerBytes   the byte array containing the header information
     * @return  returns true if set, false otherwise
     */
    public boolean isRSTFlagSet(byte[] headerBytes);
    
    /**
     * This method determines if the DRT flag has been set.
     * 
     * @param headerBytes   the byte array containing the header information
     * @return  returns true if upload, false if download
     */
    public boolean isDRTFlagSet(byte[] headerBytes);
    
    /**
     * This method determines if the ACK flag has been set.
     * 
     * @param headerBytes   the byte array containing the header information
     * @return  returns true if set, false otherwise
     */
    public boolean isACKFlagSet(byte[] headerBytes);
    
    /**
     * This method determines if the SYN flag has been set.
     * 
     * @param headerBytes   the byte array containing the header information
     * @return  returns true if set, false otherwise
     */
    public boolean isSYNFlagSet(byte[] headerBytes);
    
    /**
     * This method determines if the FIN flag has been set.
     * 
     * @param headerBytes   the byte array containing the header information
     * @return  returns true if set, false otherwise
     */
    public boolean isFINFlagSet(byte[] headerBytes);
        
    public void printDestIPField(byte[] headerBytes);
    
    public void printDestPNField(byte[] headerBytes);
    
    public void printFlagField(byte[] headerBytes);
    
    public void printFileNameField(byte[] messageBytes);
    
    public void printDataRateField(byte[] messageBytes);
    
    public void printHeaderField(byte[] messageBytes);
    
    public void printDataField(byte[] messageBytes);
    
    public void printChecksum(byte[] messageBytes);
    
    /**
     * This method sets whether or not the trace feature will be turned on.
     * 
     * @param isTraceSet
     * @return trace true or false depending what is passed in
     */
    public boolean setTrace(boolean isTraceSet);
    
    /**
     * This method retrieves the size of the file that is being transferred.
     * 
     */
    public double getFileSize();
    
    /**
     * This method starts the timer used for the data transfer. Used in trace.
     * 
     * @return time in seconds for the current time before transfer starts
     */
    public long startTransferTimer();
    
    /**
     * This method stops the timer used for the data transfer. Used in trace.
     * 
     * @return time in seconds for the current time after transfer is complete
     */
    public long stopTransferTimer();
    
    /**
     * This method retrieves the total file transfer time. 
     * 
     * @param s long from startTransferTimer
     * @param e long from stopTransferTimer
     * @return String that says "The time to transfer file is " +(e-s) + " seconds."
     */
    public String getTransferTime(long s, long e);
    
    /**
     * This method retrieves the total number of messages generated.
     * 
     */
    public int getMessagesGenerated();
    
    /**
     * This method retrieves the total number of UDP datagrams transmitted.
     * 
     */
    public int getUDPDatagramsTransmitted();
    
    /**
     * This method retrieves the total number of retransmissions
     * 
     */
    public int getTotalRetransmissions();
    
    /**
     * This method adds a maximum amount of re-transmissions for a single frame
     * to an ArrayList.
     * 
     * @param maxTransmission the int to be assigned as the max transmissions
     */
    public void addMaxTransmission(int maxTransmission);
    
    /**
     * This method retrieves the maximum number of transmissions for any single
     * frame.
     * 
     * @return int  returns an int containing the max transmissions
     */
    public int getMaxTransmission();
    
    /**
     * This method retrieves the status of the max transmission ArrayList of
     * whether or not it is empty.
     * 
     * @return boolean  returns true if empty, false if otherwise
     */
    public boolean isMaxTransmissionsEmpty();
    
    /**
     * This method retrieves the percentage of messages that were retransmitted.
     * 
     * @return
     */
    public double getPercentageOfRetransmissions();
    
    /**
     * This method prints out the trace statistics after the file transfer is
     * complete. Possibly a polymorhpic method?
     * 
     */
    public void printTraceStats();
        
    /**
     * This method prints out the contents of a message, including the contents
     * of each individual field (except the actual data). Used for testing.
     * 
     * @param messageBytes  the byte array containing the message information
     */
    public void printMessage(byte [] messageBytes);
    
    /**
     * generates the first sequence number, only used for syncing
     * @return sequence integer between 0-2147483647
     */
    public long generateSequenceNumber();
    
    /**
     * 
     * @param sequence current sequence number that was sent in last segment
     * @return seq long that is the next sequence number to be used when sending the next segment
     */
    public long incrementSequence(long sequence);
    
    /**
     * This method takes a byte[] containing header information and modifies the sequence number bytes to the correct 
     * sequence number representation and then returns the array back out 
     * sequence number has a max of 42944967295, if generated is larger then minus 42944967295 from that number generated to 
     * get sequence number
     * @param headerBytes pass in byte[] that contains the header information
     * @param number pass in the sequence number for the segment that is to be sent
     * @return headerBytes but the sequence number bytes are now setup for the message to be sent
     */
    public byte[] setSequenceNumber(byte[] headerBytes, long number);
    
    /**
     * Takes byte[] containing header information and returns an integer that is the acknowledgment number
     * @param headerBytes byte[] that contains header information
     * @return sequenceNum integer that is the sequence number
     */
    public long getSequenceNumber(byte[] headerBytes);
    
    /**
     * Takes byte[] containing header information, generates the acknowledgment number and 
     * then sets the acknowledgment bytes, and returns the new array 
     * @param headerBytes byte[] containing header information 
     * @param acknowledgment long number that is the acknowledgment number(sequence number of received message +1)
     * @return headerBytes  byte[] that now has the acknowledgment bytes set 
     */
    public byte[] setAcknowledgmentNumber(byte[] headerBytes, long acknowledgment);
    
    /**
     * 
     * @param sequence the sequence number from incoming message to be acknowledged
     * @return acknowledgment long that is sequence +1, if sequence = 42944967295 then acknowledgment = 0
     */
    public long generateAcknowledgement(long sequence);
            
    /**
     * 
     * @param headerBytes byte[] containing header information
     * @return acknowledgment integer that is the acknowledgment number from header
     */
    public long getAcknowledgmentNumber(byte[] headerBytes);
    
    /**
     * Takes byte[] containing header information, and prints a string with the sequence number
     * @param headerBytes byte[] containing header information
     */
    public void printSequenceNumber(byte[] headerBytes);
    
    /**
     * Takes byte[] containing header information, and prints a string with the acknowledgment number
     * @param headerBytes byte[] containing header information
     */
    public void printAcknowledgmentNumber(byte[] headerBytes);
    
    /**
     * 
     * @param headerBytes byte [] containing header Bytes
     * @return String containing information from the header(destination IP and port, sequence number, acknowledgment number, checksum, and data size)
     */
     public String messageLog(byte[] headerBytes);
     
     /**
      * 
     * @param headerBytes  byte [] containing header Bytes
     * @return String stating resent segment plus information from the header(destination IP and port, sequence number, acknowledgment number, checksum, and data size)
      */
      public String resendLog(byte[] headerBytes);
      
      /**
       * 
     * @param headerBytes  byte [] containing header Bytes
     * @return String stating error generated plus information from the header(destination IP and port, sequence number, acknowledgment number, checksum, and data size)
       */
       public String errorGeneratedLog(byte[] headerBytes);
       
       /**
        * 
     * @param headerBytes  byte [] containing header Bytes
     * @return String stating error detected plus information from the header(destination IP and port, sequence number, acknowledgment number, checksum, and data size)
        */
       public String errorDetectedLog(byte[] headerBytes);
}