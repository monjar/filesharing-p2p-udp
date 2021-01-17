import file.FileHandler;
import modes.ClientModes;
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
            try {
                t.serveFile("uploadable-files/aaa.png");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        thread.start();
        Thread.sleep(100);
        Receiver r = new Receiver();
        r.receiveFile("aaa.png");

        FileHandler fileHandler1 = new FileHandler("uploadable-files/aaa.png");
        FileHandler fileHandler2 = new FileHandler("uploadable-files/aaa.png");
        byte[] expected = fileHandler1.readByteFromFile(0, (int) fileHandler1.sizeInBytes());
        byte[] actual = fileHandler2.readByteFromFile(0, (int) fileHandler2.sizeInBytes());

        assertArrayEquals(expected, actual);
    }
}
