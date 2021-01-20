package receiver;

import config.ConfigLoader;
import config.Configs;
import file.FileHandler;
import modes.ClientMode;
import trackerapi.TrackerConnector;
import util.DataHelpers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class Receiver implements ClientMode {

    private static String UDP_IP;
    private static int UDP_PORT;
    private static int RECEIVE_SIZE;
    private static int PACKET_SIZE;
    private static String REQUEST_PREFIX;
    private final String fileName;

    public Receiver(String fileName) {
        ConfigLoader configLoader = new ConfigLoader("config.properties");
        configLoader.load();
        UDP_IP = Configs.getStr("udp.ip.broadcast");
        UDP_PORT = Configs.getInt("udp.port");
        RECEIVE_SIZE = Configs.getInt("udp.size.receive");
        REQUEST_PREFIX = Configs.getStr("file.req.prefix");
        PACKET_SIZE = Configs.getInt("udp.size.packet");
        this.fileName = fileName;
    }


    @Override
    public void run() {
        try {
            receiveFile();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public String receiveFile() throws IOException {
        return receiveFile(ClientMode.Receiver.DOWNLOAD);
    }

    public String receiveFile(ClientMode.Receiver receiverMode) throws IOException {
        DatagramSocket udpSocket = new DatagramSocket();
        requestFileFromOthers(fileName, udpSocket);
        String result;
        if (receiverMode == ClientMode.Receiver.METADATA)
            result = getFileMetaData(udpSocket);
        else
            result = downloadFile(udpSocket, fileName);

        TrackerConnector.getInstance().addDownloadedFile();
        return result;

    }

    private String downloadFile(DatagramSocket udpSocket, String fileName) throws IOException {

        String searchResult = getFileMetaData(udpSocket);
        System.out.println(searchResult);
        int fileSize = Integer.parseInt(searchResult.split("- size: ")[1]), downloadedBytes = 0;
        FileHandler fileHandler = new FileHandler("downloaded-files/" + fileName, true);
        while (fileSize > downloadedBytes) {
            int currentBufferSize = Math.min(PACKET_SIZE, fileSize - downloadedBytes) + 4;
            byte[] receive_bytes = new byte[currentBufferSize];
            downloadedBytes += currentBufferSize - 4;
            DatagramPacket receivePacket = new DatagramPacket(receive_bytes, receive_bytes.length);
            udpSocket.receive(receivePacket);
            byte[] indexBytes = new byte[4];
            System.arraycopy(receive_bytes, 0, indexBytes, 0, indexBytes.length);
            int index = DataHelpers.byteArrToInt(indexBytes);
            System.out.println("Got part: " + index);
            byte[] dataBytes = new byte[receive_bytes.length - indexBytes.length];
            System.arraycopy(receive_bytes, indexBytes.length, dataBytes, 0, dataBytes.length);
            fileHandler.writeToFile(dataBytes, true);
        }
        fileHandler.closeWriter();
        return "File downloaded completely";
    }


    private String getFileMetaData(DatagramSocket udpSocket) throws IOException {
        byte[] receive_bytes = new byte[PACKET_SIZE + 4];
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
