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
public interface ConnectionTableInterface 
{
    // whatever data structure this uses is up to you
    // It's probably best to choose a data structure that's easy to search
    // so we can retrieve the connection information without much hassle
    
    /**
     *
     * @param e adds specified connection to table
     */
        
    public void addConnection(Connection e);
    
    /**
     *
     * @param e removes specified connection from table
     */
    public void removeConnection(Connection e);
    
    /**
     * Wait to implement.
     * This method retrieves the corresponding connection information for a 
     * IP address and port number pair that are passed into the table. For 
     * instance, if you pass in the Client IP address and port number, you will
     * receive the Server connection and vice versa. This makes it so the IG 
     * can determine which to send to after extracting the IP address and port
     * number from the datagram.
     * 
     * @param ipAddr IP address from sender of packet
     * @param portNum Port number from sender of packet
     * @return a Connection object that contains tempIP(receiver ip), tempPort(receiver port), tempRate(data rate of connection)
     */
    public Connection getCorrespondingConnection(String ipAddr, int portNum);
    
    /**
     * This method prints the contents of the connection table.
     * 
     * @return String output of all connections stored in the table
     */
    public String printTable();
}