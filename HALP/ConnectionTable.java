/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HALP;

import java.util.ArrayList;

/**
 *
 * @author Ben
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
}