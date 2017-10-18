package store.vxdesign.cryptography.framework.files;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import store.vxdesign.cryptography.algorithms.AlgorithmType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Roman Mashenkin
 * @since 17.10.2017
 */
public final class FilesReader {

    private static final Logger LOGGER = LogManager.getLogger(FilesReader.class);
    private static final String RESOURCES_DIRECTORY = "src/main/resources/";
    private static final String ENCRYPT_DIRECTORY = Paths.get(RESOURCES_DIRECTORY, "encrypt").toString();

    /**
     * Hidden constructor.
     */
    private FilesReader() {
    }

    public static List<FileProperties> readFiles(String[] paths) throws FileNotFoundException {
        List<File> files = Arrays.stream(paths).map(File::new).collect(Collectors.toList());
        List<FileProperties> filePropertiesList = new ArrayList<>();

        if (files.isEmpty()) {
            files.addAll(getAllFilesFromEncryptDirectory());
        }

        for (File file : files) {
            if (file.exists() && file.isFile()) {
                String filenameWithoutExtension = getFilenameWithoutExtension(file);
                FileProperties fileProperties = filePropertiesList.stream()
                        .filter(properties -> properties.getFilename().equals(filenameWithoutExtension))
                        .findFirst()
                        .orElse(new FileProperties(filenameWithoutExtension, file.getParent()));

                FileExtension extension = getFileExtensionInstance(file);
                if (FileExtension.TEXT.equals(extension)) {
                    fileProperties.setAlgorithmType(AlgorithmType.value(getFileExtension(file)));
                    fileProperties.setContent(readFile(file));
                } else if (FileExtension.KEY.equals(extension)) {
                    fileProperties.setKey(readFile(file));
                }

                if (!filePropertiesList.contains(fileProperties)) {
                    filePropertiesList.add(fileProperties);
                }
            } else {
                throw new FileNotFoundException(
                        String.format("Path to file '%s' from arguments is not correct.", file.getPath())
                );
            }
        }

        return filePropertiesList;
    }

    private static List<File> getAllFilesFromEncryptDirectory() {
        File directory = new File(ENCRYPT_DIRECTORY);
        if (directory.exists() && directory.isDirectory()) {
            String[] files = directory.list();
            if (files != null && files.length > 0) {
                return Arrays.stream(files)
                        .map(filename -> Paths.get(ENCRYPT_DIRECTORY, filename).toFile())
                        .collect(Collectors.toList());
            } else {
                String message = files == null ? "could not list of directory" : "directory is empty";
                throw new RuntimeException(String.format("Failed to get files content: %s.", message));
            }
        } else {
            throw new RuntimeException(
                    String.format(
                            "Failed to get files from resources directory: path='%s', exists='%b', is_directory='%b'.",
                            directory.getPath(),
                            directory.exists(),
                            directory.isDirectory()
                    )
            );
        }
    }

    private static String readFile(File file) {
        try {
            return Files.readAllLines(file.toPath(), Charset.forName("US-ASCII"))
                    .stream()
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            LOGGER.error("Failed to read file '{}': {}.", file.getPath(), e.getMessage());
            return "";
        }
    }

    private static List<String> getFilenameWithExtension(File file) {
        return Arrays.asList(file.getName().split("\\."));
    }

    private static String getFilenameWithoutExtension(File file) {
        return getFilenameWithExtension(file).get(0);
    }

    private static String getFileExtension(File file) {
        List<String> filenameWithExtension = getFilenameWithExtension(file);
        if (!filenameWithExtension.isEmpty() && filenameWithExtension.size() <= 2) {
            return filenameWithExtension.get(1).toLowerCase();
        } else {
            LOGGER.error("Failed to get file extension: '{}'.", file.getPath());
        }
        return null;
    }

    private static FileExtension getFileExtensionInstance(File file) {
        String extension = getFileExtension(file);
        if (extension != null) {
            if (AlgorithmType.value(extension) != null) {
                return FileExtension.TEXT;
            } else if (FileExtension.KEY.name().toLowerCase().equals(extension)) {
                return FileExtension.KEY;
            }
        } else {
            LOGGER.error("Failed to define file extension: '{}'.", file.getPath());
        }
        return null;
    }
}
