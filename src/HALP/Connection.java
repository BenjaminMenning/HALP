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

    public Connection(String ipClient, int portClient, String ipServer, int portServer, int rate) {
        this.ipClient = ipClient;
        this.portClient = portClient;
        this.ipServer = ipServer;
        this.portServer = portServer;
        this.rate = rate;
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

    @Override
    public String toString() {
        return "Connection{" + "ipClient=" + ipClient + ", portClient=" + portClient + ", ipServer=" + ipServer + ", portServer=" + portServer + ", rate=" + rate + '}';
    }
    
    

}
