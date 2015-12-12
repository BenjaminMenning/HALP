package HALP;

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


/** 
 * This class provides methods and functionality to be implemented by the 
 * Connection class. It describes methods for assigning and retrieving the 
 * port numbers and IP addresses of a client and a server as a pair.
 * 
 * @author Benjamin Menning, John Blacketer
 * @version 12/15/2015
*/
public class Connection implements ConnectionInterface
{
 String ipClient;
 int portClient;
 String ipServer;
 int portServer;
 int rate;
 String tempIP;
 int tempPort;
 int tempRate;

    public Connection(){  
    }
 
    public Connection(String tempIP, int tempPort, int tempRate) {
        this.tempIP = tempIP;
        this.tempPort = tempPort;
        this.tempRate = tempRate;
    }
   
    public Connection(String ipClient, int portClient, String ipServer, int portServer, int rate) {
        this.ipClient = ipClient;
        this.portClient = portClient;
        this.ipServer = ipServer;
        this.portServer = portServer;
        this.rate = rate;
    }

 @Override
    public String getTempIP() {
        return tempIP;
    }

 @Override
    public void setTempIP(String tempIP) {
        this.tempIP = tempIP;
    }

 @Override
    public int getTempPort() {
        return tempPort;
    }

 @Override
    public void setTempPort(int tempPort) {
        this.tempPort = tempPort;
    }

 @Override
    public int getTempRate() {
        return tempRate;
    }

 @Override
    public void setTempRate(int tempRate) {
        this.tempRate = tempRate;
    }
    
 @Override
    public String getIpClient() {
        return ipClient;
    }

 @Override
    public void setIpClient(String ipClient) {
        this.ipClient = ipClient;
    }

 @Override
    public int getPortClient() {
        return portClient;
    }

 @Override

 public void setPortClient(int portClient) {
        this.portClient = portClient;
    }

 @Override
    public String getIpServer() {
        return ipServer;
    }

 @Override
    public void setIpServer(String ipServer) {
        this.ipServer = ipServer;
    }

 @Override
    public int getPortServer() {
        return portServer;
    }

 @Override
    public void setPortServer(int portServer) {
        this.portServer = portServer;
    }

 @Override
    public int getRate() {
        return rate;
    }

 @Override
    public void setRate(int rate) {
        this.rate = rate;
    }

 @Override
    public String conToString() {
        return "Connection{" + "Client IP = " + ipClient + ", Client port = " + portClient + ", Server IP = " + ipServer + ", Server port = " + portServer + ", Data rate = " + rate + '}';
    }
    
 @Override
    public String tempToString(){
        return "Connection{" + "Temp IP = " + tempIP + ", Temp port = " + tempPort + ", Data rate = " + tempRate + '}';
    }
}