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
    
    public void assembleMessage();
    
    public void sendMessage() throws Exception;
    
    public void receiveMessage();
    
    public void run();

    public void closeConnection();
    
    public void setData();
    
    public void setRSTFlag(byte[] headerBytes, boolean isSet);
    
    public void setDRTFlag(byte[] headerBytes, boolean isSet);
    
    public void setACKFlag(byte[] headerBytes, boolean isSet);
    
    public void setSYNFlag(byte[] headerBytes, boolean isSet);
    
    public void setFINFlag(byte[] headerBytes, boolean isSet);
    
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
     * This method determines if the checksum in the header is valid?
     * 
     * @param headerBytes   the byte array containing the header information
     * @return  returns true if valid, false otherwise
     */
    public boolean isChecksumValid(byte[] headerBytes);
        
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
    
}