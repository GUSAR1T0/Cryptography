package store.vxdesign.cryptography.framework.files;

import store.vxdesign.cryptography.algorithms.AlgorithmType;

/**
 * @author Roman Mashenkin
 * @since 18.10.2017
 */
public class FileProperties {

    private final String filename;
    private final String directory;

    private AlgorithmType algorithmType;
    private String content;
    private String key;

    FileProperties(String filename, String directory) {
        this.filename = filename;
        this.directory = directory;
    }

    public String getFilename() {
        return filename;
    }

    public String getDirectory() {
        return directory;
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
                ", algorithmType='" + algorithmType + '\''+
                ", content='" + content + '\'' +
                ", key='" + key + '\'' +
                ']';
    }
}
