package org.example;

import java.io.*;
import java.net.*;
import java.util.*;

public class SenderFile extends Thread{
    private File fileSend;
    private Socket socket;


    public File getFileSend() {
        return fileSend;
    }

    public void setFileSend(File fileSend) {
        this.fileSend = fileSend;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }


    public SenderFile(File fileSend, Socket socket){
        this.fileSend = fileSend;
        this.socket = socket;

    }

    public static void SocketSend(File fileSend, Socket socket){
        try {
            BufferedInputStream serverRead = new BufferedInputStream(new FileInputStream(fileSend));
            OutputStream outputStream = socket.getOutputStream();

            PrintWriter printWriter = new PrintWriter(outputStream, true);

            // Gửi tên tệp
            printWriter.println(fileSend.getName());

            // Gửi kích thước tệp
            long fileSize = fileSend.length();
            printWriter.println(fileSize);
            System.out.println("Gửi TÊN tệp và KÍCH THƯỚC hoàn tất.....");

            byte[] buffer = new byte[65536];
            int byteLength;
            long totalBytesSent = 0;

            while ((byteLength = serverRead.read(buffer)) != -1) {
                outputStream.write(buffer, 0, byteLength);
                totalBytesSent += byteLength; // Cập nhật tổng số byte đã gửi
            }
            serverRead.close();

            outputStream.flush(); // Đảm bảo toàn bộ dữ liệu được gửi

            System.out.println("Đã gửi tệp thành công. Tổng số byte đã gửi: " + totalBytesSent);
        } catch (Exception e) {
            System.out.println("Error from SenderFile: " + e.getMessage());
            System.out.println("Cause from SenderFile: " + e.getCause());
        }

    }

    @Override
    public synchronized void run() {

        SocketSend(fileSend,socket);
    }
}