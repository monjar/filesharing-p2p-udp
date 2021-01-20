import config.ConfigLoader;
import config.Configs;
import file.FileHandler;
import modes.ClientMode;
import org.junit.Test;
import receiver.Receiver;
import util.DataHelpers;

import static org.junit.Assert.*;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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

            final Receiver receiver = new Receiver("hi.txt");
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        DatagramSocket socket = new DatagramSocket(UDP_PORT, InetAddress.getByName(UDP_IP_LISTEN));
                        byte[] buf = new byte[RECEIVE_SIZE];
                        DatagramPacket packet = new DatagramPacket(buf, 0, buf.length);
                        socket.receive(packet);
                        String req_file = DataHelpers.parseBytes(buf);
                        assertEquals(REQUEST_PREFIX + "hi.txt", req_file );
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
            receiver.receiveFile(ClientMode.Receiver.METADATA);


        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void receiverDownloadFile() {
        try {

            final Receiver receiver = new Receiver("test.txt");
            final String data = "This is \ndata of the @@@data@@@data file.";
            FileHandler fileHandler2 = new FileHandler("test.txt");
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
                        byte[] indexBuffer = DataHelpers.intToByteArr(0);
                        byte[] dataBuff = data.getBytes();
                        byte[] resBuf = new byte[indexBuffer.length + dataBuff.length];
                        System.arraycopy(indexBuffer, 0, resBuf, 0, indexBuffer.length);
                        System.arraycopy(dataBuff, 0, resBuf, indexBuffer.length, dataBuff.length);
                        DatagramPacket ppacket = new DatagramPacket(resBuf, 0, resBuf.length, packet.getAddress(), packet.getPort());
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
            receiver.receiveFile(ClientMode.Receiver.DOWNLOAD);
            byte[] actual = fileHandler2.readByteFromFile(0, (int) fileHandler2.sizeInBytes());
            assertEquals(data, DataHelpers.parseBytes(actual));

        } catch (Exception e) {
            e.printStackTrace();

            fail();
        }
    }


}
