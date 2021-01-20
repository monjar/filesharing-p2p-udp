import file.FileHandler;
import org.junit.Test;
import receiver.Receiver;
import transmitter.Transmitter;

import java.io.IOException;

import static org.junit.Assert.*;

public class TransmitterTest {


    @Test
    public void transmitDataCheck() throws Exception {
        Thread thread = new Thread(() -> {
            Transmitter t = new Transmitter();
            t.addFile("aaa.png");
            try {
                t.serveFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        thread.start();
        Thread.sleep(100);
        Receiver r = new Receiver("aaa.png");
        r.receiveFile();

        FileHandler fileHandler1 = new FileHandler("uploadable-files/aaa.png");
        FileHandler fileHandler2 = new FileHandler("uploadable-files/aaa.png");
        byte[] expected = fileHandler1.readByteFromFile(0, (int) fileHandler1.sizeInBytes());
        byte[] actual = fileHandler2.readByteFromFile(0, (int) fileHandler2.sizeInBytes());

        assertArrayEquals(expected, actual);
    }
}
