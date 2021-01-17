import config.ConfigLoader;
import modes.ClientMode;
import modes.ModeManager;
import receiver.Receiver;
import transmitter.Transmitter;

import java.io.IOException;

public class App {

    public static void main(String[] args) {
        ModeManager modeManager = new ModeManager();
        modeManager.showHelp();
        ClientMode clientMode = modeManager.getMode();
        try {
            clientMode.startMode();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
