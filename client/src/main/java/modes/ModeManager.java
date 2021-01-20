package modes;

import exceptions.UnknownCommandException;
import receiver.Receiver;
import transmitter.Transmitter;

import java.io.IOException;
import java.util.Scanner;

public class ModeManager {
    private final Scanner terminalInput;
    private final Transmitter transmitter;

    public ModeManager() {
        this.transmitter = new Transmitter();

        Thread t = new Thread(transmitter);
        t.start();

        this.terminalInput = new Scanner(System.in);
    }

    public void showHelp() {
        System.out.println("How to use:");
        System.out.println("To serve a specific file:");
        System.out.println("\t~$torrent -setMode upload <FileName>");
        System.out.println("To download a specific file:");
        System.out.println("\t~$torrent -setMode download <FileName>");
    }

    public void getMode() {
        System.out.println("Enter Command:");
        ClientMode clientMode;
        String command = terminalInput.nextLine();
        //TODO make this more flexible:
        if (command.startsWith("torrent -setMode download ")) {
            clientMode = new Receiver(command.split(" ")[3]);
            Thread t = new Thread(clientMode);
            t.start();
        } else if (command.startsWith("torrent -setMode upload "))
            transmitter.addFile(command.split(" ")[3]);
        else
            throw new UnknownCommandException(command);
    }
}
