/*
 * Copyright 2017 Roman Mashenkin
 * Licensed under the Apache License, Version 2.0
 */
package store.vxdesign.apps.cryptography.framework.files;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import store.vxdesign.apps.cryptography.framework.enums.AlgorithmType;
import store.vxdesign.apps.cryptography.framework.enums.Cipher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Static class for reading files.
 *
 * @author Roman Mashenkin
 * @since 17.10.2017
 */
public final class FilesReader {

    private static final Logger LOGGER = LogManager.getLogger(FilesReader.class);

    private static String encryptDirectory = "";
    private static String decryptDirectory = "";

    static {
        try {
            encryptDirectory = new File(FilesReader.class.getResource("/" + Cipher.ENCRYPT.name().toLowerCase())
                    .toURI()).getPath();
        } catch (URISyntaxException e) {
            LOGGER.error("Failed to get files from default \"encrypt\" resources directory: {}", e.fillInStackTrace());
        }

        try {
            decryptDirectory = new File(FilesReader.class.getResource("/" + Cipher.DECRYPT.name().toLowerCase())
                    .toURI()).getPath();
        } catch (URISyntaxException e) {
            LOGGER.error("Failed to get files from default \"decrypt\" resources directory: {}", e.fillInStackTrace());
        }
    }

    /**
     * Hidden constructor.
     */
    private FilesReader() {
    }

    /**
     * Reads files.
     *
     * @param cipher type of {@link Cipher}.
     * @param paths possible string paths to file.
     * @return list of {@link FileProperties}.
     * @throws FileNotFoundException exception will be thrown if path is not correct.
     */
    public static List<FileProperties> readFiles(Cipher cipher, String[] paths) throws FileNotFoundException {
        List<File> files = Arrays.stream(paths).map(File::new).collect(Collectors.toList());
        List<FileProperties> filePropertiesList = new ArrayList<>();

        if (files.isEmpty()) {
            switch (cipher) {
                case ENCRYPT:
                    files.addAll(getAllFilesFromEncryptDirectory());
                    break;
                case DECRYPT:
                    files.addAll(getAllFilesFromDecryptDirectory());
                    break;
                case ALL:
                    files.addAll(getAllFilesFromEncryptDirectory());
                    files.addAll(getAllFilesFromDecryptDirectory());
                    break;
            }
        }

        for (File file : files) {
            if (file.exists() && file.isFile()) {
                String filenameWithoutExtension = getFilenameWithoutExtension(file);
                Cipher cipherForFile = !cipher.equals(Cipher.ALL) ? cipher : getCipher(file);
                FileProperties fileProperties = filePropertiesList.stream()
                        .filter(properties ->
                                properties.getFilename().equals(filenameWithoutExtension) &&
                                        properties.getCipher().equals(cipherForFile)
                        )
                        .findFirst()
                        .orElse(new FileProperties(filenameWithoutExtension, file.getParent(), cipherForFile));

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
        return getAllFilesFromDirectory(encryptDirectory);
    }

    private static List<File> getAllFilesFromDecryptDirectory() {
        return getAllFilesFromDirectory(decryptDirectory);
    }

    private static List<File> getAllFilesFromDirectory(String directory) {
        File dir = new File(directory);
        if (dir.exists() && dir.isDirectory()) {
            String[] files = dir.list();
            if (files != null && files.length > 0) {
                return Arrays.stream(files)
                        .map(filename -> Paths.get(directory, filename).toFile())
                        .collect(Collectors.toList());
            } else {
                String message = files == null ? "could not list of directory" : "directory is empty";
                throw new RuntimeException(String.format("Failed to get files content: %s.", message));
            }
        } else {
            throw new RuntimeException(
                    String.format(
                            "Failed to get files from resources directory: path='%s', exists='%b', is_directory='%b'.",
                            dir.getPath(),
                            dir.exists(),
                            dir.isDirectory()
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

    private static Cipher getCipher(File file) {
        String[] parent = file.getParent().split("[\\\\/]");
        Cipher cipher = Cipher.value(parent[parent.length - 1]);
        return cipher != null && !cipher.equals(Cipher.ALL) ? cipher : null;
    }
}
