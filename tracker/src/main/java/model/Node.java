package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Node implements Serializable {

    @Id
    private String address;

    @ElementCollection
    Set<String> servedFiles = new HashSet<String>();

    private int downloadedFilesCount;
    private int uploadedFilesCount;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<String> getServedFiles() {
        return servedFiles;
    }

    public void setServedFiles(Set<String> servedFiles) {
        this.servedFiles = servedFiles;
    }

    public int getDownloadedFilesCount() {
        return downloadedFilesCount;
    }

    public void setDownloadedFilesCount(int downloadedFilesCount) {
        this.downloadedFilesCount = downloadedFilesCount;
    }

    public int getUploadedFilesCount() {
        return uploadedFilesCount;
    }

    public void setUploadedFilesCount(int uploadedFilesCount) {
        this.uploadedFilesCount = uploadedFilesCount;
    }
}
