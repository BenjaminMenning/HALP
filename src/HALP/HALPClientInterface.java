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
    
    public void convertDestIPToBytes();
    
    public void convertDestPNToBytes();

}
