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

    public String getTempIP() {
        return tempIP;
    }

    public void setTempIP(String tempIP) {
        this.tempIP = tempIP;
    }

    public int getTempPort() {
        return tempPort;
    }

    public void setTempPort(int tempPort) {
        this.tempPort = tempPort;
    }

    public int getTempRate() {
        return tempRate;
    }

    public void setTempRate(int tempRate) {
        this.tempRate = tempRate;
    }
    
    public String getIpClient() {
        return ipClient;
    }

    public void setIpClient(String ipClient) {
        this.ipClient = ipClient;
    }

    public int getPortClient() {
        return portClient;
    }

    public void setPortClient(int portClient) {
        this.portClient = portClient;
    }

    public String getIpServer() {
        return ipServer;
    }

    public void setIpServer(String ipServer) {
        this.ipServer = ipServer;
    }

    public int getPortServer() {
        return portServer;
    }

    public void setPortServer(int portServer) {
        this.portServer = portServer;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    //@Override
    public String ContoString() {
        return "Connection{" + "Client IP = " + ipClient + ", Client port = " + portClient + ", Server IP = " + ipServer + ", Server port = " + portServer + ", Data rate = " + rate + '}';
    }
    
    public String TemptoString(){
        return "Connection{" + "Temp IP = " + tempIP + ", Temp port = " + tempPort + ", Data rate = " + tempRate + '}';
    }
    
    

}
