/**
 * Created by michal on 22/10/2015.
 */

import messages.CloseConnectionMessage;
import messages.Message;
import messages.PaymentsMessage;
import utilities.FormVerifier;

import javax.swing.*;
import javax.swing.border.LineBorder;
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

    public static void main(String[] args) {
        Client client = new Client();
        client.setVisible(true);
    }

    public Client() {
        buildGUI();
        connectToServer();



    }


    /**
     * Opens connection and builds data streams for input and output.
     */
    private void connectToServer() {
        try {
            // Create a socket to connect to the server
            socket = new Socket("localhost", 8000);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
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
                double ammount    = Double .parseDouble(amountTxt.getText());


                try {
                    PaymentsMessage message = new PaymentsMessage(accountNumber, annualRate, numOfYears, ammount);
                    toServer.write(message.convertToBytes());

                    textArea.append("Radius is " + 65 + "\n");
                    textArea.append("Area received from the server is \n");
                } catch (Exception ex) {
                    System.err.println(ex);
                }
            } else {
                JOptionPane.showMessageDialog(
                        textArea,
                        "Fields require numerical input");
            }
        }
    }



    /**
     * Builds GUI, sets up event handler for submit button and input validator for the form data
     */
    private void buildGUI() {
        setSize(500, 300);
        setTitle("Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    Message message = new CloseConnectionMessage();
                    toServer.write(message.convertToBytes());
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }catch (NullPointerException e2){
                    e2.printStackTrace();
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
        accNumTxt.setText("1111");
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
        rateTxt.setText("2222");
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
        yearsTxt.setText("333");

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
        amountTxt.setText("4444");
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
        getContentPane().add(textArea, BorderLayout.CENTER);


    }




}