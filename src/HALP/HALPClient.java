package HALP;

/** 
 * Author:          Benjamin Menning, John Blacketer
 * 
 * Date:            12/15/2015 
 *                
 * Course:          CS 413 - Advanced Networking
 * 
 * Assignment:      Final Project - HALP Protocol
 * 
 * Description:     This program is a program that performs a simple file 
 *                  transfer utilizing our own protocol, HALP. It includes 
 *                  three devices: a client, an internet gateway (IG), and a
 *                  server. The client initiates a file download or upload from
 *                  or to the server, and the file transfer process begins, 
 *                  while the internet gateway passes messages between them. 
 *                  It follows our protocol to provide reliability for the data
 *                  transfer process. All three devices follow the protocol to
 *                  manipulate the header data fields and can print out 
 *                  information to trace and log the connection process. Our 
 *                  program / protocol utilizes use of positive acknowledgment,
 *                  retransmission, timeout, and sequence numbers to provide 
 *                  reliability. 
 * 
 */

import static HALP.HALP.FLAG_OFFSET;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.util.Duration.millis;

/** 
 * This class implements methods and functionality from the HALPClientInterface.
 * It is not only for a client, but can be run as either a
 * client or a server. It defines methods for inputting necessary client 
 * information like file name, data rate, as well as methods for running the
 * device as a client or a server.
 * 
 * @author Benjamin Menning, John Blacketer
 * @version 12/15/2015
*/
public class HALPClient extends HALP implements HALPClientInterface
{    
    private static final int SERVER_PORT = 43124;  
    private static final int IG_PORT = 43124;
    
    protected String fileName = "";
    protected int dataRate = 0;
    private boolean isUpload = false;
    protected boolean isClient;
    
    protected long seqNum = 0;
    protected long otherSeqNum = 0;
    protected long ackNum = 0;
    protected long otherAckNum = 0;
    
    private File inputFile = null;
    private File outputFile = null;
    private FileInputStream fInStr = null;
    private FileOutputStream fOutStr = null;
                
    public static void main (String args[]) throws Exception 
    {	
        Scanner console = new Scanner(System.in);
        HALPClient halpClient = new HALPClient(IG_PORT, SERVER_PORT);
        
        // Hard coded IP addresses for testing
//        String homeTestIP = "192.168.0."; // for testing at home
////        String testIGIP = homeTestIP + "110"; // for manual entry
//        String testIGIP = halpClient.getLocalIP(); // for automatic entry
//        String testServIP = homeTestIP + "105";
//        String testFile1 = "alice.txt";
//        String testFile2 = "mission0.txt";
//        String testFile3 = "mission1.txt";
//        String testFile4 = "mission2.txt";
//        String testFile5 = "allan.txt";
//        String testFile6 = "ebZqtsI.gif";
//        String testFile7 = "4g9ttdk.gif";
//        String testFile8 = "cCPh3fg.jpg";
//        String testFileName = testFile4; // swap out test files here
//        boolean testIsUpload = true;
//        boolean testIsTrace = true;
//        int testDataRate = 32;
        
        // Hard coded input for testing
//        halpClient.setIGIP(testIGIP);
//        halpClient.setServerIP(testServIP);
//        halpClient.setTransferDirection(testIsUpload);
//        halpClient.setFileName(testFileName);
//        halpClient.setDataRate(testDataRate);
//        halpClient.setTrace(true);
        
        // For user input
        halpClient.inputFileName();
        halpClient.inputIGIP();
        halpClient.inputServIP();
        halpClient.inputIGPortNum();
        halpClient.inputSrvPortNum();
        halpClient.inputTransferDirection();
        halpClient.inputDataRate();
        halpClient.inputTrace();
                
        halpClient.initiateConnection();
    }

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
        fileName = tempFileName;
    }
    
    @Override
    public void inputDataRate()
    {
        // Requests user to input transfer direction
        System.out.println("Please enter the preferred data rate for the "
                + "connection (in up to 65,535 bytes): ");
        dataRate = console.nextInt();
    }
    
    @Override
    public void inputIGPortNum()
    {
        // Requests user to input transfer direction
        System.out.println("Please enter the port number for the IG: ");
        int tempPortNum = console.nextInt();
        igPortNum = tempPortNum;
    }

    @Override
    public void inputSrvPortNum()
    {
        // Requests user to input transfer direction
        System.out.println("Please enter the port number for the server: ");
        int tempPortNum = console.nextInt();
        servPortNum = tempPortNum;
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
    
    public boolean isSeqNumValid(long seqNum)
    {
//        byte[] tempMsgBytes = messageBytes;
        long tempSeqNum = seqNum;
        if(ackNum == tempSeqNum)
        {
            return true;
        }
        else
        {
            return false;
        }
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
        byte tempHdrBytes[] = headerBytes;
        byte tempIPBytes[] = new byte[DESTIP_LEN];
        tempIPBytes = convertIPToBytes(destIP);
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
    public void printTraceStats() 
    {
        fileTransTime = stop - start;
        String timeStr = String.format("%02d:%02d:%02d.%03d", TimeUnit.MILLISECONDS.toHours(fileTransTime),
        TimeUnit.MILLISECONDS.toMinutes(fileTransTime) % TimeUnit.HOURS.toMinutes(1),
        TimeUnit.MILLISECONDS.toSeconds(fileTransTime) % TimeUnit.MINUTES.toSeconds(1),
        fileTransTime % 1000);        
        if(!isMaxTransmissionsEmpty())
        {
            maxRetrans = getMaxTransmission();
        }
        totalRetrans = getTotalRetransmissions();
        expectedRetrans = getExpectedRetransmissions();
        percentRetrans = getPercentageOfRetransmissions();
        
        String statsStr = "\nStatistics: ";
        String fileSizeInfo = "\nSize of file transferred: " + fileSize 
                + " bytes";
        String fileTimeInfo = "\nTime to complete file transfer: " + timeStr;
        String msgGenInfo = "\nTotal number of application messages generated: "
                + msgGenNum;
        String dtgmTransInfo = "\nTotal number of UDP datagrams transmitted: " 
                + dtgmTransNum;
        String totalRetransInfo = "\nTotal number of retransmissions: " 
                + totalRetrans;
        String expecRetransInfo = "\nExpected number of retransmissions: " 
                + expectedRetrans;
        String maxRetranInfo = "\nMaximum number of transmissions for any "
                + "single application datagram: " + maxRetrans;
        String prcRetransInfo = "\nPercentage of retransmissions: " + 
                String.format("%.2f", percentRetrans) + "%";
        
        if(isTraceOn)
        {
            deviceLog.println(statsStr);
            deviceLog.println(fileSizeInfo);
            deviceLog.println(fileTimeInfo);
            deviceLog.println(msgGenInfo);
            deviceLog.println(dtgmTransInfo);
            deviceLog.println(totalRetransInfo);
            deviceLog.println(expecRetransInfo);
            deviceLog.println(maxRetranInfo);
            deviceLog.println(prcRetransInfo);
        }
        statsStr += fileSizeInfo + fileTimeInfo + msgGenInfo + dtgmTransInfo + 
                totalRetransInfo + expecRetransInfo + maxRetranInfo + 
                prcRetransInfo;
        
        System.out.println(statsStr);
        
    }
    
    @Override
    public void initiateConnection() 
    {

        isClient = true;
        try 
        {
            if(isTraceOn)
            {
                //creating log for Server
                String desktopStr = System.getProperty("user.home") + "/Desktop/";
                String deviceStr = "Client's Log.txt";
                File deviceFile = new File(desktopStr + deviceStr);
                deviceLog = new PrintWriter(deviceFile);
                deviceLog.println("Client's Log: \n");
            }
            
            deviceSocket.setSoTimeout(retransTO);
            System.out.println("Client has requested a connection.");
            if(isTraceOn)
            {
                deviceLog.println("Client has requested a connection.");
            }
            
            // Initialize message variables
            byte[] tempHeader = new byte[HEDR_LEN];
            byte[] tempData = null; // change later
            byte[] tempMsg = null;
            byte[] rcvdMsg = null;
            
            // Initialize flags
            boolean isAck = false;
            boolean isSyn = false;
            boolean isDrt = false;
            boolean isChkValid = false;
            boolean isTimedOut = false;
            seqNum = generateSequenceNumber();

            msgSize = HEDR_LEN + dataRate;

            // Create header
            tempHeader = setDestIP(tempHeader, servIPAddr);
            tempHeader = setDestPN(tempHeader, servPortNum);
            tempHeader = setSequenceNumber(tempHeader, seqNum);
            tempHeader = setAcknowledgmentNumber(tempHeader, ackNum);
            tempHeader = setDRTFlag(tempHeader, isUpload);
            tempHeader = setSYNFlag(tempHeader, true);

            // Set data fields and checksum
            int dataLen = fileName.length() + DTRT_LEN;
            tempData = new byte[dataLen];
            tempMsg = assembleMessage(tempHeader, tempData);
            tempMsg = setFileNameField(tempMsg, fileName);
            tempMsg = setDataRateField(tempMsg, dataRate);
            tempMsg = setChecksum(tempMsg);

            // Continue resending message until SYN and ACK flags are set
            // and checksum is valid
            while(!isSyn || !isAck || !isChkValid || isTimedOut) // Change isTimedOut later..?
            {
                try 
                {
                    isTimedOut = false;
                    // Sends message and prints out other fields
                    sendMessage(tempMsg);
                    if(isTraceOn)
                    {
                        printFileNameField(tempMsg);
                        printDataRateField(tempMsg);
                    }

                    // Receives message and checks if flags are set and checksum 
                    // is valid
                    rcvdMsg = receiveMessage();
                    isSyn = isSYNFlagSet(rcvdMsg);
                    isAck = isACKFlagSet(rcvdMsg);
                    isChkValid = isChecksumValid(rcvdMsg);
                } 
                catch (SocketTimeoutException e) 
                {
                        if(isTraceOn)
                        {
                            System.out.println("Connection has timed out.");
                            deviceLog.println("Connection has timed out.");
                        }
                        isTimedOut = true;
                }
            }
            
            // Sets data rate based on what IG places in data rate field
            int igDataRate = getDataRateField(rcvdMsg);
            setDataRate(igDataRate);
            if(isTraceOn)
            {
                System.out.println(dataRate);
                deviceLog.println(dataRate);
            }
            msgSize = HEDR_LEN + igDataRate;
            
            ackNum = getSequenceNumber(rcvdMsg);
//            ackNum = otherSeqNum;
            ackNum = incrementSequence(ackNum);
            seqNum = incrementSequence(seqNum);
            
            // If the transfer is a upload, then run as sender
            if(isUpload)
            {
                runAsSender();
            }
            
            // If the transfer is a download, keep sending ACK and then run as
            // receiver
            else if(!isUpload)
            {
                // Create header
                tempHeader = setDestIP(tempHeader, servIPAddr);
                tempHeader = setDestPN(tempHeader, servPortNum);
                tempHeader = setSequenceNumber(tempHeader, seqNum);
                tempHeader = setAcknowledgmentNumber(tempHeader, ackNum);
                tempHeader = setDRTFlag(tempHeader, isUpload);
                tempHeader = setACKFlag(tempHeader, true);
                tempHeader = setSYNFlag(tempHeader, true);

                // Set data fields and checksum
                tempData = new byte[1];
                tempMsg = assembleMessage(tempHeader, tempData);
                tempMsg = setChecksum(tempMsg);
                isSyn = false;
                isAck = false;
                isChkValid = false;
                isTimedOut = false;

                
                // Uncommenting the isSyn made this work for some reason
//                while(isSyn && !isAck)
//                while(!isAck && !isChkValid || isTimedOut)
                while(!isAck || !isChkValid || isTimedOut)
                {
                    try
                    {
                        isTimedOut = false;
                        sendMessage(tempMsg);
                        rcvdMsg = receiveMessage();
                        isSyn = isSYNFlagSet(rcvdMsg);
                        isAck = isACKFlagSet(rcvdMsg);
                        isChkValid = isChecksumValid(rcvdMsg);
                    } 
                    catch (SocketTimeoutException e) 
                    {
                        if(isTraceOn)
                        {
                            System.out.println("Connection has timed out.");
                            deviceLog.println("Connection has timed out.");
                        }
                        isTimedOut = true;
                    }
                }
                
                runAsReceiver(rcvdMsg);
            }
            if(isTraceOn)
            {
                deviceLog.close();
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
    public void runAsServer() 
    {
        isClient = false;
        // Sets default message size
        msgSize = 65000;
        
        // Initialize message variables
        byte[] tempHeader = new byte[HEDR_LEN];
        byte[] tempData = new byte[1]; // change later
        byte[] tempMsg = null;
        byte[] rcvdMsg = null;
                
        // Initialize flag variables
        boolean isAck = false;
        boolean isSyn = false;
        boolean isUpld = false;
        boolean isChkValid = false;
        seqNum = generateSequenceNumber();
        
        // Added for new server implementation
        try 
        {
            if(isTraceOn)
            {
                //creating log for Server
                String desktopStr = System.getProperty("user.home") + "/Desktop/";
                String deviceStr = "Server's Log.txt";
                File deviceFile = new File(desktopStr + deviceStr);
                deviceLog = new PrintWriter(deviceFile);
                deviceLog.println("Server's Log: \n");
            }
            
            deviceSocket = new DatagramSocket(servPortNum);
        } 
        catch (SocketException ex) 
        {
            Logger.getLogger(HALPClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HALPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        boolean transferOver = false;
        System.out.println("Server has started.");
        if(isTraceOn)
        {
            deviceLog.println("Server has started.");
        }
        while(transferOver == false)
        {
            isChkValid = false;
            try 
            {
                // Keep sending negative acknowledgment until checksum is valid
                // and SYN flag is set
                while(!isChkValid)
                {
                    // Receive message
                    rcvdMsg = receiveMessage();
                    
                    // Retrieves the outgoing connection info from the datagram
                    igIPAddr = currDtgm.getAddress().getHostAddress();
                    igINAddr = InetAddress.getByName(igIPAddr);
                    igPortNum = currDtgm.getPort();
                    
                    // Retrieve checksum
                    isChkValid = isChecksumValid(rcvdMsg);
                    if(isChkValid)
                        break;
                    
                    // Create header
                    tempHeader = setACKFlag(tempHeader, isChkValid);
                    tempHeader = setSYNFlag(tempHeader, true);
                    tempHeader = setSequenceNumber(tempHeader, 0);
                    tempHeader = setAcknowledgmentNumber(tempHeader, 0);
                    
                    // Create message to send to client
                    tempMsg = assembleMessage(tempHeader, tempData);
                    tempMsg = setChecksum(tempMsg);
                    sendMessage(tempMsg);
                }
                     
                // Retrieve flags
                isAck = isACKFlagSet(rcvdMsg);
                isSyn = isSYNFlagSet(rcvdMsg);
                isUpld = isDRTFlagSet(rcvdMsg);
                    
                if(!isAck && isSyn)
                {
                    // Retrieve file name, data rate, and determine message size
                    fileName = getFileNameField(rcvdMsg);
                    dataRate = getDataRateField(rcvdMsg);
//                    msgSize = HEDR_LEN + dataRate;
                    otherSeqNum = getSequenceNumber(rcvdMsg);
                    ackNum = incrementSequence(otherSeqNum);

                    // Re-initialize data to account for data rate field
                    tempData = new byte[2];

                    // Create header
                    tempHeader = setACKFlag(tempHeader, true);
                    tempHeader = setSYNFlag(tempHeader, true);
                    tempHeader = setSequenceNumber(tempHeader, seqNum);
                    tempHeader = setAcknowledgmentNumber(tempHeader, ackNum);

                    // Create message to send to client
                    tempMsg = assembleMessage(tempHeader, tempData);
                    tempMsg = setDataRateField(tempMsg, dataRate);
                    tempMsg = setChecksum(tempMsg);
                    sendMessage(tempMsg);
                    if(isTraceOn)
                    {
                        printFileNameField(tempMsg);
                        printDataRateField(tempMsg);
                    }
                }
                // If ACK is set and DRT is an upload
                else if(isAck && isUpld)
                {
                    isUpload = isUpld;
                    runAsReceiver(rcvdMsg);
                    transferOver = true;
                }
                // If ACK is set and DRT is a download
                else if(isSyn && isAck && !isUpld)
                {
                    ackNum = incrementSequence(ackNum);
                    seqNum = incrementSequence(seqNum);
                    isUpload = isUpld;
                    runAsSender();
                    transferOver = true;
                }
            } 
            catch (Exception ex)
            {
                Logger.getLogger(HALPIG.class.getName()).log(Level.SEVERE, null,
                        ex);
            }
        }
        if(isTraceOn)
        {
            deviceLog.close();
        }
    }
    
    @Override
    public void runAsSender() throws FileNotFoundException, IOException, Exception 
    {
        deviceSocket.setSoTimeout(retransTO);
        System.out.println("Begin sending data");
        if(isTraceOn)
        {
            deviceLog.println("Begin sending data");
        }
        start = System.currentTimeMillis();
        
        // Create input file and start file input stream
        inputFile = new File(fileName);
        fileSize = inputFile.length();
        fInStr = new FileInputStream(fileName);
        
        // Initialize message variables
        byte[] tempHeader = new byte[HEDR_LEN];
        byte[] tempData = new byte[dataRate]; // change later
        byte[] tempMsg = null;
        byte[] rcvdMsg = null;
        
        // Initialize flag variables
        boolean isAck = false;
        boolean isSyn = false;
        boolean isChkValid = false;
        boolean isTimedOut = false;
        
        int remainingChar = 0;
        int tempDataRate = 0;
        
        // Sends messages until the end of the file has been reached
        while(fInStr.available() != 0)
        {
            retransCount  = 0;
            // Assign data rate to temporary value
            tempDataRate = dataRate;
            
            // Determine amount of remaining characters in file
            remainingChar = fInStr.available();
            
            // If the amount is less than the data rate, make the data the size
            // of the remaining characters
            if(remainingChar < dataRate)
            {
                tempDataRate = remainingChar;
                if(isTraceOn)
                {
                    System.out.println(tempDataRate);
                    deviceLog.println(tempDataRate);
                }
            }
            
            // Create message
            tempData = new byte[tempDataRate];
            tempHeader = setSequenceNumber(tempHeader, seqNum);
            tempHeader = setAcknowledgmentNumber(tempHeader, ackNum);
            tempHeader = setDRTFlag(tempHeader, isUpload);
            tempHeader = setACKFlag(tempHeader, true);
            
            fInStr.read(tempData, 0, tempDataRate);
            msgGenNum++;
            msgGenNum2++;
            tempMsg = assembleMessage(tempHeader, tempData);
            tempMsg = setChecksum(tempMsg);
            
            // Reset flags
            isSyn = false;
            isAck = false;
            isChkValid = false;
            isTimedOut = false;
            
            // Resend message if acknowledgment is negative or checksum invalid
//            while(!isAck || !isChkValid || isTimedOut)
            while(isSyn || !isAck || !isChkValid || isTimedOut)
            {
                try 
                {
                    isTimedOut = false;
                    Thread.sleep(transDelay);
                    sendMessage(tempMsg);
                    dtgmTransNum++;
                    retransCount++;
//                    senderLog.println(resendLog(tempMsg)); //writes to the sender log information about the message that was resent
                    rcvdMsg = receiveMessage();
                    isChkValid = isChecksumValid(rcvdMsg);
                    isSyn = isSYNFlagSet(rcvdMsg);
                    isAck = isACKFlagSet(rcvdMsg);
                    
                } 
                catch (SocketTimeoutException e) 
                {
                    if(isTraceOn)
                    {
                        System.out.println("Connection has timed out.");
                        deviceLog.println("Connection has timed out.");
                    }
                    isTimedOut = true;
                }
            }
            
            retransCount--;
            if(retransCount > 0)
            {
                addMaxTransmission(retransCount);
            }
            ackNum = incrementSequence(ackNum);
            seqNum = incrementSequence(seqNum);
        }
        
        // Create message
        tempHeader = setSequenceNumber(tempHeader, seqNum);
        tempHeader = setAcknowledgmentNumber(tempHeader, ackNum);
        tempHeader = setDRTFlag(tempHeader, isUpload);
        tempHeader = setACKFlag(tempHeader, true);
        tempHeader = setFINFlag(tempHeader, true);
        tempData = new byte[1];
        tempMsg = assembleMessage(tempHeader, tempData);
        tempMsg = setChecksum(tempMsg);
            
        // Reset flags
        isAck = false;
        isChkValid = false;
        isTimedOut = false;
        
        // Resend message if acknowledgment is negative or checksum invalid
//        while(!isAck || !isChkValid)
        while(!isAck || !isChkValid || isTimedOut)
        {
            try 
            {
                isTimedOut = false;
                Thread.sleep(transDelay);
                sendMessage(tempMsg);
                rcvdMsg = receiveMessage();
                isChkValid = isChecksumValid(rcvdMsg);
                isAck = isACKFlagSet(rcvdMsg);
            } 
            catch (SocketTimeoutException e) 
            {
                if(isTraceOn)
                {
                    System.out.println("Connection has timed out.");
                    deviceLog.println("Connection has timed out.");
                }
                isTimedOut = true;
            }
        }
        
        // Close file input stream and connection
        stop = System.currentTimeMillis();
        fInStr.close();
        closeConnection();
        printTraceStats();
    }

    @Override
    public void runAsReceiver(byte[] firstMsg) throws FileNotFoundException, 
            IOException, Exception 
    {
        deviceSocket.setSoTimeout(0);
        System.out.println("Begin receiving data");
        if(isTraceOn)
        {
            deviceLog.println("Begin receiving data");
        }
        
        // Initialize flag variables
        boolean isFin = false;
        boolean isFinValid = false;
        boolean isFirst = true;
        boolean isChkValid = false;
        boolean isAck = false;
        boolean isSeqValid = false;
        boolean isWritten = false; // whether or not the data has been written
        
        long tempSeqNum = 0;
        long tempAckNum = 0;
        
        // Initialize file location and set file name
        String desktopStr = System.getProperty("user.home") + "/Desktop/";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date now = new Date();
        String strDate = sdf.format(now);
        fileName = strDate + "-" + fileName;
        outputFile = new File(desktopStr + fileName);
        fOutStr = new FileOutputStream(outputFile);
        
        // Initialize message variables
        byte[] tempHeader = new byte[HEDR_LEN];
        byte[] tempData = new byte[1]; // change later
        byte[] rcvdData = null;
        byte[] savedData = null; // valid data to be saved
        byte[] tempMsg = null;
        byte[] rcvdMsg = null;
        
        // Run until end of data stream is reached
        while(!isFinValid)
        {
            // Wait for received message if not the first
            if(!isFirst)
            {
                rcvdMsg = receiveMessage();
            }
            // Assign received message as variable passed in parameter
            else
            {
                rcvdMsg = firstMsg;
                isFirst = false;
            }
            
            
            
            // Checks if checksum is valid and if FIN flag is set
            isAck = isACKFlagSet(rcvdMsg);
            isChkValid = isChecksumValid(rcvdMsg);
            isFin = isFINFlagSet(rcvdMsg);
            if(isFin && isChkValid)
            {
                isFinValid = true;
            }
            otherSeqNum = getSequenceNumber(rcvdMsg);
            otherAckNum = getAcknowledgmentNumber(rcvdMsg);
            isSeqValid = isSeqNumValid(otherSeqNum);
//            if(isTraceOn)
//            {
//                System.out.println("Sequence number valid: " + isSeqValid);
//                deviceLog.println("Sequence number valid: " + isSeqValid);
//            }
                        

            // If checksum is valid, write data out  to file and set ACK flag
            if(isChkValid && isAck && isSeqValid)
            {
                ackNum = incrementSequence(ackNum);
                seqNum = incrementSequence(seqNum);
                rcvdData = getData(rcvdMsg);
                tempHeader = setACKFlag(tempHeader, true);
                tempHeader = setSequenceNumber(tempHeader, seqNum);
                tempHeader = setAcknowledgmentNumber(tempHeader, ackNum);
                if(!isFinValid)
                {
                    fOutStr.write(rcvdData);
                }
            }
            // Increments sequence number to see if it's the next message
            else if(isChkValid && isAck && !isSeqValid)
            {
                otherSeqNum++;
                isSeqValid = isSeqNumValid(otherSeqNum);
//                if(isTraceOn)
//                {
//                System.out.println("Sequence number valid: " + isSeqValid);
//                deviceLog.println("Sequence number valid: " + isSeqValid);
//                }
                if(isSeqValid)
                {
                    tempHeader = setACKFlag(tempHeader, true);
                    tempHeader = setSequenceNumber(tempHeader, seqNum);
                    tempHeader = setAcknowledgmentNumber(tempHeader, ackNum);
                }
            }
            // If checksum is invalid, don't set ACK flag
            else
            {
                tempHeader = setACKFlag(tempHeader, false);
            }
            
            tempHeader = setDRTFlag(tempHeader, isUpload);
            tempMsg = assembleMessage(tempHeader, tempData);
            tempMsg = setChecksum(tempMsg);
            
            // insert while loop here?
            sendMessage(tempMsg);
        }
        
        // Close file input stream and connection
        fOutStr.close();
        closeConnection();
    }
    
    @Override
    public void startDevice()
    {
        
    }
}