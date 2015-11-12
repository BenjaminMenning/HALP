/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HALP;

/**
 *
 * @author Ben
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
    
    public void sendMessage() throws Exception;
    
    public void receiveMessage();
    
    public void run();

    public void closeConnection();
    
    public void setData();
    
    // Do not implement RST unless we find we need it
    public byte[] setRSTFlag(byte[] headerBytes, boolean isSet);
    
    public byte[] setDRTFlag(byte[] headerBytes, boolean isSet);
    
    public byte[] setACKFlag(byte[] headerBytes, boolean isSet);
    
    public byte[] setSYNFlag(byte[] headerBytes, boolean isSet);
    
    public byte[] setFINFlag(byte[] headerBytes, boolean isSet);
    
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
    
    /**
     * This method sets whether or not the trace feature will be turned on.
     * 
     * @param isTraceSet
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
}