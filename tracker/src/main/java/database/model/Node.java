package database.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @ElementCollection( targetClass = java.lang.String.class)
    List<String> servedFiles = new ArrayList<>();

    private int downloadedFilesCount;
    private int uploadedFilesCount;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getServedFiles() {
        return servedFiles;
    }

    public void setServedFiles(List<String> servedFiles) {
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

    public void  addServedFile(String filename){
        this.servedFiles.add(filename);
    }
}
