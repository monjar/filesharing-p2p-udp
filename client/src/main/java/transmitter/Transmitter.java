package transmitter;

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

public class Transmitter {

    private static String UDP_IP_LISTEN;
    private static int UDP_PORT;
    private static int RECEIVE_SIZE;
    private static int PACKET_SIZE;
    private static String REQUEST_PREFIX;

    public Transmitter() {
        ConfigLoader configLoader = new ConfigLoader("config.properties");
        configLoader.load();
        UDP_IP_LISTEN = Configs.getStr("udp.ip.listen");
        UDP_PORT = Configs.getInt("udp.port");
        RECEIVE_SIZE = Configs.getInt("udp.size.receive");
        REQUEST_PREFIX = Configs.getStr("file.req.prefix");
        PACKET_SIZE = Configs.getInt("udp.size.packet");
    }

    public void serveFile(String file) throws IOException {
        FileHandler fileHandler = new FileHandler(file);
        DatagramSocket socket = new DatagramSocket(UDP_PORT, InetAddress.getByName(UDP_IP_LISTEN));
        byte[] buf = new byte[RECEIVE_SIZE];
        DatagramPacket packet = new DatagramPacket(buf, 0, buf.length);

        waitForValidRequest(file, socket, buf, packet);
        sendFileMetaData(file, fileHandler, socket, packet);
        sendFileData(fileHandler, socket, packet);
        socket.close();
    }

    private void sendFileMetaData(String file, FileHandler fileHandler, DatagramSocket socket, DatagramPacket packet) throws IOException {
        byte[] buf;
        buf = ("FOUND FILE "+ file +" - size: " + fileHandler.sizeInBytes()).getBytes() ;
        packet = new DatagramPacket(buf, 0, buf.length, packet.getAddress(), packet.getPort());
        socket.send(packet);

    }

    private void sendFileData(FileHandler fileHandler, DatagramSocket socket, DatagramPacket packet) throws IOException {
        for (int i = 0; i < Math.ceil(fileHandler.sizeInBytes() / (float)PACKET_SIZE); i++) {
            byte[] buf2 = fileHandler.readByteFromFile(i * PACKET_SIZE, PACKET_SIZE);
            DatagramPacket ppacket = new DatagramPacket(buf2, 0, buf2.length, packet.getAddress(), packet.getPort());
            socket.send(ppacket);
        }
    }

    private void waitForValidRequest(String file, DatagramSocket socket, byte[] buf, DatagramPacket packet) throws IOException {
        System.out.println("WAITING FOR REQUEST");
        socket.receive(packet);
        String req_file = DataHelpers.parseBytes(buf);
        System.out.println("REQUEST ENTERED: " + req_file);
        while (areRequestsWrong(file, req_file)){
            System.out.println("REQUEST WAS WRONG");
            socket.receive(packet);
            req_file = DataHelpers.parseBytes(buf);
        }
    }

    private boolean areRequestsWrong(String file, String req_file) {
        return !req_file.startsWith(REQUEST_PREFIX) || !req_file.substring(REQUEST_PREFIX.length()).equals(file.split("/")[1]);
    }


}
