/**
 * Created by Michal Ogrodniczak on 22/10/2015.
 * Client class
 */

import messages.CloseConnectionMessage;
import messages.Message;
import messages.RepaymentsReqMessage;
import utilities.FormVerifier;
import utilities.MessageComms;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;


public class Client extends JFrame {
    private FormVerifier intVerifier = new FormVerifier("int");
    private FormVerifier dblVerifier = new FormVerifier("decimal");

    //GUI
    private JTextField accNumTxt;
    private JTextField rateTxt;
    private JTextField yearsTxt;
    private JTextField amountTxt;
    private JTextArea  textArea;

    // IO streams
    private DataOutputStream toServer;
    private DataInputStream fromServer;
    private Socket socket;

    /**
     * Client constructor, initiates GUI and server connection
     */
    public Client() {
        buildGUI();
        connectToServer();
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.setVisible(true);
    }




    /**
     * Opens connection and builds data streams for input and output.
     */
    private void connectToServer() {
        try {
            socket = new Socket("localhost", 8000);

            // Create an input output streams to receive and send data from and to the server
            fromServer = new DataInputStream(socket.getInputStream());
            toServer   = new DataOutputStream(socket.getOutputStream());

            InetAddress address = socket.getInetAddress();
            textArea.append("Connected to Bank`s server :" + address.getHostName()+"@"+address.getHostAddress());
        }
        catch (IOException ex) {
            textArea.append(ex.toString() + '\n');
        }
    }


    /**
     * Submit button listener
     */
    private class SubmitBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean verified = intVerifier.verify(accNumTxt) &&
                               intVerifier.verify(yearsTxt) &&
                               dblVerifier.verify(rateTxt) &&
                               dblVerifier.verify(amountTxt);
            if (verified) {
                int accountNumber = Integer.parseInt   (accNumTxt.getText());
                double annualRate = Double .parseDouble(rateTxt.getText());
                int    numOfYears = Integer.parseInt   (yearsTxt.getText());
                double amount    = Double .parseDouble(amountTxt.getText());


                try {
                    RepaymentsReqMessage message = new RepaymentsReqMessage(accountNumber, annualRate, numOfYears, amount);
                    toServer.write(message.convertToBytes());

                    waitForResponse();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(
                        textArea,
                        "Fields require numerical input");
            }
        }
    }

    /**
     * waits for the server response, releases hold after it gets the message or IOException
     */
    private void waitForResponse() {
        while (true){
            try {
                if(fromServer.available() > 0){
                    Message messageIn = MessageComms.readInMessage(fromServer);
                    textArea.append("Message from Server: \n" + messageIn.prettyPrint() + "\n");

                    InetAddress address = socket.getInetAddress();
                    textArea.append("Message from Bank`s server :" +
                            address.getHostName()+"@"+address.getHostAddress()+
                            ":\n" + messageIn.prettyPrint() + "\n");
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }


    /**
     * Builds GUI, sets up event handler for submit button and input validator for the form data
     */
    private void buildGUI() {
        setSize(500, 300);
        setTitle("Client");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    Message message = new CloseConnectionMessage();
                    toServer.write(message.convertToBytes());
                    socket.close();
                } catch (IOException | NullPointerException e1) {
                    e1.printStackTrace();
                }
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
        getContentPane().add(topPanel, BorderLayout.NORTH);
        GridBagLayout gbl_topPanel = new GridBagLayout();

        int topCol_1 = (int) (getWidth() * 0.75);
        int topCol_2 = (int) (getWidth() * 0.20);
        gbl_topPanel.columnWidths = new int[]{topCol_1, topCol_2};
        topPanel.setLayout(gbl_topPanel);

        //form panel
        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        GridBagConstraints gbc_formPanel = new GridBagConstraints();
        gbc_formPanel.fill = GridBagConstraints.HORIZONTAL;
        gbc_formPanel.insets = new Insets(0, 0, 0, 5);
        gbc_formPanel.anchor = GridBagConstraints.WEST;
        topPanel.add(formPanel, gbc_formPanel);
        formPanel.setLayout(new GridLayout(0, 1, 0, 0));

        //Account Number
        JPanel accPanel = new JPanel();
        formPanel.add(accPanel);
        accPanel.setLayout(new GridLayout(0, 2, 5, 0));

        JLabel accNumLbl = new JLabel("Account Number");
        accNumLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        accPanel.add(accNumLbl);

        accNumTxt = new JTextField();
        accNumTxt.setName(accNumLbl.getText());
        accNumTxt.setInputVerifier(intVerifier);
        accNumTxt.setText("1001");
        accPanel.add(accNumTxt);

        //Annual Interest Rate
        JPanel ratePanel = new JPanel();
        formPanel.add(ratePanel);
        ratePanel.setLayout(new GridLayout(0, 2, 5, 0));

        JLabel rateLbl = new JLabel("Annual Interest Rate");
        rateLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        ratePanel.add(rateLbl);

        rateTxt = new JTextField();
        rateTxt.setName(rateLbl.getText());
        rateTxt.setInputVerifier(dblVerifier);
        rateTxt.setText("4");
        ratePanel.add(rateTxt);

        JPanel yearsPanel = new JPanel();
        formPanel.add(yearsPanel);
        yearsPanel.setLayout(new GridLayout(0, 2, 5, 0));

        //Number of Years
        JLabel yearsLbl = new JLabel("Number of Years");
        yearsLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        yearsPanel.add(yearsLbl);

        yearsTxt = new JTextField();
        yearsTxt.setName(yearsLbl.getText());
        yearsTxt.setInputVerifier(intVerifier);
        yearsTxt.setText("5");

        yearsPanel.add(yearsTxt);

        //Loan Amount
        JPanel amountPanel = new JPanel();
        formPanel.add(amountPanel);
        amountPanel.setLayout(new GridLayout(0, 2, 5, 0));

        JLabel amountLbl = new JLabel("Loan Amount");
        amountLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        amountPanel.add(amountLbl);

        amountTxt = new JTextField();
        amountTxt.setName(amountLbl.getText());
        amountTxt.setInputVerifier(dblVerifier);
        amountTxt.setText("14000");
        amountPanel.add(amountTxt);

        //Button
        JButton submitBtn = new JButton("Submit");
        submitBtn.setBorder(new LineBorder(new Color(0, 0, 0)));
        submitBtn.addActionListener(new SubmitBtnListener());

        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_btnNewButton.anchor = GridBagConstraints.EAST;
        gbc_btnNewButton.fill = GridBagConstraints.BOTH;

        topPanel.add(submitBtn, gbc_btnNewButton);

        //text area
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);

        //scrolls to the last line of text area
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                textArea.setCaretPosition(textArea.getDocument().getLength());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

    }

}