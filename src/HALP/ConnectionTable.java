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
       int search = table.indexOf(ipAddr.equals(connect.getIpClient()) && portNum == connect.getPortClient());
        
             if(search != -1){
                 Connection temp = table.get(search);
                 return temp;
             }
         
             else{
                int search2 = table.indexOf(ipAddr.equals(connect.getIpServer()) && portNum == connect.getPortServer()); 
                
                   if(search2 != -1){
                       Connection temp = table.get(search);
                      return temp;
                   }
                   
                   else{
                       
                   }
                   
             }
        return null;
    }

    @Override
    public void printTable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

  
}
