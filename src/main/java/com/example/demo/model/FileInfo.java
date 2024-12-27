package com.example.demo.model;

public class FileInfo {

    private final String fileName;
    private final String fileSize;
    private final String fileDate;

    public FileInfo(String name, String size, String lastModified) {
        this.fileName = name;
        this.fileSize = size;
        this.fileDate = lastModified;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getFileDate() {
        return fileDate;
    }
}
