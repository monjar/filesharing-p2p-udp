package trackerapi;

import config.ConfigLoader;
import config.Configs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class TrackerConnector {

    private static volatile TrackerConnector instance;
    private final String TRACKER_IP;
    private final int TRACKER_PORT;
    private final int UDP_PORT;
    private TrackerConnector() {
        ConfigLoader configLoader = new ConfigLoader("config.properties");
        configLoader.load();
        TRACKER_IP = Configs.getStr("tracker.ip");
        TRACKER_PORT = Configs.getInt("tracker.port");
        UDP_PORT = Configs.getInt("udp.port");
    }

    synchronized public static TrackerConnector getInstance() {
        if (instance == null)
            instance = new TrackerConnector();
        return instance;
    }

    public void registerNode()
    {
        try {
            Socket socket = new Socket(TRACKER_IP, TRACKER_PORT);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            dos.writeUTF("{\"method\":\"ADD\", \"targetRoute\":\"/\", \"body\":{\"address\":\"addressTest0\"}, \"port\":" + UDP_PORT +  "}");
            String response = dis.readUTF();
            System.out.println("Response Register: " + response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addServedFile(String fileName)
    {
        try {
            Socket socket = new Socket(TRACKER_IP, TRACKER_PORT);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            dos.writeUTF("{\"method\":\"ADD\", \"targetRoute\":\"/addServedFile\", \"body\":{\"fileName\":\""+fileName+"\"}, \"port\":" + UDP_PORT +  "}");
            String response = dis.readUTF();
            System.out.println("Response Add file: " + response);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addUploadedFile()
    {
        try {
            Socket socket = new Socket(TRACKER_IP, TRACKER_PORT);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            dos.writeUTF("{\"method\":\"ADD\", \"targetRoute\":\"/addUploadedFile\", \"body\":null, \"port\":" + UDP_PORT +  "}");
            String response = dis.readUTF();
            System.out.println("Response Add uploaded: " + response);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void addDownloadedFile()
    {
        try {
            Socket socket = new Socket(TRACKER_IP, TRACKER_PORT);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            dos.writeUTF("{\"method\":\"ADD\", \"targetRoute\":\"/addDownloadedFile\", \"body\":null, \"port\":" + UDP_PORT +  "}");
            String response = dis.readUTF();
            System.out.println("Response Add downloaded: " + response);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public String getAllData()
    {
        try {
            Socket socket = new Socket(TRACKER_IP, TRACKER_PORT);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            dos.writeUTF("{\"method\":\"GET\", \"targetRoute\":\"/\", \"body\":null, \"port\":" + UDP_PORT +  "}");
            String response = dis.readUTF();
            System.out.println("Response get all:" + response);
            return response;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
