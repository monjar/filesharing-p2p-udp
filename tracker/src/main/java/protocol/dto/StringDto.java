package protocol.dto;

import protocol.Response;

public class StringDto extends Response {
    private String data;

    public StringDto(String data) {
        this.data = data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
