/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HALP;

import static java.lang.System.console;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Ben
 */
public class HALP implements HALPInterface
{
    private Scanner console = new Scanner(System.in);
    
    private String clntIPAddr = "";
    private String igIPAddr = "";
    private String servIPAddr = "";
    
    private int clntPortNum = 0;
    private int igPortNum = 0;
    private int servPortNum = 0;
    private int errorRate = 0;
    
    // Constants for header field lengths in bytes
    private static final int DESTIP_LEN = 4;
    private static final int DESTPN_LEN = 2;
    private static final int CRC_LEN = 2;
    private static final int SEQ_LEN = 4;
    private static final int ACK_LEN = 4;
    private static final int FLAG_LEN = 1;
    private static final int RSVD_LEN = 3;
    private static final int HEDR_LEN = 20;
    private static final int DTRT_LEN = 2;

    // Constants for header field byte offsets
    private static final int DESTIP_OFFSET = 0;
    private static final int DESTPN_OFFSET = 4;
    private static final int CRC_OFFSET = 6;
    private static final int SEQ_OFFSET = 8;
    private static final int ACK_OFFSET = 12;
    private static final int FLAG_OFFSET = 16;
    private static final int RSVD_OFFSET = 17; 
    private static final int DATA_OFFSET = 20;
    private static final int DTRT_OFFSET = 20; // data rate
    private static final int FILE_OFFSET = 22;
    
    private DatagramSocket clntSocket;
    private DatagramSocket igSocket;
    private DatagramSocket servSocket;

    public HALP() throws SocketException
    {
        clntSocket = new DatagramSocket();
        igSocket = new DatagramSocket();
        servSocket = new DatagramSocket();
    }
    
    public HALP(int clntPN, int igPN, int servPN) throws SocketException
    {
        clntPortNum = clntPN;
        igPortNum = igPN;
        servPortNum = servPN;
        clntSocket = new DatagramSocket();
        igSocket = new DatagramSocket();
        servSocket = new DatagramSocket();
    }
    
    @Override
    public void setClientIP(String ipAddr) 
    {
        clntIPAddr = ipAddr;
    }

    @Override
    public void setClientPort(int portNum) 
    {
        clntPortNum = portNum;
    }

    @Override
    public String getClientIP() 
    {
        return clntIPAddr;
    }

    @Override
    public int getClientPort() 
    {
        return clntPortNum;
    }

    @Override
    public void setIGIP(String ipAddr) 
    {
        igIPAddr = ipAddr;
    }

    @Override
    public void setIGPort(int portNum) 
    {
        igPortNum = portNum;
    }

    @Override
    public String getIGIP() 
    {
        return igIPAddr;
    }

    @Override
    public int getIGPort() 
    {
        return igPortNum;
    }

    @Override
    public void setServerIP(String ipAddr) 
    {
        servIPAddr = ipAddr;
    }

    @Override
    public void setServerPort(int portNum) 
    {
        servPortNum = portNum;
    }

    @Override
    public String getServerIP() 
    {
        return servIPAddr;
    }

    @Override
    public int getServerPort() 
    {
        return servPortNum;
    }
    
    @Override
    public byte[] getHeader(byte[] messageBytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] getData(byte[] messageBytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getDestinationIP(byte[] messageBytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getDestinationPort(byte[] messageBytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isChecksumValid(byte[] headerBytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRSTFlagSet(byte[] headerBytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isDRTFlagSet(byte[] headerBytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isAckFlagSet(byte[] headerBytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isSynFlagSet(byte[] headerBytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isFinFlagSet(byte[] headerBytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean errorGenerator()
    {
        Random random = new Random();
        int chanceMax = 100;
        int randomChance = random.nextInt(chanceMax) + 1;
        if(randomChance <= errorRate)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int randomIndex()
    {
        Random random = new Random();
        int indexMax = 18;
        int randomIndex = random.nextInt(indexMax);
        return randomIndex;
    }
    
    @Override
    public byte generateByteError(byte oldByte)
    {
        Random random = new Random();
        int bitMax = 7;
        int randomBit = random.nextInt(bitMax);
        byte errorByte = oldByte;
        errorByte ^= 1 << randomBit; 
//        System.out.println(errorByte);
        return errorByte;
    }
    
    @Override
    public int errorNumber()
    {
        int errorNum;
        Random random = new Random();
        int chanceMax = 100;
        int randomChance = random.nextInt(chanceMax) + 1;
        if(randomChance <= 70)
        {
            errorNum = 1;
            return errorNum;
        }
        else
        {
            errorNum = 2;
            return errorNum;
        }
    }
    
    @Override
    public void setErrorRate(int rate)
    {
        errorRate = rate;
    }
    
    @Override
    public int getErrorRate()
    {
        return errorRate;
    }

    @Override
    public void clntInputIGIP() 
    {
        System.out.println("Please enter the internet gateway IP address: ");
        String igIPAddress = console.nextLine();
        setIGIP(igIPAddress);
    }

    @Override
    public void clntInputServIP() 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}