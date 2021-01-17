package receiver;

import config.ConfigLoader;
import config.Configs;
import file.FileHandler;
import modes.ClientModes;
import util.DataHelpers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;

public class Receiver {

    private static String UDP_IP;
    private static int UDP_PORT;
    private static int RECEIVE_SIZE;
    private static String REQUEST_PREFIX;

    public Receiver() {
        ConfigLoader configLoader = new ConfigLoader("config.properties");
        configLoader.load();
        UDP_IP = Configs.getStr("udp.ip.broadcast");
        UDP_PORT = Configs.getInt("udp.port");
        RECEIVE_SIZE = Configs.getInt("udp.size.receive");
        REQUEST_PREFIX = Configs.getStr("file.req.prefix");

    }

    public String receiveFile(String file) throws IOException {
        return receiveFile(file, ClientModes.Receiver.DOWNLOAD);
    }

    public String receiveFile(String file, ClientModes.Receiver receiverMode) throws IOException {
        DatagramSocket udpSocket = new DatagramSocket();
        requestFileFromOthers(file, udpSocket);
        if (receiverMode == ClientModes.Receiver.SEARCH)
            return searchForFile(udpSocket);
        else
            return downloadFile(udpSocket, file);

    }

    private String downloadFile(DatagramSocket udpSocket, String fileName) throws IOException {
        String searchResult = searchForFile(udpSocket);
        int fileSize = Integer.parseInt(searchResult.split("- size: ")[1]);
        System.out.println("SIZE: " + fileSize);
        int downloadedBytes = 0;
        byte[] receive_bytes = new byte[RECEIVE_SIZE];
        FileHandler fileHandler = new FileHandler(fileName, true);
        while (fileSize != downloadedBytes) {
            int currentBufferSize = Math.min(RECEIVE_SIZE, fileSize - downloadedBytes);
            receive_bytes = new byte[currentBufferSize];
            downloadedBytes += currentBufferSize;
//            System.out.print( "\r"+downloadedBytes+"/"+fileSize );
            DatagramPacket receivePacket = new DatagramPacket(receive_bytes, receive_bytes.length);
            udpSocket.receive(receivePacket);
            fileHandler.writeToFile(receive_bytes, true);
        }
        fileHandler.closeWriter();
        if(downloadedBytes == fileSize)
            return "File downloaded completely";
        else
            return "Couldn't download file completely";
    }


    private String searchForFile(DatagramSocket udpSocket) throws IOException {
        byte[] receive_bytes = new byte[RECEIVE_SIZE];
        DatagramPacket receivePacket = new DatagramPacket(receive_bytes, receive_bytes.length);
        udpSocket.receive(receivePacket);
        return DataHelpers.parseBytes(receive_bytes);
    }

    private void requestFileFromOthers(String file, DatagramSocket udpSocket) throws IOException {
        String requestString = REQUEST_PREFIX + file;
        byte[] requestBytes = requestString.getBytes();
        DatagramPacket packet = new DatagramPacket(requestBytes, 0, requestBytes.length,
                InetAddress.getByName(UDP_IP), UDP_PORT);
        udpSocket.send(packet);
    }


}
