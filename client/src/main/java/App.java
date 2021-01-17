import config.ConfigLoader;
import modes.ClientModes;
import receiver.Receiver;
import transmitter.Transmitter;

import java.io.IOException;

public class App {

    public static void main(String[] args) {
        ConfigLoader configLoader = new ConfigLoader("config.properties");
        configLoader.load();

        Thread thread = new Thread(() -> {
            Transmitter t = new Transmitter();
            try {
                t.serveFile("uploadable-files/hi.txt");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        thread.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Receiver r = new Receiver();
        try {
            String data =r.receiveFile("hi.txt", ClientModes.Receiver.DOWNLOAD);
            System.out.println(data);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }


    }
}
