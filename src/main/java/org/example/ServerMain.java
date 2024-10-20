package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;

public class ServerMain extends JFrame implements ActionListener {

    public TextField txtPort, txtFile;
    public JButton btnChoose, btnSend;
    private File selectedFile;

    public void GUI() {
        JLabel labelPort;
        JPanel pnTotal, pnTop, pnCenter;

        labelPort = new JLabel("PORT: ");
        txtPort = new TextField(15);
        btnChoose = new JButton("Choose");
        btnChoose.addActionListener(this);

        txtFile = new TextField(15);
        btnSend = new JButton("Send file");
        btnSend.addActionListener(this);


        pnTop = new JPanel(new FlowLayout(10));
        pnCenter = new JPanel(new FlowLayout(40, 40, 40));
        pnTotal = new JPanel(new BorderLayout());

        pnTop.add(labelPort);
        pnTop.add(txtPort);

        pnCenter.add(btnChoose);
        pnCenter.add(txtFile);
        pnCenter.add(btnSend);

        pnTotal.add(pnTop, BorderLayout.NORTH);
        pnTotal.add(pnCenter, BorderLayout.CENTER);

        add(pnTotal);
        setBounds(200, 200, 500, 350);
        setVisible(true);

    }

    public ServerMain(String st) {

        super(st);
        GUI();
    }

    public static void main(String[] args) {
        new ServerMain("Server");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnChoose) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn một file để gửi");
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                txtFile.setText(selectedFile.getName());
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            }
        } else if (e.getSource() == btnSend) {
            int portServer;
            if (!txtPort.getText().trim().isEmpty()) {
                portServer = Integer.parseInt(txtPort.getText().trim());
                // Sử dụng SwingWorker cho socket server và chuyển file
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() {
                        try {
                            ServerSocket serverSocket = new ServerSocket(portServer);
                            System.out.println("Server đã được tạo. Đang chờ kết nối từ client......");
                            Socket socket = serverSocket.accept();

                            SenderFile senderFile = new SenderFile(selectedFile, socket);
                            senderFile.start();
                            senderFile.join();

                            String filePathReceive = "E:\\DuAnTruongHoc\\Ky5\\TransferFile\\FileServerReceive\\";
                            ReceiveFile receiveFile = new ReceiveFile(filePathReceive, socket);
                            receiveFile.start();
                            receiveFile.join();

                        } catch (Exception ex) {
                            System.out.println("Error from server main " + ex.getMessage());
                        }
                        return null;
                    }

                    @Override
                    protected void done() {
                        JOptionPane.showMessageDialog(ServerMain.this, "Chuyển file hoàn tất.");
                    }
                }.execute();
            } else {
                System.out.println("Vui lòng nhập cổng server");
            }
        }
    }

}