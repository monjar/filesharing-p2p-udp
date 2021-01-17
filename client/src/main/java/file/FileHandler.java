package file;

import util.DataHelpers;

import java.io.*;

public class FileHandler {
    private File file;
    FileOutputStream f;

    public FileHandler(String path) {
        this(path, false);

    }

    public FileHandler(String path, boolean create) {
        try {
            file = new File(path);
            if (create)
                f = new FileOutputStream(file);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private String getPlusOneName(String path, String[] split, int filePostFix) {
        return path.substring(0, path.length() - split[split.length - 1].length() - 1) + filePostFix + "." + split[split.length - 1];
    }

    public synchronized void writeToFile(byte[] input, boolean append) {
        try {

            f.write(input);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    public void closeWriter(){
        try {
            f.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public long sizeInBytes() {
        return this.file.length();
    }

    public synchronized byte[] readByteFromFile(int start, int offset) {
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            raf.seek(start);
            if (offset >= this.file.length() || start + offset >= this.file.length())
                offset = (int) this.file.length() - start;

            byte[] buffer = new byte[offset];
            raf.readFully(buffer);
            return buffer;
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return null;
        }
    }


}
