package modes;

import java.io.IOException;

public interface ClientMode extends Runnable{

    enum Receiver {
        METADATA, DOWNLOAD
    }

    enum TRANSMITTER {
        UPLOAD
    }

}
