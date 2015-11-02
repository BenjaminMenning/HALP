package HALP;

// Simple echo client.

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class HALPClient 
{
    private static final int SERVER_PORT = 54001;  
    private static final int IG_PORT = 54001;
        
    public static void main (String args[]) throws Exception 
    {	
        Scanner console = new Scanner(System.in);
        HALP halpClient = new HALP(IG_PORT, IG_PORT, SERVER_PORT);
        halpClient.runClient();
    }
}