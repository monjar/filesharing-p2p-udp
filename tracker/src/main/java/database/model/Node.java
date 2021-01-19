package database.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Node implements Serializable {

    public Node(String address, int downloadedFilesCount, int uploadedFilesCount) {
        this.address = address;
        this.downloadedFilesCount = downloadedFilesCount;
        this.uploadedFilesCount = uploadedFilesCount;
    }
    public Node() {
    }
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
