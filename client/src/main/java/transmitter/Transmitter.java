package transmitter;

import config.ConfigLoader;
import config.Configs;
import file.FileHandler;
import modes.ClientMode;
import util.DataHelpers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Transmitter implements ClientMode {

    private static String UDP_IP_LISTEN;
    private static int UDP_PORT;
    private static int RECEIVE_SIZE;
    private static String REQUEST_PREFIX;
    private boolean isServing = true;
    private String fileName = "";

    @Override
    public void run() {

    }

    @Override
    public void startMode() throws IOException {
        serveFile();
    }
// TODO add multiple files
    public Transmitter(String fileName ) {
        ConfigLoader configLoader = new ConfigLoader("config.properties");
        configLoader.load();
        UDP_IP_LISTEN = Configs.getStr("udp.ip.listen");
        UDP_PORT = Configs.getInt("udp.port");
        RECEIVE_SIZE = Configs.getInt("udp.size.receive");
        REQUEST_PREFIX = Configs.getStr("file.req.prefix");
        this.fileName = fileName;
    }

    public void serveFile() throws IOException {
        FileHandler fileHandler = new FileHandler(fileName);
        DatagramSocket socket = new DatagramSocket(UDP_PORT, InetAddress.getByName(UDP_IP_LISTEN));
        byte[] buf = new byte[RECEIVE_SIZE];
        DatagramPacket packet = new DatagramPacket(buf, 0, buf.length);
        while (isServing) {
            waitForValidRequest(fileName, socket, buf, packet);
            int destPort = packet.getPort();
            InetAddress destIp = packet.getAddress();
            UploadHandler handler = new UploadHandler(fileName, fileHandler, socket, destIp, destPort);
            handler.start();
        }
//        socket.close();
    }


    private void waitForValidRequest(String file, DatagramSocket socket, byte[] buf, DatagramPacket packet) throws IOException {
        socket.receive(packet);
        String req_file = DataHelpers.parseBytes(buf);
        while (areRequestsWrong(file, req_file)) {
            socket.receive(packet);
            req_file = DataHelpers.parseBytes(buf);
        }
    }

    private boolean areRequestsWrong(String file, String req_file) {
        return !req_file.startsWith(REQUEST_PREFIX) || !req_file.substring(REQUEST_PREFIX.length()).equals(file.split("/")[1]);
    }


}
