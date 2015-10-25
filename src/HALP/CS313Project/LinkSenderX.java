package HALP.CS313Project;

import java.util.*;
import java.io.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.System.console;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

/** 
 * Author:          Benjamin Menning
 * 
 * Date:            04/21/2015 
 *                
 * Course:          CS 313 - 01, Spring 2015
 * 
 * Assignment:      Project
 * 
 * Description:     The goal of this project is to simulate data communication
 *                  over a noisy simplex channel in the data link layer using 
 *                  the simplex stop-and-wait protocol with PAR and NAK. The 
 *                  sender transmits a file through a noisy channel and the file
 *                  is divided into packets to fit into a frame. Errors are 
 *                  randomly generated in the CRC-checksummed frame. If the 
 *                  receiver receives a negative acknowledgment, it re-transmits
 *                  the frame. Otherwise, data communication continues until
 *                  the file has been fully transmitted.
 */

/** 
 * This class simulates the functions of a sender of a data transmission. It
 * establishes a connection with a receiver, sends frames, and receives an 
 * acknowledgment frame based on whether or not there were errors in the sent
 * frames. After the data transmission is over, the connection terminates.
 * 
 * @author Benjamin Menning
 * @version 04/21/2015 
 */
public class LinkSenderX {

    static int senderPort = 3200;   // port number used by sender
    static int receiverPort = 3300; // port number used by receiver

    private int errorRate = 0;

    ArrayList<Integer> maxTransmissions = new ArrayList<>();
        
    /** 
     * This class is a comparator that compares transmissions. It orders them 
     * in ascending order.
     * 
     * @author Benjamin Menning
     * @version 04/21/2015
     */
    public class maxTransmissionsComparator implements Comparator<Integer>
    {
        @Override
        public int compare(Integer x, Integer y)
        {
            if (x < y)
            {
                return 1;
            }
            if (x > y)
            {
                return -1;
            }
            return 0;
        }
    }    
    
    /**
     * This method adds a maximum amount of re-transmissions for a single frame
     * to an ArrayList.
     * 
     * @param maxTransmission the int to be assigned as the max transmissions
     */
    public void addMaxTransmission(int maxTransmission)
    {
        maxTransmissions.add(maxTransmission);
    }
    
    /**
     * This method retrieves the maximum number of transmissions for any single
     * frame.
     * 
     * @return int  returns an int containing the max transmissions
     */
    public int getMaxTransmission()
    {
        Collections.sort(maxTransmissions, new maxTransmissionsComparator());
        int maxTransmission = maxTransmissions.get(0);
        return maxTransmission;
    }
    
    /**
     * This method retrieves the status of the max transmission ArrayList of
     * whether or not it is empty.
     * 
     * @return boolean  returns true if empty, false if otherwise
     */
    public boolean isMaxTransmissionsEmpty()
    {
        boolean isEmpty = maxTransmissions.isEmpty();
        return isEmpty;
    }
        
    /**
     * This method generates errors based off of the error rate entered by the
     * user.
     * 
     * @return boolean  returns true if error, false if otherwise
     */
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
    
    /**
     * This method returns a random index for a byte in the frame for an 
     * error to be placed.
     * 
     * @return int  returns an int containing the byte index for the error
     */
    public int randomIndex()
    {
        Random random = new Random();
        int indexMax = 18;
        int randomIndex = random.nextInt(indexMax);
        return randomIndex;
    }
    
    /**
     * This error generates an error within a byte by flipping a random bit 
     * inside of the byte.
     * 
     * @param oldByte the byte where the error is to be generated
     * @return byte returns a byte containing the new error.
     */
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
    
    /**
     * This method generates the number of errors based on the chance of the
     * errors occurring.
     * 
     * @return int  returns an int containing the number of errors
     */
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
    
    /**
     * This method assigns the error rate for the data transmission.
     * 
     * @param rate  the int to be assigned as the error rate
     */
    public void setErrorRate(int rate)
    {
        errorRate = rate;
    }
    
    /**
     * This method retrieves the error rate for the data transmission
     * 
     * @return int  returns the int assigned as the error rate
     */
    public int getErrorRate()
    {
        return errorRate;
    }

    /**
     * This class simulates the functions of a sender of a data transmission. It
     * establishes a connection with a receiver, sends frames, and receives an 
     * acknowledgment frame based on whether or not there were errors in the 
     * sent frames. After the data transmission is over, the connection 
     * terminates.
     * 
     * @param args
     * @throws Exception
     */
    @SuppressWarnings("empty-statement")
    public static void main (String args[]) throws Exception 
    {	
        Scanner console = new Scanner(System.in);
        
        // Creates CRC and link sender objects
        CRC8 crc8 = new CRC8();
        LinkSenderX linkSender = new LinkSenderX();

        byte[] testByte = new byte[1];
        new Random().nextBytes(testByte);
        linkSender.generateByteError(testByte[0]);

        // Files to be read from and transmitted
        String currentFile = "";
        String fileName1 = "mission0.txt";
        String fileName2 = "mission1.txt";
        String fileName3 = "mission2.txt";

        // Frame information
        int lengthFrameToSend = 19; 
        int lengthMessageToSend;
        int lengthMessageReceived = 0;
        int remainingMessage;
        int byteOffset = 0;
        int bytesRead = 0;
        int randomIndex;
        String messageToSend;
        String messageReceived = "1";
        String frameStatus;
        boolean isPositiveAck = false;
        boolean isError = false;
        boolean isEnd = false;
        boolean isTraceOn;
        byte[] receivingBuffer = new byte[1];

        // Data frame segments
        byte[] sequenceByte = {(byte)0x00};
        byte lengthByte;
        byte[] payloadBytes = new byte[16];
        byte[] checksumByte = {(byte)0x00};
        byte[] dataFrameBytes = new byte[19];
        byte[] dataFrameCopy = new byte [19];
        byte[] errorByte = new byte[1];
        byte checksum;

        // Counters
        int errorCount;
        int totalPacketsCnt = 0;
        int totalFramesCnt = 0;
        double totalTheorFramesCnt = 0;
        int totalFramesDmgCnt = 0;
        int maxFramesCnt = 0;
        String statisticsStr;

        // Input variables
        int errorPercent;
        int traceInput = 0;
        
        try
        { 
            // Requests user to enter input file, error rate, and trace
            System.out.println("Please enter the name of the input "
                     + "file:");
            currentFile = console.nextLine();

            System.out.println("Please enter the error rate: ");
            errorPercent = console.nextInt();
            linkSender.setErrorRate(errorPercent);

            System.out.println("Please enter '1' to turn trace on: ");
            traceInput = console.nextInt();
        }
        catch(ArrayIndexOutOfBoundsException | IllegalArgumentException e)
        {
           System.out.println(e);
        }

        if(traceInput == 1)
        {
            isTraceOn = true;
        }
        else
        {
            isTraceOn= false;
        }

        // Set up a link with source and destination ports
        Link myLink = new SimpleLink(senderPort, receiverPort);

        // Assign input file and create file input stream
        File inputFile = new File(currentFile);
        FileInputStream fInStr = null;

        // Determines the length of the file message to send
        lengthMessageToSend = (int) inputFile.length();
        remainingMessage = lengthMessageToSend;


        try 
        {
            // Assign file input stream
            fInStr = new FileInputStream(inputFile);

            // Establish data communication until end of file is reached
            while(fInStr.available() != 0)
            {
                // Assign byte reads based on the remaining amount of message
                if(remainingMessage >= 16)
                {
                    bytesRead = 16;
                }
                else
                {
                    bytesRead = remainingMessage;
                }
                
                // Re-initialize ackknowledgment
                isPositiveAck = false;
                
                // Re-initialize data frame segments
                sequenceByte = new byte[1];
                lengthByte = (byte) bytesRead;
                payloadBytes = new byte[16];
                checksumByte[0] = 0x00;

                // Assign payload bytes from message
                fInStr.read(payloadBytes, byteOffset, bytesRead);
                totalPacketsCnt++;

                // Re-initialize payload if end of file is reached
                if(isEnd == true)
                {
                    payloadBytes = new byte[16];
                }

                // Create and write segments to full data frame using output
                // stream
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
                outputStream.write(sequenceByte);
                outputStream.write(lengthByte);
                outputStream.write(payloadBytes);
                outputStream.write(checksumByte);
                dataFrameBytes = outputStream.toByteArray();   

                // Calculate checksum and assign to data frame
                checksum = crc8.checksum(dataFrameBytes);
                
                // Display message if trace is on
                if(isTraceOn == true)
                {
                    System.out.println("\nCalculated checksum: " + checksum);
                }
                
                // Assign checksum to frame
                dataFrameBytes[18] = checksum;
                
                // Make copy of frame
                System.arraycopy(dataFrameBytes, 0, dataFrameCopy, 0, 
                        dataFrameBytes.length);

                // Transmit frame until a postive acknowledgment is received
                while(isPositiveAck == false)
                {
                    // Copy frame back to original (for re-transmission)
                    System.arraycopy(dataFrameCopy, 0, dataFrameBytes, 0, 
                                                dataFrameCopy.length);
                    
                    // Determine if there is an error
                    isError = linkSender.errorGenerator();
                    
                    // Randomly generate errors
                    if(isError == true)
                    {
                        // Assign status of frame to error
                        frameStatus = "Error";
                        
                        // Increment count of damaged frames
                        totalFramesDmgCnt++;
                        
                        // Generate error count
                        errorCount = linkSender.errorNumber();
                        int i = 0;
                        
                        // Generate errors within a byte
                        for(i = 0; i < errorCount; i++)
                        {
                            randomIndex = linkSender.randomIndex();
                            errorByte[0] = dataFrameBytes[randomIndex];
                            errorByte[0] = linkSender.generateByteError(errorByte[0]);
                            dataFrameBytes[randomIndex] = errorByte[0];
                        }
                    }
                    else
                    {
                        // Assign status of frame to OK
                        frameStatus = "OK";
                    }

                    // Send data frame and display sent message and status
                    myLink.sendFrame(dataFrameBytes, lengthFrameToSend);
                    totalFramesCnt++;
                    messageToSend = new String(payloadBytes);
                    if(isTraceOn == true)
                    {
                        System.out.println("Frame transmitted: [" + 
                                messageToSend + "]");
                        System.out.println("Frame status: " + frameStatus);
                    }

                    // Receive a message
                    lengthMessageReceived = 
                            myLink.receiveFrame(receivingBuffer);

                    // Display the received message
                    messageReceived = new String(receivingBuffer, 0, 
                            lengthMessageReceived);
                    if(isTraceOn == true)
                    {
                        System.out.println("Frame received: [" + 
                                messageReceived + "]");
                    }

                    // Add the max amount of retransmissions for the frame
                    // or increase the amount depending on ack received
                    if(messageReceived.equals("1"))
                    {
                        if(maxFramesCnt > 0)
                        {
                            linkSender.addMaxTransmission(maxFramesCnt);
                        }
                        maxFramesCnt = 0;
                        isPositiveAck = true;
                        if(bytesRead <= 0)
                        {
                            isEnd = true;
                        }
                    }
                    else
                    {
                        isPositiveAck = false;
                        maxFramesCnt++;
                    }
                }

                // Decrease the remaining message length by 16
                remainingMessage -= 16;
            }

            // Initialize variables for last frame
            bytesRead = 0;
            isPositiveAck = false;
            
            // Re-initialize data frame segments
            sequenceByte = new byte[1];
            lengthByte = (byte) bytesRead;
            payloadBytes = new byte[16];
            checksumByte[0] = 0x00;

            // Create and write segments to data frame using output
            // stream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
            outputStream.write(sequenceByte);
            outputStream.write(lengthByte);
            outputStream.write(payloadBytes);
            outputStream.write(checksumByte);
            dataFrameBytes = outputStream.toByteArray();   

            // Calculate checksum and assign to data frame
            checksum = crc8.checksum(dataFrameBytes);
            if(isTraceOn == true)
            {
                System.out.println("\nCalculated checksum: " + checksum);
            }
            
            // Assign checksum to frame
            dataFrameBytes[18] = checksum;
            
            // Make a copy of frame
            System.arraycopy(dataFrameBytes, 0, dataFrameCopy, 0, 
                    dataFrameBytes.length);

            // Re-transmit last frame until positive acknowledgment is received
            while(isPositiveAck == false)
            {
                // Copy frame back to original (for re-transmission)
                System.arraycopy(dataFrameCopy, 0, dataFrameBytes, 0, 
                                            dataFrameCopy.length);
                
                // Determine whether or not an error occurs
                isError = linkSender.errorGenerator();
                
                // Randomly generate errors
                if(isError == true)
                {
                    // Assign status of frame and increase damaged frame count
                    frameStatus = "Error";
                    totalFramesDmgCnt++;
                    
                    // Generate number of errors
                    errorCount = linkSender.errorNumber();
                    int i = 0;
                    
                    // Assign errors to byte
                    for(i = 0; i < errorCount; i++)
                    {
                        randomIndex = linkSender.randomIndex();
                        errorByte[0] = dataFrameBytes[randomIndex];
                        errorByte[0] = linkSender.generateByteError(errorByte[0]);
                        dataFrameBytes[randomIndex] = errorByte[0];
                    }
                }
                else
                {
                    frameStatus = "OK";
                }

                // Send data frame and display sent message
                myLink.sendFrame(dataFrameBytes, lengthFrameToSend);
                totalFramesCnt++;
                messageToSend = new String(payloadBytes);
                if(isTraceOn == true)
                {
                    System.out.println("Frame transmitted: [" + 
                            messageToSend + "]");
                    System.out.println("Frame status: " + frameStatus);
                }

                // Receive a message
                lengthMessageReceived = 
                        myLink.receiveFrame(receivingBuffer);

                // Display the received message
                messageReceived = new String(receivingBuffer, 0, 
                        lengthMessageReceived);
                if(isTraceOn == true)
                {
                    System.out.println("Frame received: [" + 
                            messageReceived + "]");
                }
                
                // Add the max amount of retransmissions for the frame
                // or increase the amount depending on ack received
                if(messageReceived.equals("1"))
                {
                    if(maxFramesCnt > 0)
                    {
                        linkSender.addMaxTransmission(maxFramesCnt);
                    }
                    maxFramesCnt = 0;
                    isPositiveAck = true;
                }
                else
                {
                    isPositiveAck = false;
                    maxFramesCnt++;
                }
            }
        }
        catch (FileNotFoundException e) 
        {
            System.out.println(e);
        }
        catch (IOException e) 
        {
            System.out.println(e);
        }
        finally 
        {
            try 
            {
                if (fInStr != null) 
                {
                    fInStr.close();
                }
            }
            catch (IOException e) 
            {
                System.out.println(e);
            }
        }   
        
        // Calculates theoretical frame count
        int errorRate = linkSender.getErrorRate();
        double denominator = errorRate * .01;
//        System.out.println(errorRate);
//        System.out.println(denominator);
        totalTheorFramesCnt = totalPacketsCnt / (1 - denominator);
        
        // Retrieves the maximum amount of retransmissions
        if(linkSender.isMaxTransmissionsEmpty() == false)
        {
            maxFramesCnt = linkSender.getMaxTransmission();
        }
        
        // Displays the statistics of the data transmission and prints out
        statisticsStr = 
                "\nStatistics: " +
                "\nTotal number of packets read: " + totalPacketsCnt +
                "\nTotal number of frames transmitted: " + totalFramesCnt +
                "\nTheoretical total number of frames transmitted: " + 
                totalTheorFramesCnt +
                "\nTotal number of frames damaged: " + totalFramesDmgCnt +
                "\nMaximum number of retransmission for any single frame: " 
                + maxFramesCnt;
        System.out.println(statisticsStr);
        
        // Close the connection	
        myLink.disconnect();
    }
}