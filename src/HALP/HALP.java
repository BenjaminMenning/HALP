/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HALP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static java.lang.System.console;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ben
 */
public abstract class HALP implements HALPInterface
{
    protected Scanner console = new Scanner(System.in);
    
    protected boolean connectionActive = false;
    
    // Hard coded IP addresses for testing
    protected String homeTestIP = "192.168.0."; // for testing at home
    protected String testIGIP = homeTestIP + "111";
    protected String testServIP = homeTestIP + "105";
    
    protected String clntIPAddr = "";
    protected String igIPAddr = "";
    protected String servIPAddr = "";
    
    protected InetAddress servINAddr;
    protected InetAddress igINAddr;
    
    protected int clntPortNum = 0;
    protected int igPortNum = 0;
    protected int servPortNum = 0;
    protected int errorRate = 0;
    
    protected int currMsgLen = 0;
    
    // Byte arrays for message fields
    protected byte[] destIPBytes = new byte[DESTIP_LEN];
    protected byte[] destPNBytes = new byte[DESTPN_LEN];
    protected byte[] crcBytes = new byte[CRC_LEN];
    protected byte[] seqBytes = new byte[SEQ_LEN];
    protected byte[] ackBytes = new byte[ACK_LEN];
    protected byte[] flagBytes = new byte[FLAG_LEN];
    protected byte[] rsvdBytes = new byte[RSVD_LEN];
    protected byte[] hedrBytes = new byte[HEDR_LEN];
    protected byte[] dtrtBytes = new byte[DTRT_LEN];
    protected byte[] fileBytes = new byte[10]; // placeholder value
    protected byte[] dataBytes = new byte[DTRT_LEN];
    protected byte[] currMsg;
    protected ArrayList<byte[]> messageQueue = new ArrayList<byte[]>();
    
    // Constants for header field lengths in bytes
    protected static final int DESTIP_LEN = 4;
    protected static final int DESTPN_LEN = 2;
    protected static final int CRC_LEN = 2;
    protected static final int SEQ_LEN = 4;
    protected static final int ACK_LEN = 4;
    protected static final int FLAG_LEN = 1;
    protected static final int RSVD_LEN = 3;
    protected static final int HEDR_LEN = 20;
    protected static final int DTRT_LEN = 2;

    // Constants for header field byte offsets
    protected static final int DESTIP_OFFSET = 0;
    protected static final int DESTPN_OFFSET = 4;
    protected static final int CRC_OFFSET = 6;
    protected static final int SEQ_OFFSET = 8;
    protected static final int ACK_OFFSET = 12;
    protected static final int FLAG_OFFSET = 16;
    protected static final int RSVD_OFFSET = 17; 
    protected static final int DATA_OFFSET = 20;
    protected static final int DTRT_OFFSET = 20; // data rate
    protected static final int FILE_OFFSET = 22;
    
    protected DatagramSocket clntSocket;
    protected DatagramSocket igSocket;
    protected DatagramSocket servSocket;

    
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
        try {
            igINAddr = InetAddress.getByName(igIPAddr);
        } catch (UnknownHostException ex) {
            Logger.getLogger(HALP.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            servINAddr = InetAddress.getByName(servIPAddr);
        } catch (UnknownHostException ex) {
            Logger.getLogger(HALP.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    public void setData() 
    {
        String dataStr = "Yoyoyoyoyo";
        dataBytes = dataStr.getBytes();
    }

    
    @Override
    public byte[] getHeader(byte[] messageBytes) {
        byte[] header = new byte[20];
        System.arraycopy(messageBytes, 0, header, 0, 20);
        return header;
    }

    @Override
    public byte[] getData(byte[] messageBytes) 
    {
        int originalLength = Array.getLength(messageBytes);
        byte [] data = new byte[originalLength];
        System.arraycopy(messageBytes, 20, data, 0, originalLength-20);
        return data;
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
    public boolean isFinFlagSet(byte[] headerBytes) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public void assembleMessage() 
    {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
            outputStream.write(destIPBytes);
            outputStream.write(destPNBytes);
            outputStream.write(crcBytes);
            outputStream.write(seqBytes);
            outputStream.write(ackBytes);
            outputStream.write(flagBytes);
            outputStream.write(rsvdBytes);
            outputStream.write(dtrtBytes);
            outputStream.write(dataBytes);
            currMsg = outputStream.toByteArray();
            currMsgLen = currMsg.length;
            messageQueue.add(currMsg);
        } catch (IOException ex) {
            Logger.getLogger(HALP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}