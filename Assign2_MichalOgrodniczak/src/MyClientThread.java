import messages.*;
import utilities.MessageComms;
import utilities.SQL_driver;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;


/**
 * Created by Michal Ogrodniczak on 23/10/2015.
 * Thread class, being started from the server, handles client communication
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
    private JTextArea jta;

    private SQL_driver sqlDriver;

    /**
     * Client thread constructor
     * @param socket socket at which client is connected to the server
     * @param clientCtr counter, an ID of sorts of the client threads
     * @param jta main text area in server GUI to display all communication
     */
    public MyClientThread(Socket socket, int clientCtr, JTextArea jta) {
        this.jta    = jta;
        this.socket = socket;
        address     = socket.getInetAddress();
        clientName  = String.valueOf(clientCtr);

//      Sets up DB connection
//      This pattern follows Oracle JDBC Developer's Guide and Reference
//      (https://docs.oracle.com/cd/A87860_01/doc/java.817/a83724/tips1.htm)
        sqlDriver   = SQL_driver.defaultSqlDriverBuilder();
        sqlDriver.connect();

        try {
            inputFromClient = new DataInputStream (socket.getInputStream());
            outputToClient  = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * The method that runs when the thread starts
    */
    public void run() {
        //synchronized keyword or locks could be used here but JTextArea.append() is ThreadSafe so there is no need,
        //synch. example below (it`s it resource heavier and slower on runtime - tested 100 concurrent client connections)
        jta.append("Client " + clientName + "'s hostname is: " + address.getHostName()   + "\n" +
                "Client " + clientName + "'s IP address is: " + address.getHostAddress() + "\n");
        /*
        synchronized (jta) {
            jta.append("Client " + clientName + "'s hostname is: "   + address.getHostName()    + "\n");
            jta.append("Client " + clientName + "'s IP address is: " + address.getHostAddress() + "\n");
        }
        */


        while (true) {
            try {

                if(inputFromClient.available() > 0){
                    Message messageIn = MessageComms.readInMessage(inputFromClient);

                    jta.append("Message from Client " + clientName + ": "
                            + address.getHostName() + "@" + address.getHostAddress() + "\n" +
                            messageIn.toString() + "\n");

                    Message response = handleMessage(messageIn);

                    if(response == null){
                        jta.append("Client " + clientName + " closed connection");
                        cleanUp();
                        break;
                    }else {
                        outputToClient.write(response.convertToBytes());
                    }

                    System.out.println();
                }

            } catch (IOException e) {
                e.printStackTrace();
                cleanUp();
                break;
            }
        }

    }




    /**
     * Handles incoming message and build up a response.
     * @param msg Message from client
     * @return  response to the message
     */
    private Message handleMessage(Message msg) {
        if(msg == null) return new TextMessage("Error, please resend the request");

        else if(msg instanceof CloseConnectionMessage) return null;

        else if(msg instanceof RepaymentsReqMessage){
            RepaymentsReqMessage repaymentsReqMessage = (RepaymentsReqMessage) msg;

            try {
                String applicant = sqlDriver.getApplicantByID(repaymentsReqMessage.getAccountNumber());
                if(applicant == null) return new TextMessage("User doesn't exist");
                else                  return new RepaymentsRespMessage(applicant, repaymentsReqMessage);
            } catch (SQLException e) {
                e.printStackTrace();
                jta.append("Client " + clientName + " threw SQL exception on our database");
                return new TextMessage("Database connection error, please try again at later stage");
            }
        }
        return null;
    }


    /**
     * Clean up method when thread stops running, closes the socket to release the resources.
     */
    private void cleanUp(){
        System.out.println("Closing client thread");
        try{
            socket.close();
            inputFromClient.close();
            outputToClient.close();
        } catch (IOException e) {
            //ignore close exception
        }
    }
}