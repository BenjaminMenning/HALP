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
    public void setClientIP(String ipAddr);
    
    public void setClientPort(int portNum);
    
    public String getClientIP();
    
    public int getClientPort();
    
    public void setIGIP(String ipAddr);
    
    public void setIGPort(int portNum);
    
    public String getIGIP();
    
    public int getIGPort();
    
    public void setServerIP(String ipAddr);
    
    public void setServerPort(int portNum);
    
    public String getServerIP();
    
    public int getServerPort();
}
