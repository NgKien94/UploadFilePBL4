package org.example;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerMain {
    public static void main(String[] args) {
        String filePath = "D:\\Video Android\\testFileTCP.txt";
       try{
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("Server created. Waiting for client.......");

            Socket socket = serverSocket.accept();
           System.out.println("Client connected");

           BufferedInputStream serverRead = new BufferedInputStream(new FileInputStream(filePath));

           // Tao outputStream de gui du lieu di
           OutputStream outputStream = socket.getOutputStream();

           // DataOutputStream được sử dụng để tự chuyển các kieu nguyên thủy vào outputstream

           DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

          byte[] buffer = new byte[4096];
          int byteLength ;
          while((byteLength = serverRead.read(buffer)) > 0){
              outputStream.write(buffer,0,byteLength);
          }
           System.out.println("Gửi tệp hoàn tất.");


        serverRead.close();
        outputStream.close();

       }catch(Exception e){
           System.out.println("Error from server" +e.getMessage());
           System.out.println("Cause from server" +e.getCause());
       }
    }
}
