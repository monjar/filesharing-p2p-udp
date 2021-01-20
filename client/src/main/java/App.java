import config.ConfigLoader;
import modes.ClientMode;
import modes.ModeManager;
import receiver.Receiver;
import trackerapi.TrackerConnector;
import transmitter.Transmitter;

import java.io.IOException;

public class App {

    public static void main(String[] args) {
        TrackerConnector.getInstance().registerNode();
        ModeManager modeManager = new ModeManager();
        modeManager.showHelp();

        while (true){
            TrackerConnector.getInstance().getAllData();
            modeManager.getMode();
            waitASec();
        }

    }

    public static void waitASec(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
