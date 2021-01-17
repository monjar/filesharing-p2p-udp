package modes;

import java.io.IOException;

public interface ClientMode {
    void startMode() throws IOException;

    enum Receiver {
        METADATA, DOWNLOAD
    }

    enum TRANSMITTER {
        UPLOAD
    }
}
