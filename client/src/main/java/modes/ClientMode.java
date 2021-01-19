package modes;

import java.io.IOException;

public interface ClientMode extends Runnable{
    void startMode() throws IOException;

    enum Receiver {
        METADATA, DOWNLOAD
    }

    enum TRANSMITTER {
        UPLOAD
    }

}
