package receiver;

import modes.ClientModes;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.*;

public class ReceiverTest {


    @Test
    public void receiverSearch() {
        try {
            final Receiver receiver = new Receiver();
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        DatagramSocket socket = new DatagramSocket(8081, InetAddress.getByName("0.0.0.0"));
                        byte[] buf = new byte[100];
                        DatagramPacket packet = new DatagramPacket(buf, 0, buf.length);
                        socket.receive(packet);
                        String req_file = parseBytes(buf);
                        assertEquals(req_file, "GET hi.txt");
                        buf = "FOUND TEST CLIENT".getBytes();
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
            receiver.receiveFile("hi.txt", ClientModes.Receiver.SEARCH);


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
                        DatagramSocket socket = new DatagramSocket(8081, InetAddress.getByName("0.0.0.0"));
                        byte[] buf = new byte[100];
                        DatagramPacket packet = new DatagramPacket(buf, 0, buf.length);
                        System.out.println("Waiting for packet...");
                        socket.receive(packet);
                        String req_file = parseBytes(buf);
                        System.out.println("Request:" + req_file);

                        for (int i = 0; i < Math.ceil(data.length() / 8f); i++) {
                            String line = "Index:" + (i) + "@@@data:" + data.substring(i * 8, Math.min((i + 1) * 8, data.length()));
                            byte[] buf2 = line.getBytes();
                            DatagramPacket ppacket = new DatagramPacket(buf2, 0, buf2.length, packet.getAddress(), packet.getPort());
                            socket.send(ppacket);
                        }
                        buf = "#END".getBytes();
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
            String returnValue = receiver.receiveFile("hi.txt", ClientModes.Receiver.DOWNLOAD);
            assertEquals(data, returnValue);

        } catch (Exception e) {
            e.printStackTrace();

            fail();
        }
    }

    private String parseBytes(byte[] a) {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0) {
            ret.append((char) a[i]);
            i++;
        }
        return ret.toString();
    }
}
