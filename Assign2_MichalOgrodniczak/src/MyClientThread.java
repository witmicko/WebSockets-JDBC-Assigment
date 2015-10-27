import messages.CloseConnectionMessage;
import messages.Message;
import messages.PaymentsMessage;
import utilities.SQL_driver;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;


/**
 * Created by witmi on 23/10/2015.
 */
public class MyClientThread extends Thread {
    //The socket the client is connected through
    private Socket socket;
    //The ip address of the client
    private InetAddress address;
    //The input and output streams to the client
    private DataInputStream inputFromClient;
    private DataOutputStream outputToClient;

    private String clientName;
    private Server server;

    // The Constructor for the client
    public MyClientThread(Socket socket, int clientCtr, Server server) {
        this.server = server;
        this.socket = socket;
        address     = socket.getInetAddress();
        clientName  = String.valueOf(clientCtr);

        try {
            inputFromClient = new DataInputStream(socket.getInputStream());
            outputToClient = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * The method that runs when the thread starts
    */
    public void run() {
        //synchronized keyword or locks could be used here but JTextArea.append() is ThreadSafe so there is no need,
        //synch. example below (it`s it resource heavire and slower on runtime - tested 100 concurrent client connections)
        server.jta.append("Client " + clientName + "'s hostname is: " + address.getHostName()   + "\n" +
                "Client " + clientName + "'s IP address is: " + address.getHostAddress() + "\n");
        /*
        synchronized (jta) {
            jta.append("Client " + clientName + "'s hostname is: "   + address.getHostName()    + "\n");
            jta.append("Client " + clientName + "'s IP address is: " + address.getHostAddress() + "\n");
        }
        */


        while (true) {
            try {
                int length = inputFromClient.available();
                if(length > 0){
                    Message messageIn = readInMessage();

                    if(messageIn instanceof CloseConnectionMessage)cleanUp();
                    Message response = handleMessage(messageIn);



                    System.out.println();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Message handleMessage(Message msg) {
        if(msg instanceof CloseConnectionMessage) return null;

        else if(msg instanceof PaymentsMessage){
            PaymentsMessage paymentsMessage = (PaymentsMessage) msg;
            String ss = server.sql_driver.getApplicantByID(paymentsMessage.getAccNUm());
            System.out.println();
        }
        return null;
    }

    /**
     * reads in bytes from the input stream and returns a messages.Message object
     * @return
     */
    private Message readInMessage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Message message = null;

        try {
            while (inputFromClient.available() > 0) {
                int i = inputFromClient.read();
                if (i > -1) {
                    baos.write(i);
                }
            }

            message = Message.convertFromBytes(baos.toByteArray());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return message;
    }


    /**
     * Clean up method when thread stops running, closes the socket to release the resources.
     */
    private void cleanUp(){
        System.out.println("Closing client thread");
        try{
            socket.close();
        } catch (IOException e) {
            System.out.println("Could not close socket");
            System.exit(-1);
        }
    }
}