package HALP;

// Simple echo client.

import static HALP.HALP.FLAG_OFFSET;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HALPClient extends HALP implements HALPClientInterface
{
        // Hard coded IP addresses for testing
    protected String homeTestIP = "192.168.0."; // for testing at home
    protected String testIGIP = homeTestIP + "111";
    protected String testServIP = homeTestIP + "110";
    protected String testFileName = "alice.txt";
    protected boolean testIsUpload = true;
    protected int testDataRate = 5000;
    
    private static final int SERVER_PORT = 54001;  
    private static final int IG_PORT = 54001;
    protected String fileName = "";
    protected int dataRate = 0;
    private boolean isUpload = false;
    
    private File inputFile = null;
    private File outputFile = null;
    private FileInputStream fInStr = null;
    private FileOutputStream fOutStr = null;
    
    public HALPClient() throws SocketException
    {
        deviceSocket = new DatagramSocket();
    }
    
    public HALPClient(int igPN, int servPN) throws SocketException
    {
        igPortNum = igPN;
        servPortNum = servPN;
        deviceSocket = new DatagramSocket();
    }
    
    @Override
    public void inputIGIP() 
    {
        System.out.println("Please enter the internet gateway IP address: ");
        String igIPAddress = console.nextLine();
        setIGIP(igIPAddress);
    }

    @Override
    public void inputServIP() 
    {
        System.out.println("Please enter the server IP address: ");
        String servIPAddress = console.nextLine();
        setServerIP(servIPAddress);
    }
    
    @Override
    public void inputTransferDirection()
    {
        int trfrInput = 0;
        // Requests user to input transfer direction
        System.out.println("Please enter '1' to upload to the server, or '0' to"
                + " download from the server: ");
        trfrInput = console.nextInt();

        // Turns trace on or off based on user input
        if(trfrInput == 1)
        {
            isUpload = true;
        }
        else
        {
            isUpload = false;
        }
    }
   
    @Override
    public void inputFileName()
    {
        System.out.println("Please enter the name of the file you wish to "
                + "download / upload: ");
        String tempFileName = console.nextLine();
//        setFileName(tempFileName);
        fileName = tempFileName;
    }
    
    @Override
    public void inputDataRate()
    {
        // Requests user to input transfer direction
        System.out.println("Please enter the preferred data rate for the "
                + "connection: ");
        dataRate = console.nextInt();
    }
    
    @Override
    public void setTransferDirection(boolean trfrDir)
    {
        isUpload = trfrDir;
    }
    
    @Override
    public void setFileName(String fileNameStr)
    {
        fileName = fileNameStr;
    }
    
    @Override
    public void setDataRate(int rate)
    {
        dataRate = rate;
    }
    
    @Override
    public byte[] setFileNameField(byte[] messageBytes, String fileNameStr)
    {
        byte tempMsgBytes[] = messageBytes;
        byte tempFlNmBytes[] = fileNameStr.getBytes();
        int fileNameLen = fileNameStr.length();
        System.arraycopy(tempFlNmBytes, 0, tempMsgBytes, FILE_OFFSET, 
                fileNameLen);   
        return tempMsgBytes;
    }
           
    
    @Override
    public byte[] setDestIP(byte[] headerBytes, String destIP)
    {
        // for future use?
//        byte tempHdrBytes[] = new byte[HEDR_LEN];
//        tempHdrBytes = headerBytes;
        
        byte tempHdrBytes[] = headerBytes;
        byte tempIPBytes[] = new byte[DESTIP_LEN];
        tempIPBytes = convertIPToBytes(destIP);
//        tempIPBytes = destIN.getAddress();
//        tempHdrBytes[DESTIP_OFFSET] = tempIPBytes;
        System.arraycopy(tempIPBytes, 0, tempHdrBytes, DESTIP_OFFSET, 
                DESTIP_LEN);   
        return tempHdrBytes;
    }
    
    @Override
    public byte[] setDestPN(byte[] headerBytes, int portNum)
    {
        byte tempHdrBytes[] = headerBytes;
        byte tempPNBytes[] = new byte[DESTPN_LEN];
        tempPNBytes = convertPNToBytes(portNum);
//        tempHdrBytes[DESTIP_OFFSET] = tempIPBytes;
        System.arraycopy(tempPNBytes, 0, tempHdrBytes, DESTPN_OFFSET, 
                DESTPN_LEN);   
        return tempHdrBytes;
    }

    @Override
    public byte[] convertIPToBytes(String ipAddr) 
    {
        try {
            InetAddress testINAddr = InetAddress.getByName(ipAddr);
            byte[] tempIPBytes = testINAddr.getAddress();
            return tempIPBytes;
        } catch (UnknownHostException ex) {
            Logger.getLogger(HALPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] blankBytes = new byte[4];
        return blankBytes;
    }
    
    @Override
    public void initiateConnection() 
    {
        try 
        {
            System.out.println("Client has requested a connection.");
            byte[] tempHeader = new byte[HEDR_LEN];
            byte[] tempData = null; // change later
            byte[] tempMsg = null;

            // User input
    //        inputIGIP();
    //        inputServIP();

            // Hard coded values for testing
            setIGIP(testIGIP);
            setServerIP(testServIP);
            setFileName(testFileName);
            setTransferDirection(testIsUpload);
            setDataRate(testDataRate);
            msgSize = HEDR_LEN + testDataRate;

            tempHeader = setDestIP(tempHeader, servIPAddr);
            tempHeader = setDestPN(tempHeader, servPortNum);
            tempHeader = setSYNFlag(tempHeader, true);
            tempHeader = setDRTFlag(tempHeader, testIsUpload);
            printMessage(tempHeader);

            int dataLen = fileName.length() + DTRT_LEN;
            tempData = new byte[dataLen];
            System.out.print("Length of data field: " + dataLen + "\n");
            tempMsg = assembleMessage(tempHeader, tempData);
            tempMsg = setFileNameField(tempMsg, fileName);
            tempMsg = setDataRateField(tempMsg, dataRate);
            printFileNameField(tempMsg);
            printDataRateField(tempMsg);
            System.out.println();

            sendMessage(tempMsg);
            byte[] rcvdMsg = receiveMessage();
            boolean isSyn = isSYNFlagSet(rcvdMsg);
            boolean isAck = isACKFlagSet(rcvdMsg);
            boolean isDrt = isDRTFlagSet(rcvdMsg);
            
            // Sets data rate based on what IG places in data rate field
            int igDataRate = getDataRateField(rcvdMsg);
            setDataRate(igDataRate);
            msgSize = HEDR_LEN + igDataRate;
            
            if(isSyn && isAck && isUpload)
            {
                runAsSender();
            }
            else if(isSyn && isAck && !isUpload)
            {
                runAsReceiver();
            }
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(HALP.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            closeConnection();
        }
    }
    
    @Override
    public void run()
    {
        
    }
    
    @Override
    public void runAsServer() 
    {
        msgSize = 65000;
        // Added for new server implementation
        try {
            deviceSocket = new DatagramSocket(servPortNum);
        } catch (SocketException ex) {
            Logger.getLogger(HALPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        boolean placeholderCondition = false;
        System.out.println("Server has started.");
        while(placeholderCondition == false)
        {
            try {
                byte[] rcvdMsg = receiveMessage();
                printDataRateField(rcvdMsg);
                fileName = getFileNameField(rcvdMsg);
                dataRate = getDataRateField(rcvdMsg);
                msgSize = HEDR_LEN + dataRate;
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
                    runAsReceiver();
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
    
    @Override
    public void runAsSender() throws FileNotFoundException, IOException, Exception 
    {
        System.out.println("Begin sending data");
        inputFile = new File(fileName);
        fInStr = new FileInputStream(fileName);
        
        byte[] tempHeader = new byte[HEDR_LEN];
        byte[] tempData = new byte[dataRate]; // change later
        byte[] tempMsg = null;
        while(fInStr.available() != 0)
        {
            tempData = new byte[dataRate];
            tempHeader = setDestIP(tempHeader, servIPAddr);
            tempHeader = setDestPN(tempHeader, servPortNum);
//            tempHeader = setDRTFlag(tempHeader, true);
//            printMessage(tempHeader);
            fInStr.read(tempData, 0, dataRate);
            tempMsg = assembleMessage(tempHeader, tempData);
            
            boolean isAck = false;
            while(!isAck)
            {
                Thread.sleep(1000);
                sendMessage(tempMsg);
                byte[] rcvdMsg = receiveMessage();
                isAck = isACKFlagSet(rcvdMsg);
            }
        }
        tempHeader = setFINFlag(tempHeader, true);
        tempData = new byte[1];
        tempMsg = assembleMessage(tempHeader, tempData);
        sendMessage(tempMsg);
        
        fInStr.close();
        closeConnection();
    }

    @Override
    public void runAsReceiver() throws FileNotFoundException, IOException, Exception 
    {
        System.out.println("Begin receiving data");
        boolean isFin = false;
        String desktopStr = System.getProperty("user.home") + "/Desktop/";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        String strDate = sdf.format(now);
//        File desktopFile = new File(desktopStr + fileName);
        
        fileName = strDate + "-" + fileName;
        outputFile = new File(desktopStr + fileName);
        fOutStr = new FileOutputStream(outputFile);
        
        byte[] tempHeader = new byte[HEDR_LEN];
        byte[] tempData = new byte[1]; // change later
        byte[] rcvdData = null;
        byte[] tempMsg = null;
        while(isFin == false)
        {
            byte[] rcvdMsg = receiveMessage();
            isFin = isFINFlagSet(rcvdMsg);
//            tempHeader = getHeader(rcvdMsg);
            rcvdData = getData(rcvdMsg);
            fOutStr.write(rcvdData);
            
            tempHeader = setACKFlag(tempHeader, true);
            tempMsg = assembleMessage(tempHeader, tempData);
            sendMessage(tempMsg);
        }
        fOutStr.close();
        closeConnection();
    }

    public static void main (String args[]) throws Exception 
    {	
        Scanner console = new Scanner(System.in);
        HALPClient halpClient = new HALPClient(IG_PORT, SERVER_PORT);
        halpClient.initiateConnection();
    }
}