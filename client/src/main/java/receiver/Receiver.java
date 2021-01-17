package receiver;

import config.ConfigLoader;
import config.Configs;
import modes.ClientModes;
import util.DataHelpers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Receiver {

    private static String UDP_IP;
    private static int UDP_PORT;
    private static int RECEIVE_SIZE;
    private static String REQUEST_PREFIX;
    private static String DATA_SPLITTER;
    private static String END_IDENTIFIER;

    Receiver() {
        ConfigLoader configLoader = new ConfigLoader("config.properties");
        configLoader.load();
        UDP_IP = Configs.getStr("udp.ip");
        UDP_PORT = Configs.getInt("udp.port");
        RECEIVE_SIZE = Configs.getInt("udp.size.receive");
        REQUEST_PREFIX = Configs.getStr("file.req.prefix");
        DATA_SPLITTER = Configs.getStr("file.protocol.split");
        END_IDENTIFIER = Configs.getStr("file.protocol.end");
    }

    public String receiveFile(String file, ClientModes.Receiver receiverMode) throws IOException {

        DatagramSocket udpSocket = new DatagramSocket();
        requestFileFromOthers(file, udpSocket);
        if (receiverMode == ClientModes.Receiver.SEARCH)
            return searchForFile(udpSocket);
        else
            return downloadFile(udpSocket);

    }

    private String downloadFile(DatagramSocket udpSocket) throws IOException {
        StringBuilder dataBuilder = new StringBuilder();
        byte[] receive_bytes = new byte[RECEIVE_SIZE];
        while (dataIsComing(receive_bytes)) {
            receive_bytes = new byte[RECEIVE_SIZE];
            String[] split = downloadNewPacket(udpSocket, receive_bytes);
            String indexString = split[0];
            appendToPreviousData(dataBuilder, split);
        }
        System.out.println("Downloaded data: " + dataBuilder.toString());
        //TODO: write to file
        return dataBuilder.toString();
    }

    private boolean dataIsComing(byte[] receive_bytes) {
        return !DataHelpers.parseBytes(receive_bytes).equals(END_IDENTIFIER);
    }

    private String[] downloadNewPacket(DatagramSocket udpSocket, byte[] receive_bytes) throws IOException {
        DatagramPacket receivePacket = new DatagramPacket(receive_bytes, receive_bytes.length);
        udpSocket.receive(receivePacket);
        String receivedString = DataHelpers.parseBytes(receive_bytes);
        return receivedString.split(DATA_SPLITTER);
    }

    private void appendToPreviousData(StringBuilder dataBuilder, String[] split) {
        for (int i = 1; i < split.length; i++) {
            dataBuilder.append(split[i]);
            if (i != split.length - 1)
                dataBuilder.append(DATA_SPLITTER);
        }
    }

    private String searchForFile(DatagramSocket udpSocket) throws IOException {
        byte[] receive_bytes = new byte[RECEIVE_SIZE];
        DatagramPacket receivePacket = new DatagramPacket(receive_bytes, receive_bytes.length);
        udpSocket.receive(receivePacket);
        return "Found packet: " + DataHelpers.parseBytes(receive_bytes);
    }

    private void requestFileFromOthers(String file, DatagramSocket udpSocket) throws IOException {
        String requestString = REQUEST_PREFIX + file;
        byte[] requestBytes = requestString.getBytes();
        DatagramPacket packet = new DatagramPacket(requestBytes, 0, requestBytes.length,
                InetAddress.getByName(UDP_IP), UDP_PORT);
        udpSocket.send(packet);
    }

}
