package receiver;

import modes.ClientModes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Receiver {


    Receiver(){
    }

    public String receiveFile(String file, ClientModes.Receiver receiverMode){
        try {
            DatagramSocket udpSocket = new DatagramSocket();
            String requestString = "GET " + file;
            byte[] requestBytes = requestString.getBytes();
            DatagramPacket packet = new DatagramPacket(requestBytes,0,requestBytes.length, InetAddress.getByName("255.255.255.255"), 8081);
            udpSocket.send(packet);
            StringBuilder dataBuilder = new StringBuilder();
            byte[] receive_bytes = new byte[100];
            if(receiverMode == ClientModes.Receiver.SEARCH){
                DatagramPacket receivePacket = new DatagramPacket(receive_bytes, receive_bytes.length);
                udpSocket.receive(receivePacket);
                System.out.println("Recieved packet: " + parseBytes(receive_bytes));
                return "";
            }
            while (!parseBytes(receive_bytes).equals("#END")){
                receive_bytes = new byte[100];
                DatagramPacket receivePacket = new DatagramPacket(receive_bytes, receive_bytes.length);
                udpSocket.receive(receivePacket);
                String receivedString =  parseBytes(receive_bytes);
                String[] splited = receivedString.split("@@@data:");
                String indexString = splited[0];
                for (int i = 1; i <splited.length ; i++) {
                    dataBuilder.append(splited[i]);
                    if(i !=splited.length -1 )
                        dataBuilder.append("@@@data:");
                }
                //TODO: add to previous  data
            }
            System.out.println(dataBuilder.toString());
            return dataBuilder.toString();
            //TODO: write to file

        }catch (Exception ioException){
            ioException.printStackTrace();
        }

        return "Hi";
    }
    private String parseBytes(byte[] a) {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret.toString();
    }
}
