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
    public Socket socket;

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

    public static void main(String[] args) {
        new ClientMain("Client");
    }

    public   void getFileTransfer( String ipServer,int PortServer){
        String filePathReceive = "E:\\DuAnTruongHoc\\Ky5\\TransferFile\\FileClientReceive\\";

        try{
            socket = new Socket(ipServer,PortServer);
            ReceiveFile receiveFile = new ReceiveFile(filePathReceive,socket);
            receiveFile.start();
            receiveFile.join();

        }catch (Exception e){
            System.out.println("Error from client " + e.getMessage());
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnConnect) {
            int PortServer = Integer.parseInt(txtPort.getText().trim());
            String ipServer = txtIP.getText().trim();
            txtFile.setText("No file chosen");
            // Sử dụng SwingWorker cho chuyển file
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    getFileTransfer(ipServer, PortServer);
                    return null;
                }

                @Override
                protected void done() {
                    JOptionPane.showMessageDialog(ClientMain.this, "Chuyển file hoàn tất.");
                }
            }.execute();
        } else if (e.getSource() == btnChoose) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn một file để gửi");
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                txtFile.setText(selectedFile.getName());
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            }
        } else if (e.getSource() == btnSend) {
            if (selectedFile != null) {
                // Sử dụng SwingWorker cho gửi file
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() {
                        try {
                            SenderFile senderFile = new SenderFile(selectedFile, socket);
                            senderFile.start();
                            senderFile.join();
                        } catch (Exception ex) {
                            System.out.println("Error from client Main Action Performed: " + ex.getMessage());
                        }
                        return null;
                    }

                    @Override
                    protected void done() {
                        JOptionPane.showMessageDialog(ClientMain.this, "File đã được gửi thành công.");
                    }
                }.execute();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một file trước.");
            }
        }
    }

}