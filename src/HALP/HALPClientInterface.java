/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HALP;

import java.net.InetAddress;

/**
 *
 * @author Ben
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
}