package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;
import java.net.*;

public class ClientMain extends JFrame implements ActionListener {

    public JLabel lbPort,lbIP;
    public   JTextField txtPort, txtIP,txtFile;
    public JPanel pnPort, pnIP,pnBottom, pnTotal;
    public JButton btnConnect,btnChoose,btnSend;
    public File selectedFile;
    public static Socket socket;
    public static String filePathReceive = "//home//giapwibu//";
    public static  boolean isConnected = false;
    public void GUI(){

        lbPort = new JLabel("PORT: ");
        lbIP = new JLabel("IP");
        txtPort = new JTextField(10);
        txtIP = new JTextField(10);
        txtFile = new JTextField();
        txtFile.setPreferredSize(new Dimension(200,30));

        btnConnect = new JButton("Connect");
        btnConnect.addActionListener(this);

        btnChoose = new JButton("Choose");
        btnChoose.addActionListener(this);

        btnSend = new JButton("Send");
        btnSend.addActionListener(this);



        pnPort = new JPanel(new FlowLayout(10,10,10));
        pnIP = new JPanel(new FlowLayout(10,10,10));
        pnBottom = new JPanel(new FlowLayout(10));
        pnTotal = new JPanel(new BorderLayout());

        pnPort.add(lbPort);
        pnPort.add(txtPort);

        pnIP.add(lbIP);
        pnIP.add(txtIP);
        pnIP.add(btnConnect);


        pnBottom.add(btnChoose);
        pnBottom.add(txtFile);
        pnBottom.add(btnSend);

        pnTotal.add(pnPort,BorderLayout.NORTH);
        pnTotal.add(pnIP,BorderLayout.CENTER);



        pnTotal.add(pnBottom,BorderLayout.SOUTH);

        add(pnTotal);
        setBounds(200, 200, 500, 350);
        setVisible(true);

    }

    public ClientMain(String st){
        super(st);
        GUI();
    }

    public static void main(String[] args) throws InterruptedException {
        new ClientMain("Client");

        while(true){
            Thread.sleep(200);
            if(isConnected) break;
        }

        new Thread(() -> {
            while (isConnected && socket != null && !socket.isClosed()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

                ReceiveFile receiveFile = new ReceiveFile(filePathReceive, socket);
                receiveFile.start();
                try {
                    receiveFile.join(); // Đợi quá trình nhận file hoàn tất trước khi lắng nghe file tiếp theo
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();


    }

//    public   void getFileTransfer( String ipServer,int PortServer){
//        String filePathReceive = "E:\\DuAnTruongHoc\\Ky5\\TransferFile\\FileClientReceive\\";
//
//        try {
//            socket = new Socket(ipServer, PortServer);
//            System.out.println("Connected to server. Listening for files...");
//
//            // Keep listening for incoming files as long as the socket is open.
//            while (!socket.isClosed()) {
//                ReceiveFile receiveFile = new ReceiveFile(filePathReceive, socket);
//                receiveFile.start();
//                receiveFile.join(); // Wait for the file transfer to complete.
//            }
//        } catch (Exception e) {
//            System.out.println("Error from client: " + e.getMessage());
//        }
//    }




    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnConnect) {

            // btnConnect.setEnabled(false);
            if(!txtPort.getText().trim().isEmpty() && !txtIP.getText().trim().isEmpty()) {
                int portServer = Integer.parseInt(txtPort.getText().trim());
                String ipServer = txtIP.getText().trim();
                try {
                    socket = new Socket(ipServer,portServer);
                    isConnected = true;
                    btnConnect.setEnabled(false);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }


        } else if (e.getSource() == btnChoose) {

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose a file to send");
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                txtFile.setText(selectedFile.getName());
            }

        } else if (e.getSource() == btnSend) {

            if (isConnected && selectedFile != null) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                SenderFile senderFile = new SenderFile(selectedFile, socket);
                senderFile.start();
                try {
                    senderFile.join(); // Wait for the file sending to complete
                    System.out.println("Sent file: " + selectedFile.getName());
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                System.out.println("No file selected or not connected to server.");
            }

        }
    }


}