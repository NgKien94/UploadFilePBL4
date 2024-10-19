package org.example;

import java.util.*;
import java.io.*;
import java.net.*;

public class ClientMain {
    public static void main(String[] args) {
        try{
            Socket socket = new Socket("localhost",9999);

            InputStream inputStream = socket.getInputStream();
           // DataInputStream dataInputStream = new DataInputStream(inputStream);


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[4096];
            int byteLength;
            while( (byteLength = inputStream.read(buffer)) > 0){
                System.out.println("Vao vong lap");
                byteArrayOutputStream.write(buffer,0,byteLength);
            }
            String fileContent = byteArrayOutputStream.toString("UTF-8");
            System.out.println("Nội dung tệp: ");
            System.out.println(fileContent);



            // Đóng các luồng và socket
            byteArrayOutputStream.close();
            inputStream.close();
            socket.close();

        }catch (Exception e){
           System.out.println("Error from client " + e.getMessage());
        }
    }
}
