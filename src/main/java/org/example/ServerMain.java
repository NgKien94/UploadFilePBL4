package org.example;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerMain {



    public static void main(String[] args) {
        //String filePath = "D:\\Video Android\\testFileTCP.txt";
      //  String filePath = "D:\\Video Android\\anh1.jpg";
        String filePath = "D:\\Video Android\\MyCalculator.mp4";
       try{
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("Server created. Waiting for client.......");

            Socket socket = serverSocket.accept();
           System.out.println("Client connected");


            // Dùng để đọc tên tệp
           File file = new File(filePath);
           BufferedInputStream serverRead = new BufferedInputStream(new FileInputStream(file));

           // Tao outputStream de gui du lieu di
           OutputStream outputStream = socket.getOutputStream();


           PrintWriter printWriter = new PrintWriter(outputStream,true);
           printWriter.println(file.getName());
           System.out.println("Gửi TÊN tệp hoàn tất.....");

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
