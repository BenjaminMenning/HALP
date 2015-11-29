package HALP;

// Simple echo server.  Modified from some example on the Internet.

import java.io.*;
import java.net.*;
import java.lang.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HALPServer extends HALPClient
{
    static DatagramSocket serverSocket;

    private static final int SERVER_PORT = 43000;
    
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

    public HALPServer() throws SocketException
    {
        deviceSocket = new DatagramSocket();
    }
    
    public HALPServer(int servPN) throws SocketException
    {
        servPortNum = servPN;
        deviceSocket = new DatagramSocket(servPN);
    }
    
    @Override
    public void run() 
    {
        boolean placeholderCondition = false;
        System.out.println("Server has started.");
        while(placeholderCondition == false)
        {
            try {
                byte[] rcvdMsg = receiveMessage();
                printDataRateField(rcvdMsg);
                fileName = getFileNameField(rcvdMsg);
                dataRate = getDataRateField(rcvdMsg);
                boolean isSyn = isSYNFlagSet(rcvdMsg);
                boolean isAck = isACKFlagSet(rcvdMsg);
                boolean isUpld = isDRTFlagSet(rcvdMsg);
                if(isSyn && !isAck) 
                {
                    rcvdMsg = setACKFlag(rcvdMsg, true);
                    
                    // Retrieves the outgoing connection info from the datagram
                    igIPAddr = currDtgm.getAddress().getHostAddress();
                    igINAddr = InetAddress.getByName(igIPAddr);
                    igPortNum = currDtgm.getPort();
                }
                sendMessage(rcvdMsg);
                if(isUpld)
                {
                    runAsReceiver(rcvdMsg); // Received message is placeholder
                }
                else
                {
                    runAsSender();
                }
                placeholderCondition = true;
            } 
            catch (Exception ex)
            {
                Logger.getLogger(HALPIG.class.getName()).log(Level.SEVERE, null,
                        ex);
            }
        }
    }
}