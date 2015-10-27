import utilities.SQL_driver;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * Created by michal on 22/10/2015.
 */
public class Server extends JFrame {
    private static final String URL  = "jdbc:mysql://localhost:3306/BankDatabase";
    private static final String USER = "root";
    private static final String PASS = "";

    // Text area for displaying contents
    private JTextArea jta = new JTextArea();
    private SQL_driver sql_driver;
    private ServerSocket serverSocket;


    public static void main(String[] args) {
        Server s = new Server();
        s.run();

    }

    public Server() {
        setupSqlConnection();
        buildGUI();
        buildSocket();
    } // End Server Construct


    /**
     * Builds GUI
     */
    private void buildGUI() {
        // Place text area on the frame
        setLayout(new BorderLayout());
        add(new JScrollPane(jta), BorderLayout.CENTER);
        setTitle("Server");
        setSize(800, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true); // It is necessary to show the frame here!
    }

    /**
     * Sets up DB connection, prints connection status to the main text area.
     * This is mainly for debugging and status monitoring as each thread will have its own jdbc connection.
     * This pattern follows Oracle JDBC Developer's Guide and Reference
     * (https://docs.oracle.com/cd/A87860_01/doc/java.817/a83724/tips1.htm)
     */
    private void setupSqlConnection() {
        sql_driver = new SQL_driver(URL, USER, PASS);
        String dbAddr = sql_driver.getUSER() + "@" + sql_driver.getURL() + "\n";

        if (sql_driver.connect()) {
            jta.append("SQL connected to: " + dbAddr);
        } else {
            jta.append("SQL error\n");
            jta.append("Cannot connect to: " + dbAddr);
        }
    }

    /**
     * Builds socket and prints status to main text area
     */
    private void buildSocket() {
        try {
            serverSocket = new ServerSocket(8000);
            jta.append("Server started at " + new Date() + '\n');
        } catch (IOException e) {
            e.printStackTrace();
            jta.append("Can't start server, Socket exception\n" + e.getMessage());
        }
        jta.append("--------------------------Setup Complete-------------------------\n");

    }

    /**
     * Runs main loop in the program, awaits client connections and starts nwe thread to deal with them as they come in
     */
    private void run() {
        int clientCtr = 0;
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                clientCtr++;

                jta.append("Starting thread for client " + clientCtr + " " + new Date() + "\n");

                MyClientThread c = new MyClientThread(socket, clientCtr, jta);
                c.start();
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }

    protected void jtaAppend(String string){
        jta.append(string);
    }






}