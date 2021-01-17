package file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {
    private File file;

    public FileHandler(String path) {
        file = new File(path);
        try {
            int filePostFix = 0;
            while (file.exists()) {
                filePostFix++;
                String[] split = path.split("\\.");
                String fileName = getPlusOneName(path, split, filePostFix);
                file = new File(fileName);
            }
            if (file.createNewFile())
                System.out.println("Created file: " + file.getName());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private String getPlusOneName(String path, String[] split, int filePostFix) {
        return path.substring(0, path.length() - split[split.length - 1].length() - 1) + filePostFix + "." + split[split.length - 1];
    }

    public void writeToFile(String input, boolean append) {
        try {

            FileWriter writer = new FileWriter(file);
            if (append)
                writer.append(input);
            else
                writer.write(input);

            writer.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
