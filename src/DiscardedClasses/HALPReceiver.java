package DiscardedClasses;

import HALP.CS313Project.*;
import java.io.FileOutputStream;
import static java.lang.System.console;
import java.util.Arrays;
import java.util.Scanner;

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
 *                  receiver receives a file divided into frames and calculates
 *                  the CRC checksum to detect errors, and sends a positive 
 *                  acknowledgment if a frame has no errors and a negative 
 *                  acknowledgment if a frame has errors. Data communication 
 *                  persists until the file has been transmitted completely.
 */

/** 
 * This class simulates the functions of a receiver of a data transmission. It
 * establishes a connection with a sender, receives frames, and sends an 
 * acknowledgment frame based on whether or not there were errors in the sent
 * frames. After the data transmission is over, the connection terminates.
 * 
 * @author Benjamin Menning
 * @version 04/21/2015 
 */
public class HALPReceiver 
{

	static int senderPort = 3200;   // port number used by sender
	static int receiverPort = 3300; // port number used by receiver
	
    /**
     * This is the main method that simulates the functions of a receiver of a
     * data transmission. It establishes a connection with a sender, receives 
     * frames, and sends an acknowledgment frame based on whether or not there 
     * were errors in the sent frames. After the data transmission is over, 
     * the connection terminates.
     * 
     * @param args
     * @throws Exception
     */
    public static void main (String args[]) throws Exception 
    {	
        Scanner console = new Scanner(System.in);

        // Variables declared

        // Creates CRC Object
        CRC8 crc8 = new CRC8();

        // File name variables
        String currentFile = "";
        String fileName1 = "mission0x.txt";
        String fileName2 = "mission1x.txt";
        String fileName3 = "mission2x.txt";

        // Int variables
        int lengthMessageToSend; 
        int lengthMessageReceived = 0;
        int length;
        int maxRange;
        int traceInput = 0;

        // String variables
        String ackFrame;
        String messageReceived;
        String frameStatus = "";

        // Byte variables for buffers and fields
        byte[] sendingBuffer = new byte[1];
        byte[] receivingBuffer = new byte[19];
        byte[] lengthByte = new byte[1];
        byte[] payloadBytes = new byte[16];
        byte checksum;

        // Boolean variables
        boolean isEnd = false; // for end of transmission
        boolean isTraceOn = false; // for when trace is turned on

        try
        { 
           // Requests user to enter output file name
            System.out.println("Please enter the name of the output "
                     + "file:");
            currentFile = console.nextLine();

            // Requests user to turn on the trace feature
            System.out.println("Please enter '1' to turn trace on: ");
            traceInput = console.nextInt();
        }
        catch(ArrayIndexOutOfBoundsException | IllegalArgumentException e)
        {
           System.out.println(e);
        }

        // Turns trace on or off based on user input
        if(traceInput == 1)
        {
            isTraceOn = true;
        }
        else
        {
            isTraceOn= false;
        }

        // Set up a link with source and destination ports
        // Any 4-digit number greater than 3000 should be fine. 
        Link myLink = new SimpleLink(receiverPort, senderPort);

        // Creates file output stream to write message to
        FileOutputStream fOutStr = new FileOutputStream(currentFile);

        // Performs data transmission until the end has been reached
        while(isEnd == false)
        {
            // Receive a message
            lengthMessageReceived = myLink.receiveFrame(receivingBuffer);

            // Parse out the payload bytes into a message string
            messageReceived = new String(receivingBuffer, 2, 
                    16);

            // Parse out the length byte into an integer
            lengthByte = Arrays.copyOfRange(receivingBuffer, 1, 2);
            length = new Byte(lengthByte[0]).intValue();

            // Calculate max range of payload byte copy
            maxRange = length + 2;

            // Copy payload bytes
            payloadBytes = Arrays.copyOfRange(receivingBuffer, 2, maxRange);

            // Display message received if trace is turned on
            if(isTraceOn == true)
            {
                System.out.println("\nFrame received: [" + messageReceived + 
                        "]");
            }

            // Calculate checksum of received frame
            checksum = crc8.checksum(receivingBuffer);

            // Display calculated checksum if trace is turned on
            if(isTraceOn == true)
            {
                System.out.println("Calculated checksum: " + checksum);
            }

            // Write payload to output file and send positive acknowledgment
            // if checksum is 0
            if(checksum == 0x00)
            {
                fOutStr.write(payloadBytes);
                frameStatus = "OK";
                ackFrame = "1";
            }
            // Send negative acknowledment
            else
            {
                frameStatus = "Error";
                ackFrame = "0";
            }

            // Prepare a message		
            lengthMessageToSend = ackFrame.length(); 
            sendingBuffer = ackFrame.getBytes();

            // Send the message
            myLink.sendFrame(sendingBuffer, lengthMessageToSend);

            // Display frame status and acknowledment frame transmitted
            // if trace is on
            if(isTraceOn == true)
            {
                System.out.println("Frame status: " + frameStatus);
                System.out.println("Frame transmitted: [" + ackFrame + "]");
            }

            // End data communication if data is empty and checksum is 0
            if(length == 0 && checksum == 0x00)
            {
                isEnd = true;
            }
        }
        fOutStr.close();         
        myLink.disconnect();
    }
}