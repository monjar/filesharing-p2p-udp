package protocol.dto;

import protocol.RequestBody;

public class NodeInputDto extends RequestBody {
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
