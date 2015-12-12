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

import java.util.ArrayList;

/** 
 * This class provides methods and functionality to be implemented by the 
 * ConnectionTable class. It stores Connection objects and implements methods for
 * adding connections, removing connections, and retrieving the corresponding
 * connection (i.e. the connection info of the server if passed connection info
 * of client), printing the connection table, and searching the connection 
 * table.
 * 
 * @author Benjamin Menning, John Blacketer
 * @version 12/15/2015
*/
public class ConnectionTable implements ConnectionTableInterface 
{
    Connection connect = new Connection();
    ArrayList<Connection> table = new ArrayList();

    @Override
    public void addConnection(Connection e) {
        boolean add = table.add(e);
    }

    @Override
    public void removeConnection(Connection e) {
        boolean remove = table.remove(e);
    }

    
    @Override
    public Connection getCorrespondingConnection(String ipAddr, int portNum) {
        Connection tempOut = new Connection();
        for(int i = 0; i<table.size(); i++){
            connect = table.get(i);
            
             if(ipAddr.equals(connect.getIpClient()) && portNum == connect.getPortClient()){
              tempOut.setTempIP(connect.getIpServer());
              tempOut.setTempPort(connect.getPortServer());
              tempOut.setTempRate(connect.getRate());
              break;
             }
             
             else if(ipAddr.equals(connect.getIpServer()) && portNum == connect.getPortServer()){
              tempOut.setTempIP(connect.getIpClient());
              tempOut.setTempPort(connect.getPortClient());
              tempOut.setTempRate(connect.getRate());
              break;
             }
        }
        return tempOut;
    }

    @Override
    public String printTable() {
        String printOut = "";
        
        for(int i =0; i<table.size(); i++){
           Connection tempPrint = table.get(i);
           String ipClient = tempPrint.getIpClient();
           int portClient = tempPrint.getPortClient();
           String ipServer = tempPrint.getIpServer();
           int portServer = tempPrint.getPortServer();
           int rate = tempPrint.getRate();
           
           printOut += "\nConnection " + i + ": \n" + "Client IP: " + ipClient + "\nClient port: " + 
                       portClient + "\nServer IP: " + ipServer + "\nServer port: " + portServer + "\nData rate: " + rate + "\n";
        }
        return printOut;
    }
 
    @Override
    public boolean searchTable(String ipAddr, int portNum){
        boolean result = false;
        for(int i = 0; i<table.size(); i++){
            connect = table.get(i);
            
             if(ipAddr.equals(connect.getIpClient()) && portNum == connect.getPortClient()){
              result = true;
              break;
             }
             
             else if(ipAddr.equals(connect.getIpServer()) && portNum == connect.getPortServer()){
              result = true;
              break;
             }
        }
        return result;
    }
    
}