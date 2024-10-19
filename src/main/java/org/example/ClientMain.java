package org.example;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;
import java.net.*;

public class ClientMain {
    public static void main(String[] args) {
        String filePathReceive = "E:\\DuAnTruongHoc\\Ky5\\TransferFile\\";

        try{
            Socket socket = new Socket("localhost",9999);

            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);// Dùng để nhận tên tệp





           String fileName = dataInputStream.readUTF();
           System.out.println("Client nhận tên tệp : "+fileName);


            File saveFile = new File(filePathReceive + fileName);
            if(saveFile.exists()){

//                String newNameFile = createNewFile(fileName);
//                saveFive = new File(filePathReceive + newNameFile);
                String newNameFile = createNewFile(saveFile);
                saveFile = new File(filePathReceive + newNameFile);


            }


            BufferedOutputStream writeToReceiveFile = new BufferedOutputStream(new FileOutputStream(saveFile));

           byte[] readByte = new byte[4096];
           int byteLength;
           while( (byteLength = inputStream.read(readByte) ) > 0){
               writeToReceiveFile.write(readByte,0,byteLength);

           }

           writeToReceiveFile.close();
           inputStream.close();

        }catch (Exception e){
           System.out.println("Error from client " + e.getMessage());
        }
    }

    public static String createNewFile(File oldFile){
        String newName ="";
        String fileInfo = oldFile.getName();
        String nameOldFile ="";
        String extensionOldFile = "";

        for(int i = 0 ;i < fileInfo.length() ;i++){
            if(fileInfo.charAt(i) == '.'){
                nameOldFile = fileInfo.substring(0,i);
                extensionOldFile = fileInfo.substring(i);
                break;
            }
        }

        int counter = 1;
        do{
            newName="";
            newName += nameOldFile + "_"+counter + extensionOldFile;
            counter++;
        }while(new File(oldFile.getParent(),newName).exists()); // nếu file tồn tại thì tiếp tục lặp counter

        return newName;
    }
}
