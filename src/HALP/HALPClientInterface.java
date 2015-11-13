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
    
    public void setTransferDirection();
    
    public void setFileName(String fileName);
    
    public void setDataRate();
    
    public byte[] setDestIP(byte[] headerBytes, String destIP);
    
    public byte[] setDestPN(byte[] headerBytes, int portNum);
        
    public byte[] convertIPToBytes(String ipAddr);
    
    public byte[] convertPNToBytes(int portNum);
    
    public void initiateConnection();
}