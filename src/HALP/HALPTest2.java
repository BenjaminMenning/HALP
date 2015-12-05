/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HALP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 *
 * @author Ben
 */
public class HALPTest2 
{
    
    private String getIP()
    {
        // This try will give the Public IP Address of the Host.
        try
        {
            URL url = new URL("http://automation.whatismyip.com/n09230945.asp");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String ipAddress = new String();
            ipAddress = (in.readLine()).trim();
            /* IF not connected to internet, then
             * the above code will return one empty
             * String, we can check it's length and
             * if length is not greater than zero, 
             * then we can go for LAN IP or Local IP
             * or PRIVATE IP
             */
            if (!(ipAddress.length() > 0))
            {
                try
                {
                    InetAddress ip = InetAddress.getLocalHost();
                    System.out.println((ip.getHostAddress()).trim());
                    return ((ip.getHostAddress()).trim());
                }
                catch(Exception ex)
                {
                    return "ERROR";
                }
            }
            System.out.println("IP Address is : " + ipAddress);

            return (ipAddress);
        }
        catch(Exception e)
        {
            // This try will give the Private IP of the Host.
            try
            {
                InetAddress ip = InetAddress.getLocalHost();
                System.out.println((ip.getHostAddress()).trim());
                return ((ip.getHostAddress()).trim());
            }
            catch(Exception ex)
            {
                return "ERROR";
            }
        }
    }    
   public static void main(String[] args) throws SocketException, UnknownHostException, IOException
   {
       HALPTest2 halpTest2 = new HALPTest2();
       halpTest2.getIP();
//        String ipAddress = "";
//        try {
//            URL url = null;
//            BufferedReader in = null;
//            url = new URL("http://bot.whatismyipaddress.com");
//            in = new BufferedReader(new InputStreamReader(url.openStream()));
//            ipAddress = in.readLine().trim();
//            /* IF not connected to internet, then
//             * the above code will return one empty
//             * String, we can check it's length and
//             * if length is not greater than zero, 
//             * then we can go for LAN IP or Local IP
//             * or PRIVATE IP
//             */
//            if (!(ipAddress.length() > 0)) {
//                try {
//                    InetAddress ip = InetAddress.getLocalHost();
//                    System.out.println((ip.getHostAddress()).trim());
//                    ipAddress = (ip.getHostAddress()).trim();
//                } catch(Exception exp) {
//                    ipAddress = "ERROR";
//                }
//            }
//        } catch (Exception ex) {
//            // This try will give the Private IP of the Host.
//            try {
//                InetAddress ip = InetAddress.getLocalHost();
//                System.out.println((ip.getHostAddress()).trim());
//                ipAddress = (ip.getHostAddress()).trim();
//            } catch(Exception exp) {
//                ipAddress = "ERROR";
//            }
//            //ex.printStackTrace();
//        }
//        System.out.println("IP Address: " + ipAddress);   
   }
}
