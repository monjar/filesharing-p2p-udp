import config.ConfigLoader;
import config.Configs;
import file.FileHandler;
import modes.ClientModes;
import org.junit.Test;
import receiver.Receiver;
import util.DataHelpers;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ReceiverTest {
    private static String UDP_IP_LISTEN;
    private static int UDP_PORT;
    private static int RECEIVE_SIZE;
    private static int PACKET_SIZE;
    private static String REQUEST_PREFIX;

    public ReceiverTest() {
        ConfigLoader configLoader = new ConfigLoader("config.properties");
        configLoader.load();
        UDP_IP_LISTEN = Configs.getStr("udp.ip.listen");
        UDP_PORT = Configs.getInt("udp.port");
        RECEIVE_SIZE = Configs.getInt("udp.size.receive");
        REQUEST_PREFIX = Configs.getStr("file.req.prefix");
        PACKET_SIZE = Configs.getInt("udp.size.packet");
    }



    @Test
    public void receiverMetaData() {
        try {

            final Receiver receiver = new Receiver();
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        DatagramSocket socket = new DatagramSocket(UDP_PORT, InetAddress.getByName(UDP_IP_LISTEN));
                        byte[] buf = new byte[RECEIVE_SIZE];
                        DatagramPacket packet = new DatagramPacket(buf, 0, buf.length);
                        socket.receive(packet);
                        String req_file = DataHelpers.parseBytes(buf);
                        assertEquals(req_file, REQUEST_PREFIX + "hi.txt");
                        buf = "FOUND TEST CLIENT".getBytes(StandardCharsets.UTF_8);
                        packet = new DatagramPacket(buf, 0, buf.length, packet.getAddress(), packet.getPort());
                        socket.send(packet);
                        socket.close();

                    } catch (Exception e) {

                        e.printStackTrace();
                        fail();
                    }
                }
            });
            t.start();
            Thread.sleep(100);
            receiver.receiveFile("hi.txt", ClientModes.Receiver.METADATA);


        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void receiverDownloadFile() {
        try {

            final Receiver receiver = new Receiver();
            final String data = "This is \ndata of the @@@data@@@data file.";
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        DatagramSocket socket = new DatagramSocket(UDP_PORT, InetAddress.getByName(UDP_IP_LISTEN));
                        byte[] buf = new byte[RECEIVE_SIZE];
                        DatagramPacket packet = new DatagramPacket(buf, 0, buf.length);
                        socket.receive(packet);
                        String req_file = DataHelpers.parseBytes(buf);
                        buf = ("FOUND FILE " + "test.txt" + " - size: " + data.length()).getBytes();
                        packet = new DatagramPacket(buf, 0, buf.length, packet.getAddress(), packet.getPort());
                        socket.send(packet);
                        byte[] buf2 = data.getBytes();
                        DatagramPacket ppacket = new DatagramPacket(buf2, 0, buf2.length, packet.getAddress(), packet.getPort());
                        socket.send(ppacket);

                        socket.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                        fail();
                    }
                }
            });
            t.start();
            Thread.sleep(100);
            receiver.receiveFile("test.txt", ClientModes.Receiver.DOWNLOAD);
            FileHandler fileHandler2 = new FileHandler("test.txt");
            byte[] actual = fileHandler2.readByteFromFile(0, (int) fileHandler2.sizeInBytes());
            assertEquals(data, DataHelpers.parseBytes(actual));

        } catch (Exception e) {
            e.printStackTrace();

            fail();
        }
    }


}
