/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HALP;

import java.util.Scanner;

/**
 *
 * @author Ben
 */
public class Server2 
{
    private static final int SERVER_PORT = 49000;
    
    public static void main(String args[]) throws Exception
    {
        Scanner console = new Scanner(System.in);
        
        // For old server implmentation, don't delete
//        HALPServer halpServer = new HALPServer(SERVER_PORT);
//        halpServer.run();
        
        // For new server implementation
        HALPClient halpServer = new HALPClient(SERVER_PORT, SERVER_PORT);
        halpServer.runAsServer();
    }
}