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

/** 
 * This interface provides methods and functionality to be implemented by the 
 * Connection class. It describes methods for assigning and retrieving the 
 * port numbers and IP addresses of a client and a server as a pair.
 * 
 * @author Benjamin Menning, John Blacketer
 * @version 12/15/2015
*/
public interface ConnectionInterface 
{
    /**
     * This method is used to retrieve the ip of the intended receiver from the connection table 
     *  
     * @return tempIP(receivers ip) after searching for a connection in connection table
     */
     public String getTempIP();
     
     /**
     * This method sets the ip address of the intended receiver in the connection table
     * @param tempIP the ip address of the intended receiver
     */
    public void setTempIP(String tempIP);
    
     /**
     *  This method is used to retrieve the port of the intended receiver from the connection table
     * @return tempPort(receivers port number) after searching for a connection in connection table
     */
    public int getTempPort();
    
    /**
     * This method sets the port number of the intended receiver in the connection table
     * @param tempPort the port number of the intended receiver
     */
    public void setTempPort(int tempPort);
    
    /**
     * The data rate of a connection in the connection table
     * @return tempRate is the data rate from the connection found after searching connection table
     */
    public int getTempRate();
    
    /**
     * sets the data rate for a connection in the connection table
     * @param tempRate data rate of the connection found after searching the connection table and 
     */
    public void setTempRate(int tempRate);
    
    /**
     * Used to retrieve the ip address of a client from a connection object 
     * @return ipClient ip address of the client in a connection object
     */
    public String getIpClient();
    
    /**
     * Used to set the ip address of a client from a connection object
     * @param ipClient sets the ip address of the client in a connection object
     */
    public void setIpClient(String ipClient);
    
    /**
     * Used to retrieve the port number of a client from a connection object
     * @return portClient port number of the client in a connection object
     */
    public int getPortClient();

    /**
     * Used to set the port number of a client from a connection object
     * @param portClient sets the port number of the client in a connection object 
     */
    public void setPortClient(int portClient);

    /**
     * Used to retrieve the ip address of a server from a connection object
     * @return ipServer ip address of the server in a connection object
     */
    public String getIpServer();

    /**
     * Used to set the ip address of a server from a connection object
     * @param ipServer sets the ip address of the server in a connection object
     */
    public void setIpServer(String ipServer);

    /**
     * Used to retrieve the port number of a server from a connection object
     * @return portSerrver port number of the server in a connection object
     */
    public int getPortServer();

    /**
     * Used to set the port number of a server from a connection object
     * @param portServer sets the port number of the server in a connection object 
     */
    public void setPortServer(int portServer);

    /**
     * Used to retrieve the data rate of a connection object
     * @return rate data rate of a connection object
     */
    public int getRate();

    /**
     * Used to set the data rate of a connection object
     * @param rate sets the data rate of a connection object
     */
    public void setRate(int rate);

    /**
     * Used to create a string that has the client ip, client port, server ip, server port, and data rate of a connection object
     * @return a string that states the client IP address, client port number, server IP address, server port number, and data rate.
     */
    public String conToString();
    
    /**
     * Used to create a string that has the temp ip, temp port, and data rate of a temp connection object
     * @return a string that states the temp IP address, temp port number, and data rate.
     */
    public String tempToString();
     
}

