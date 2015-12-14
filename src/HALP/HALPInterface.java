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
     *Method used to set the client ip address to be used by other methods
     * 
     * @param ipAddr string that is to set as the ip address for the client
     */
    public void setClientIP(String ipAddr);
    
    /**
     *Method used to set the client port number to be used by other methods
     * 
     * @param portNum the port number for the client
     */
    public void setClientPort(int portNum);
    
    /**
     *Method used for getting the ip address for the client
     * 
     * @return string that is the client ip address
     */
    public String getClientIP();
    
    /**
     *Method used for getting the port number of the client
     * 
     * @return integer that is the clients port number
     */
    public int getClientPort();
    
    /**
     *Method used for setting the internet gateway ip address to be used by other methods
     * 
     * @param ipAddr string that is to be set as the internet gateways ip address
     */
    public void setIGIP(String ipAddr);
    
    /**
     *Method used the set the port number of the internet gateway to be used by other methods
     * 
     * @param portNum integer that is the port number of the internet gateway
     */
    public void setIGPort(int portNum);
    
    /**
     *Method used for getting the internet gateways ip address
     * 
     * @return string that is the internet gateways ip address
     */
    public String getIGIP();
    
    /**
     *Method used for getting the port number of the internet gateway
     * 
     * @return integer that is the internet gateway port number
     */
    public int getIGPort();
    
    /**
     *Method used for setting the server ip address used by other methods
     * 
     * @param ipAddr string that is the ip address of the server
     */
    public void setServerIP(String ipAddr);
    
    /**
     *Method for setting the port number of the server used by other methods
     * 
     * @param portNum integer that is the port number of the server
     */
    public void setServerPort(int portNum);
            
    /**
     *Method used for getting the ip address of the server
     * 
     * @return string that is the ip address of the server
     */
    public String getServerIP();
    
    /**
     *Method for getting the port number of the server 
     * 
     * @return integer that is the port number of the server
     */
    public int getServerPort();
    
    /**
     *Method for getting the bytes that are the segment sent 
     * 
     * @return byte array that contains the bytes of the segment
     */
    public byte[] getMessage();
    
     /**
     *Method to assemble a message that is to be sent 
     * 
     * 
     * @param headerBytes byte array that contains the header information
     * @param dataBytes byte array that contains the data being sent
     * @return byte array that contains the header information and data
     */
    public byte[] assembleMessage(byte[] headerBytes, byte[] dataBytes);
    
     /**
     *Method to send a message 
     * 
     * @param messageBytes byte array of the message to be sent
     * @throws java.lang.Exception
     */
    public void sendMessage(byte[] messageBytes) throws Exception;
    
     /**
     *Method for receiving messages
     * 
     * @return byte array of the message received 
     * @throws java.net.SocketTimeoutException 
     */
    public byte[] receiveMessage() throws SocketTimeoutException;
    
     /**
     *Method for starting methods in a program
     * 
     */
    public void startDevice();

     /**
     *Method to close a device connection socket
     * 
     */
    public void closeConnection();
    
     /**
     *Method for converting an integer that is a port number into bytes
     * 
     * @param portNum integer that is the port number to be converted to bytes
     * @return byte array of the bytes representing the port number
     */
    public byte[] convertPNToBytes(int portNum);
    
     /**
     *Method for setting the data of a message *used for testing
     * 
     */
    public void setData();
    
     /**
     *Method for setting the reset flag
     * 
     * @param headerBytes byte array that contains header information
     * @param isSet boolean specifying if the reset flag is set
     * @return byte array containing header information fwith the reset flag set or unset
     */
    // Do not implement RST unless we find we need it
    public byte[] setRSTFlag(byte[] headerBytes, boolean isSet);
    
     /**
     *Method for setting the direction flag
     * 
     * @param headerBytes byte array that contains the header information
     * @param isSet boolean specifying if the direction flag is upload or not
     * @return byte array containing the header information and the direction flag set
     */
    public byte[] setDRTFlag(byte[] headerBytes, boolean isSet);
    
     /**
     *Method for setting the acknowledgment flag
     * 
     * @param headerBytes byte array that contains the header information
     * @param isSet boolean indicating if there is an acknowledgment or not
     * @return byte array containing the header information and acknowledgment flag set
     */
    public byte[] setACKFlag(byte[] headerBytes, boolean isSet);
    
     /**
     *Method for setting the sync flag
     * 
     * @param headerBytes byte array that contains the header information
     * @param isSet boolean that indicates if the sync bit is set
     * @return byte array containing the header information and sync flag set
     */
    public byte[] setSYNFlag(byte[] headerBytes, boolean isSet);
    
     /**
     *Method for setting the finish flag
     * 
     * @param headerBytes byte array that contains the header information
     * @param isSet boolean that indicates if the finish flag is set
     * @return byte array containing the header information and finish flag set
     */
    public byte[] setFINFlag(byte[] headerBytes, boolean isSet);
    
     /**
     *Method for setting the data rate field in the message
     * 
     * @param messageBytes byte array that contains the message information
     * @param rate integer that is the data rate for sending the messages
     * @return byte array that contains the data rate 
     */
    public byte[] setDataRateField(byte[] messageBytes, int rate);
    
    
     /**
     *Method for setting the checksum in the message to be sent 
     * 
     * @param messageBytes byte array for message being sent
     * @return byte array that contains the calculated checksum set
     */
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
    
     /**
     *Method used for getting the file name from a message
     * 
     * @param messageBytes byte array that contains the bytes of a message
     * @return string that is the file name of the file being transfered
     */
    public String getFileNameField(byte[] messageBytes);
    
     /**
     *Method for retrieving the data rate from a message
     * 
     * @param messageBytes byte array that contains the bytes of a message
     * @return integer that is the data rate
     */
    public int getDataRateField(byte[] messageBytes); 
    
     /**
     *Method for retrieving the checksum from a message
     * 
     * @param messageBytes byte array that contains the bytes of a message
     * @return integer that is the checksum
     */
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
        
     /**
     *Method for printing the destination ip address
     * 
     * @param headerBytes byte array that contains the header information
     */
    public void printDestIPField(byte[] headerBytes);
    
     /**
     *Method for printing the destination port number
     * 
     * @param headerBytes byte array that contains the header information
     */
    public void printDestPNField(byte[] headerBytes);
    
     /**
     *Method for printing the flag field
     * 
     * @param headerBytes byte array that contains the header information
     */
    public void printFlagField(byte[] headerBytes);
    
     /**
     *Method for printing the file name
     * 
     * @param messageBytes byte array that contains the header information
     */
    public void printFileNameField(byte[] messageBytes);
    
     /**
     *Method for printing the data rate
     * 
     * @param messageBytes byte array that contains the header information
     */
    public void printDataRateField(byte[] messageBytes);
    
     /**
     *Method for printing the header information
     * 
     * @param messageBytes byte array that contains the message information
     */
    public void printHeaderField(byte[] messageBytes);
    
     /**
     *Method for printing the data
     * 
     * @param messageBytes byte array that contains the message information
     */
    public void printDataField(byte[] messageBytes);
    
     /**
     *Method for printing the checksum
     * 
     * @param messageBytes byte array that contains the message information
     */
    public void printChecksum(byte[] messageBytes);
    
    /**
     * This method sets whether or not the trace feature will be turned on.
     * 
     * @param isTraceSet
     */
    public void setTrace(boolean isTraceSet);
    
     /**
     *Method for the user to indicate if the trace function is to be on or off
     * 
     * 
     */
    public void inputTrace();
    
    /**
     * This method retrieves the size of the file that is being transferred.
     * 
     * @return double that is the file size
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
     * This method calculates the total file transfer time. 
     * 
     * @param s long from startTransferTimer
     * @param e long from stopTransferTimer
     * @return String that says "The time to transfer file is " +(e-s) + " seconds."
     */
    public String getTransferTime(long s, long e);
    
    /**
     * This method retrieves the total number of messages generated.
     * 
     * @return integer of number of messages created by the sender
     */
    public int getMessagesGenerated();
    
    /**
     * This method retrieves the total number of UDP datagrams transmitted.
     * 
     * @return integer of how many datagrams have been sent from the sender
     */
    public int getUDPDatagramsTransmitted();
    
    /**
     * This method retrieves the total number of retransmissions
     * 
     * @return integer of total number of retransmissions were sent 
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
     * @return double that is the percentage of retransmissions
     */
    public double getPercentageOfRetransmissions();
    
    /**
     * This method retrieves the expected number of retransmissions.
     * 
     * @return double that is what is the expected number of retransmissions
     */
    public double getExpectedRetransmissions();
    
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
     *Method that generates the first sequence number, only used for syncing
     * 
     * @return sequence long between 0-4294967295
     */
    public long generateSequenceNumber();
    
    /**
     *Method for incrementing the sequence number 
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
     * @return sequenceNum long that is the sequence number
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
     * Method for generating the acknowledgment number
     * 
     * @param sequence the sequence number from incoming message to be acknowledged
     * @return acknowledgment long that is sequence +1, if sequence = 42944967295 then acknowledgment = 0
     */
    public long generateAcknowledgement(long sequence);
            
    /**
     * Method for retrieving the acknowledgment number
     * 
     * @param headerBytes byte[] containing header information
     * @return acknowledgment integer that is the acknowledgment number from header
     */
    public long getAcknowledgmentNumber(byte[] headerBytes);
    
    /**
     * Method for printing the sequence number
     * 
     * Takes byte[] containing header information, and prints a string with the sequence number
     * @param headerBytes byte[] containing header information
     */
    public void printSequenceNumber(byte[] headerBytes);
    
    /**
     * Method for printing the acknowledgment number
     * 
     * Takes byte[] containing header information, and prints a string with the acknowledgment number
     * @param headerBytes byte[] containing header information
     */
    public void printAcknowledgmentNumber(byte[] headerBytes);
    
    /**
     * Method for generating a string of header information to be written to a log file
     * 
     * @param headerBytes byte [] containing header Bytes
     * @return String containing information from the header(destination IP and port, sequence number, acknowledgment number, checksum, and data size)
     */
     public String messageLog(byte[] headerBytes);
     
     /**
      * Method for generating a string of header information to be written to a log file
      * 
     * @param headerBytes  byte [] containing header Bytes
     * @return String stating resent segment plus information from the header(destination IP and port, sequence number, acknowledgment number, checksum, and data size)
      */
      public String resendLog(byte[] headerBytes);
      
      /**
       * Method for generating a string of header information to be written to a log file
       * 
     * @param headerBytes  byte [] containing header Bytes
     * @return String stating error generated plus information from the header(destination IP and port, sequence number, acknowledgment number, checksum, and data size)
       */
       public String errorGeneratedLog(byte[] headerBytes);
       
       /**
        * Method for generating a string of header information to be written to a log file
        * 
     * @param headerBytes  byte [] containing header Bytes
     * @return String stating error detected plus information from the header(destination IP and port, sequence number, acknowledgment number, checksum, and data size)
        */
       public String errorDetectedLog(byte[] headerBytes);
}