package HALP;

/**
 *
 * @author Ben
 */


    // Will hold IP address, port number for sure
    // Will also possibly hold connection ID, fileName, and data rate
    // Include setters, getters, and print methods at minimum
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