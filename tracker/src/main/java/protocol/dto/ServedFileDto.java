package protocol.dto;

import protocol.RequestBody;

public class ServedFileDto extends RequestBody {

    private String fileName;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
