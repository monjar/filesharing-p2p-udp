package transmitter;


import config.Configs;
import file.FileHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UploadHandler extends Thread{
    private final String fileName;
    private final FileHandler fileHandler;
    private final DatagramSocket socket;
    private final InetAddress destAddr;
    private final int destPort;
    private final int PACKET_SIZE;

    public UploadHandler(String fileName, FileHandler fileHandler, DatagramSocket socket, InetAddress destAddr, int destPort) {
        this.fileName = fileName;
        this.fileHandler = fileHandler;
        this.socket = socket;
        this.destAddr = destAddr;
        this.destPort = destPort;
        PACKET_SIZE = Configs.getInt("udp.size.packet");
    }



    @Override
    public void run() {
        super.run();
        try {
            this.sendFileMetaData();
            this.sendFileData();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendFileMetaData() throws IOException {
        byte[] buf;
        buf = ("FOUND FILE " + fileName + " - size: " + fileHandler.sizeInBytes()).getBytes();
        DatagramPacket packet = new DatagramPacket(buf, 0, buf.length, destAddr, destPort);
        socket.send(packet);

    }

    public void sendFileData() throws IOException {
        for (int i = 0; i < Math.ceil(fileHandler.sizeInBytes() / (float) PACKET_SIZE); i++) {
            byte[] buf2 = fileHandler.readByteFromFile(i * PACKET_SIZE, PACKET_SIZE);
            DatagramPacket packet = new DatagramPacket(buf2, 0, buf2.length, destAddr, destPort);
            socket.send(packet);
        }
    }
}
