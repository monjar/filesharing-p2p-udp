package transmitter;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Transmitter implements ClientMode {

    private static String UDP_IP_LISTEN;
    private static int UDP_PORT;
    private static int RECEIVE_SIZE;
    private static String REQUEST_PREFIX;
    private boolean isServing = true;
    private Set<String> files;


    @Override
    public void run() {

        try {
            serveFile();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    // TODO add multiple files
    public Transmitter() {
        files = new HashSet<>();
        ConfigLoader configLoader = new ConfigLoader("config.properties");
        configLoader.load();
        UDP_IP_LISTEN = Configs.getStr("udp.ip.listen");
        UDP_PORT = Configs.getInt("udp.port");
        RECEIVE_SIZE = Configs.getInt("udp.size.receive");
        REQUEST_PREFIX = Configs.getStr("file.req.prefix");
    }

    public void serveFile() throws IOException {
        DatagramSocket socket = new DatagramSocket(UDP_PORT, InetAddress.getByName(UDP_IP_LISTEN));
        byte[] buf = new byte[RECEIVE_SIZE + 4];
        DatagramPacket packet = new DatagramPacket(buf, 0, buf.length);
        while (isServing) {
            String fileName = waitForValidRequest(socket, buf, packet);
            int destPort = packet.getPort();
            InetAddress destIp = packet.getAddress();
            UploadHandler handler = new UploadHandler(fileName, socket, destIp, destPort);
            handler.start();
        }
        socket.close();
    }

    public void addFile(String fileName) {
        if (fileName.split("/").length > 1)
            this.files.add(fileName.split("/")[1]);
        else
            this.files.add(fileName);
        TrackerConnector.getInstance().addServedFile(fileName);
    }

    private String waitForValidRequest(DatagramSocket socket, byte[] buf, DatagramPacket packet) throws IOException {
        socket.receive(packet);
        String req_file = DataHelpers.parseBytes(buf);
        System.out.println(req_file);
        while (areRequestsWrong(req_file)) {
            socket.receive(packet);
            req_file = DataHelpers.parseBytes(buf);
        }
        return req_file.substring(REQUEST_PREFIX.length());
    }

    private boolean areRequestsWrong(String req_file) {
        return !req_file.startsWith(REQUEST_PREFIX) || !files.contains(req_file.substring(REQUEST_PREFIX.length()));
    }


}
