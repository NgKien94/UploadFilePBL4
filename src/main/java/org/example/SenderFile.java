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

        try{
            // Dùng để đọc tên tệp
            // File file = new File(filePath);
            BufferedInputStream serverRead = new BufferedInputStream(new FileInputStream(fileSend));

            // Tao outputStream de gui du lieu di
            OutputStream outputStream = socket.getOutputStream();


            PrintWriter printWriter = new PrintWriter(outputStream,true);
            printWriter.println(fileSend.getName());
            System.out.println("Gửi TÊN tệp hoàn tất.....");

            byte[] buffer = new byte[4096];
            int byteLength ;
            while((byteLength = serverRead.read(buffer)) !=-1){
                outputStream.write(buffer,0,byteLength);
            }
            System.out.println("Gửi tệp hoàn tất.");

//            printWriter.close();
//            serverRead.close();
//            outputStream.close();
            outputStream.flush();  // Đảm bảo toàn bộ dữ liệu được gửi
            System.out.println("Đã gửi tệp thành công.");


        }catch(Exception e){
            System.out.println("Error from SenderFile" +e.getMessage());
            System.out.println("Cause from SenderFile" +e.getCause());
        }

    }

    @Override
    public void run() {
        SocketSend(fileSend,socket);
    }
}