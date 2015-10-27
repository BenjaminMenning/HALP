/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HALP;

import java.net.DatagramSocket;
import java.net.SocketException;

/**
 *
 * @author Ben
 */
public class HALP implements HALPInterface
{
    private String clntIPAddr = "";
    private String igIPAddr = "";
    private String servIPAddr = "";
    private int clntPortNum = 0;
    private int igPortNum = 0;
    private int servPortNum = 0;
    private DatagramSocket clntSocket;
    private DatagramSocket igSocket;
    private DatagramSocket servSocket;

    public HALP() throws SocketException
    {
        clntSocket = new DatagramSocket();
        igSocket = new DatagramSocket();
        servSocket = new DatagramSocket();
    }
    
    @Override
    public void setClientIP(String ipAddr) 
    {
        clntIPAddr = ipAddr;
    }

    @Override
    public void setClientPort(int portNum) 
    {
        clntPortNum = portNum;
    }

    @Override
    public String getClientIP() 
    {
        return clntIPAddr;
    }

    @Override
    public int getClientPort() 
    {
        return clntPortNum;
    }

    @Override
    public void setIGIP(String ipAddr) 
    {
        igIPAddr = ipAddr;
    }

    @Override
    public void setIGPort(int portNum) 
    {
        igPortNum = portNum;
    }

    @Override
    public String getIGIP() 
    {
        return igIPAddr;
    }

    @Override
    public int getIGPort() 
    {
        return igPortNum;
    }

    @Override
    public void setServerIP(String ipAddr) 
    {
        servIPAddr = ipAddr;
    }

    @Override
    public void setServerPort(int portNum) 
    {
        servPortNum = portNum;
    }

    @Override
    public String getServerIP() 
    {
        return servIPAddr;
    }

    @Override
    public int getServerPort() 
    {
        return servPortNum;
    }
}