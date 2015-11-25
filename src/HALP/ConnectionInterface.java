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
public interface ConnectionInterface 
{
    /**
     * 
     *  
     * @return tempIP(receivers ip) after searching for a connection in connection table
     */
     public String getTempIP();
     
     /**
     * 
     * @param tempIP the ip address of the intended receiver
     */
    public void setTempIP(String tempIP);
    
     /**
     *  
     * @return tempPort(receivers port number) after searching for a connection in connection table
     */
    public int getTempPort();
    
    /**
     * 
     * @param tempPort the port number of the intended receiver
     */
    public void setTempPort(int tempPort);
    
    /**
     * 
     * @return tempRate is the data rate from the connection found after searching connection table
     */
    public int getTempRate();
    
    /**
     * 
     * @param tempRate data rate of the connection found after searching the connection table and 
     */
    public void setTempRate(int tempRate);
    
    /**
     * 
     * @return ipClient ip address of the client in a connection object
     */
    public String getIpClient();
    
    /**
     * 
     * @param ipClient sets the ip address of the client in a connection object
     */
    public void setIpClient(String ipClient);
    
    /**
     * 
     * @return portClient port number of the client in a connection object
     */
    public int getPortClient();

    /**
     * 
     * @param portClient sets the port number of the client in a connection object 
     */
    public void setPortClient(int portClient);

    /**
     * 
     * @return ipServer ip address of the server in a connection object
     */
    public String getIpServer();

    /**
     * 
     * @param ipServer sets the ip address of the server in a connection object
     */
    public void setIpServer(String ipServer);

    /**
     * 
     * @return portSerrver port number of the server in a connection object
     */
    public int getPortServer();

    /**
     * 
     * @param portServer sets the port number of the server in a connection object 
     */
    public void setPortServer(int portServer);

    /**
     * 
     * @return rate data rate of a connection object
     */
    public int getRate();

    /**
     * 
     * @param rate sets the data rate of a connection object
     */
    public void setRate(int rate);

    /**
     * 
     * @return a string that states the client IP address, client port number, server IP address, server port number, and data rate.
     */
    public String conToString();
    
    /**
     * 
     * @return a string that states the temp IP address, temp port number, and data rate.
     */
    public String tempToString();
     
}

