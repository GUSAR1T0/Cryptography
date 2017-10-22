/*
 * Copyright 2017 Roman Mashenkin
 * Licensed under the Apache License, Version 2.0
 */
package store.vxdesign.cryptography.framework.files;

import store.vxdesign.cryptography.framework.enums.AlgorithmType;
import store.vxdesign.cryptography.framework.enums.Cipher;

/**
 * @author Roman Mashenkin
 * @since 18.10.2017
 */
public class FileProperties {

    private final String filename;
    private final String directory;
    private final Cipher cipher;

    private AlgorithmType algorithmType;
    private String content;
    private String key;

    FileProperties(String filename, String directory, Cipher cipher) {
        this.filename = filename;
        this.directory = directory;
        this.cipher = cipher;
    }

    public String getFilename() {
        return filename;
    }

    public String getDirectory() {
        return directory;
    }

    public Cipher getCipher() {
        return cipher;
    }

    public AlgorithmType getAlgorithmType() {
        return algorithmType;
    }

    void setAlgorithmType(AlgorithmType algorithmType) {
        this.algorithmType = algorithmType;
    }

    public String getContent() {
        return content;
    }

    void setContent(String content) {
        this.content = content;
    }

    public String getKey() {
        return key;
    }

    void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "FileProperties [" +
                "filename='" + filename + '\'' +
                ", directory='" + directory + '\'' +
                ", cipher='" + cipher + '\'' +
                ", algorithmType='" + algorithmType + '\'' +
                ", content='" + content + '\'' +
                ", key='" + key + '\'' +
                ']';
    }
}
