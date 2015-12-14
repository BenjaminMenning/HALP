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
 * ConnectionTable class. It stores Connection objects and describes methods for
 * adding connections, removing connections, and retrieving the corresponding
 * connection (i.e. the connection info of the server if passed connection info
 * of client), printing the connection table, and searching the connection 
 * table.
 * 
 * @author Benjamin Menning, John Blacketer
 * @version 12/15/2015
*/
public interface ConnectionTableInterface 
{
    // whatever data structure this uses is up to you
    // It's probably best to choose a data structure that's easy to search
    // so we can retrieve the connection information without much hassle
    
    /**
     *Used to add a connection the connection table to be referenced at a later time
     * @param e adds specified connection to table
     */
        
    public void addConnection(Connection e);
    
    /**
     *Used to remove an existing connection from the connection table after the connection has been closed
     * @param e removes specified connection from table
     */
    public void removeConnection(Connection e);
    
    /**
     * 
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
    
    /**
     * 
     * @param ipAddr  string ip address to look for a connection in the table
     * @param portNum  int port number to look for a connection with
     * @return result  boolean where true means the connection exists in the table
     */
    public boolean searchTable(String ipAddr, int portNum);
}