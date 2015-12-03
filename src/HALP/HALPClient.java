package HALP;

// Simple echo client.

import static HALP.HALP.FLAG_OFFSET;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HALPClient extends HALP implements HALPClientInterface
{
        // Hard coded IP addresses for testing
//    protected String homeTestIP = "192.168.0."; // for testing at home
//    protected String testIGIP = homeTestIP + "110";
//    protected String testServIP = homeTestIP + "109";
//    protected String testFileName = "alice.txt";
//    protected boolean testIsUpload = true;
//    protected int testDataRate = 5000;
    
    private static final int SERVER_PORT = 43000;  
    private static final int IG_PORT = 43000;
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
    
    //variables for statistics
//    private final long FILESIZE = inputFile.length();
    private long start;
    private long stop;
//    private final String TRANSFERTIME = getTransferTime(start, stop);
//    private int messagesGen = 0;
//    private int totalMessages = 0;
//    private final int RETRANSMISSIONS = (totalMessages - messagesGen);
//    private int expectedRetrans = (errorRate/(1-errorRate))*messagesGen;  not sure how we will get error rate transfered to client?????
//    private int maxRetrans = 0;
//    private final int PERCENTRETRANS = (RETRANSMISSIONS/totalMessages)*100;
    
    private PrintWriter senderLog = null;
    private PrintWriter receiverLog = null;
   
            
    public static void main (String args[]) throws Exception 
    {	
        Scanner console = new Scanner(System.in);
        HALPClient halpClient = new HALPClient(IG_PORT, SERVER_PORT);
        
        // Hard coded IP addresses for testing
        String homeTestIP = "192.168.0."; // for testing at home
        String testIGIP = homeTestIP + "110";
        String testServIP = homeTestIP + "111";
        String testFile1 = "alice.txt";
        String testFile2 = "mission0.txt";
        String testFile3 = "mission1.txt";
        String testFile4 = "mission2.txt";
        String testFileName = testFile4; // swap out test files here
        boolean testIsUpload = true;
        int testDataRate = 16;
        
        // Hard coded values for testing
        halpClient.setIGIP(testIGIP);
        halpClient.setServerIP(testServIP);
        halpClient.setFileName(testFileName);
        halpClient.setTransferDirection(testIsUpload);
        halpClient.setDataRate(testDataRate);
        
        
        // For user input
//        halpClient.inputIGIP();
//        halpClient.inputServIP();
//        halpClient.inputFileName();
//        halpClient.inputTransferDirection();
//        halpClient.inputDataRate();
                
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
        isClient = true;
        try 
        {
            System.out.println("Client has requested a connection.");
            
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
            seqNum = generateSequenceNumber();

            // User input
    //        inputIGIP();
    //        inputServIP();

            // Hard coded values for testing
//            setIGIP(testIGIP);
//            setServerIP(testServIP);
//            setFileName(testFileName);
//            setTransferDirection(testIsUpload);
//            setDataRate(testDataRate);
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
            while(!isSyn && !isAck && !isChkValid)
            {
                // Sends message and prints out other fields
                sendMessage(tempMsg);
                printFileNameField(tempMsg);
                printDataRateField(tempMsg);
                
                // Receives message and checks if flags are set and checksum 
                // is valid
                rcvdMsg = receiveMessage();
                isSyn = isSYNFlagSet(rcvdMsg);
                isAck = isACKFlagSet(rcvdMsg);
                isChkValid = isChecksumValid(rcvdMsg);
            }
            
            // Sets data rate based on what IG places in data rate field
            int igDataRate = getDataRateField(rcvdMsg);
            setDataRate(igDataRate);
            System.out.println(dataRate);
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
//                printMessage(tempHeader);

                // Set data fields and checksum
                tempData = new byte[1];
                tempMsg = assembleMessage(tempHeader, tempData);
                tempMsg = setChecksum(tempMsg);
                isSyn = false;
                isAck = false;

                
                // Uncommenting the isSyn made this work for some reason
                while(isSyn && !isAck)
                {
                    sendMessage(tempMsg);
                    rcvdMsg = receiveMessage();
                    isSyn = isSYNFlagSet(rcvdMsg);
                    isAck = isACKFlagSet(rcvdMsg);
    //                isDrt = isDRTFlagSet(rcvdMsg);
                }
                
//                ackNum = getSequenceNumber(rcvdMsg);
//                ackNum = incrementSequence(ackNum);
//                seqNum = incrementSequence(seqNum);
                runAsReceiver(rcvdMsg);
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
            deviceSocket = new DatagramSocket(servPortNum);
        } 
        catch (SocketException ex) 
        {
            Logger.getLogger(HALPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        boolean placeholderCondition = false;
        System.out.println("Server has started.");
        while(placeholderCondition == false)
        {
            isChkValid = false;
            try 
            {
                // Keep sending negative acknowledgment until checksum is valid
                // and SYN flag is set
                while(isChkValid == false)
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
//                    tempHeader = setDestIP(tempHeader, servIPAddr);
//                    tempHeader = setDestPN(tempHeader, servPortNum);
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
                    msgSize = HEDR_LEN + dataRate;
                    otherSeqNum = getSequenceNumber(rcvdMsg);
                    ackNum = incrementSequence(otherSeqNum);

                    // Re-initialize data to account for data rate field
                    tempData = new byte[2];

                    // Create header
    //                    tempHeader = setDestIP(tempHeader, servIPAddr);
    //                    tempHeader = setDestPN(tempHeader, servPortNum);
                    tempHeader = setACKFlag(tempHeader, true);
                    tempHeader = setSYNFlag(tempHeader, true);
                    tempHeader = setSequenceNumber(tempHeader, seqNum);
                    tempHeader = setAcknowledgmentNumber(tempHeader, ackNum);

                    // Create message to send to client
                    tempMsg = assembleMessage(tempHeader, tempData);
                    tempMsg = setDataRateField(tempMsg, dataRate);
                    tempMsg = setChecksum(tempMsg);
                    sendMessage(tempMsg);
                    printFileNameField(tempMsg);
                    printDataRateField(tempMsg);
                }
                // If ACK is set and DRT is an upload
                else if(isAck && isUpld)
                {
//                    ackNum = getSequenceNumber(rcvdMsg);
//                    ackNum = incrementSequence(ackNum);
//                    seqNum = incrementSequence(seqNum);
                    isUpload = isUpld;
                    runAsReceiver(rcvdMsg);
                    placeholderCondition = true;
                }
                // If ACK is set and DRT is a download
                else if(isAck && !isUpld)
                {
//                    ackNum = getSequenceNumber(rcvdMsg);
                    ackNum = incrementSequence(ackNum);
                    seqNum = incrementSequence(seqNum);
                    isUpload = isUpld;
                    runAsSender();
                    placeholderCondition = true;
                }
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
        start = startTransferTimer();
        
        // Create input file and start file input stream
        inputFile = new File(fileName);
        fInStr = new FileInputStream(fileName);
        
         
        //creating log for sender
        String senderStr = System.getProperty("user.home") + "/Desktop/";
        String senderName = "Sender_Log.txt";
        File senderFile = new File(senderStr + senderName);
        senderLog = new PrintWriter(senderFile);
        senderLog.println("Senders' Logging: \n");
        
        
        // Initialize message variables
        byte[] tempHeader = new byte[HEDR_LEN];
        byte[] tempData = new byte[dataRate]; // change later
        byte[] tempMsg = null;
        byte[] rcvdMsg = null;
        
        // Initialize flag variables
        boolean isAck = false;
        boolean isChkValid = false;
        
        // Sends messages until the end of the file has been reached
        while(fInStr.available() != 0)
        {
            // Create message
            tempData = new byte[dataRate];
            tempHeader = setDestIP(tempHeader, servIPAddr);
            tempHeader = setDestPN(tempHeader, servPortNum);
            tempHeader = setSequenceNumber(tempHeader, seqNum);
            tempHeader = setAcknowledgmentNumber(tempHeader, ackNum);
            tempHeader = setDRTFlag(tempHeader, isUpload);
            tempHeader = setACKFlag(tempHeader, true);
            fInStr.read(tempData, 0, dataRate);
            tempMsg = assembleMessage(tempHeader, tempData);
            tempMsg = setChecksum(tempMsg);
            
            // Reset flags
            isAck = false;
            isChkValid = false;
            
            // Resend message if acknowledgment is negative or checksum invalid
            while(!isAck || !isChkValid)
            {
                Thread.sleep(1000);
                sendMessage(tempMsg);
                rcvdMsg = receiveMessage();
                isChkValid = isChecksumValid(rcvdMsg);
                isAck = isACKFlagSet(rcvdMsg);
            }
//            ackNum = getSequenceNumber(rcvdMsg);
            ackNum = incrementSequence(ackNum);
            seqNum = incrementSequence(seqNum);
        }
        
        
        // Create message
        tempHeader = setDestIP(tempHeader, servIPAddr);
        tempHeader = setDestPN(tempHeader, servPortNum);
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
        
        // Resend message if acknowledgment is negative or checksum invalid
        while(!isAck || !isChkValid)
        {
            Thread.sleep(1000);
            sendMessage(tempMsg);
            rcvdMsg = receiveMessage();
            isChkValid = isChecksumValid(rcvdMsg);
            isAck = isACKFlagSet(rcvdMsg);
        }
        
        // Close file input stream and connection
        stop = stopTransferTimer();
        fInStr.close();
        senderLog.close();
        closeConnection();
    }

    @Override
    public void runAsReceiver(byte[] firstMsg) throws FileNotFoundException, 
            IOException, Exception 
    {
        System.out.println("Begin receiving data");
        
        // Initialize flag variables
        boolean isFin = false;
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
        
        
        //creating log file for receiver
        String receiverStr = System.getProperty("user.home") + "/Desktop/";
        String receiverName = "Receiver_Log.txt";
        File receiverFile = new File(receiverStr + receiverName);
        receiverLog = new PrintWriter(receiverFile);
        receiverLog.println("Receivers' Logging: \n");
        
        
        
        // Initialize message variables
        byte[] tempHeader = new byte[HEDR_LEN];
        byte[] tempData = new byte[1]; // change later
        byte[] rcvdData = null;
        byte[] savedData = null; // valid data to be saved
        byte[] tempMsg = null;
        byte[] rcvdMsg = null;
        
        // Run until end of data stream is reached
        while(!isFin)
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
            otherSeqNum = getSequenceNumber(rcvdMsg);
            otherAckNum = getAcknowledgmentNumber(rcvdMsg);
            isSeqValid = isSeqNumValid(otherSeqNum);
            System.out.println(isSeqValid);
            
//            otherSeqNum = getSequenceNumber(rcvdMsg);

            // If checksum is valid, write data out  to file and set ACK flag
            if(isChkValid && isAck && isSeqValid)
            {
                ackNum = incrementSequence(ackNum);
                seqNum = incrementSequence(seqNum);
                rcvdData = getData(rcvdMsg);
                tempHeader = setACKFlag(tempHeader, true);
                tempHeader = setSequenceNumber(tempHeader, seqNum);
                tempHeader = setAcknowledgmentNumber(tempHeader, ackNum);
//                if(!isWritten)
//                {
//                savedData = rcvdData;
                    fOutStr.write(rcvdData);
////                    isWritten = true;
//                    ackNum = incrementSequence(ackNum);
//                }
            }
            // Increments sequence number to see if it's the next message
            else if(isChkValid && isAck && !isSeqValid)
            {
//                tempSeqNum = getSequenceNumber(rcvdMsg);
                otherSeqNum++;
                isSeqValid = isSeqNumValid(otherSeqNum);
                System.out.println(isSeqValid);
                if(isSeqValid)
                {
                    tempHeader = setACKFlag(tempHeader, true);
                    tempHeader = setSequenceNumber(tempHeader, seqNum);
                    tempHeader = setAcknowledgmentNumber(tempHeader, ackNum);
//                    fOutStr.write(savedData);
////                    isWritten = true;
//                    ackNum = incrementSequence(ackNum);
//                    seqNum = incrementSequence(seqNum);
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
        receiverLog.close();
        closeConnection();
    }
    
    @Override
    public void run()
    {
        
    }
}