/*
 * Copyright 2017 Roman Mashenkin
 * Licensed under the Apache License, Version 2.0
 */
package store.vxdesign.apps.cryptography;

import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import store.vxdesign.apps.cryptography.algorithms.mh.MerkleHellman;
import store.vxdesign.apps.cryptography.algorithms.Algorithm;
import store.vxdesign.apps.cryptography.algorithms.des.DataEncryptionStandard;
import store.vxdesign.apps.cryptography.framework.cli.CommandLineInterface;
import store.vxdesign.apps.cryptography.framework.enums.AlgorithmType;
import store.vxdesign.apps.cryptography.framework.enums.Cipher;
import store.vxdesign.apps.cryptography.framework.files.FileProperties;
import store.vxdesign.apps.cryptography.framework.files.FilesReader;

import java.io.IOException;
import java.util.List;

/**
 * Main class of application.
 *
 * @author Roman Mashenkin
 * @since 17.10.2017
 */
public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        CommandLine cmd = new CommandLineInterface(args).getParsedArguments();

        Cipher cipher = Cipher.value(cmd.getOptionValue('c'));
        String[] paths = cmd.getOptionValues('p');
        if (cipher == null) {
            LOGGER.warn("Program will encrypt and decrypt files by default.");
            cipher = Cipher.ALL;
            paths = new String[]{};
        } else {
            if (!cipher.equals(Cipher.ALL)) {
                if (paths == null || paths.length == 0) {
                    LOGGER.warn("Program will choose files from {} directory by default.", cipher);
                    paths = new String[]{};
                }
            } else {
                LOGGER.warn("Program will ignore entered paths because cipher was set for encryption and decryption.");
                paths = new String[]{};
            }
        }

        List<FileProperties> filePropertiesList = FilesReader.readFiles(cipher, paths);
        for (AlgorithmType type : AlgorithmType.values()) {
            filePropertiesList.stream().filter(properties ->
                    type.equals(properties.getAlgorithmType()) && ((Cipher.DECRYPT.equals(
                            properties.getCipher()) &&
                            properties.getKey() != null &&
                            !properties.getKey().isEmpty()
                    ) || Cipher.ENCRYPT.equals(properties.getCipher())))
                    .forEach(properties -> {
                        try {
                            LOGGER.debug("Prepare to {}: '{}'.", properties.getCipher().getName(false), properties);
                            Algorithm algorithm = getAlgorithmInstance(type);
                            LOGGER.debug("Start to {}: '{}'.", properties.getCipher().getName(false), properties);
                            String result = getResult(algorithm, properties, !Cipher.DECRYPT.equals(properties.getCipher()) && cmd.hasOption('k'));
                            LOGGER.debug("{} is completed, result for '{}' is:\n\n{}",
                                    properties.getCipher().getName(true), properties, result);
                        } catch (Exception e) {
                            LOGGER.error("{} is failed: {}.",
                                    properties.getCipher().getName(true), e.fillInStackTrace());
                        }
                    });
        }
    }

    private static Algorithm getAlgorithmInstance(AlgorithmType type) {
        switch (type) {
            case DATA_ENCRYPTION_STANDARD:
                return new DataEncryptionStandard();
            case MERKLE_HELLMAN:
                return new MerkleHellman();
            default:
                throw new RuntimeException(String.format("Failed to get any algorithm instance: type='%s'.", type));
        }
    }

    private static String getResult(Algorithm algorithm, FileProperties properties, boolean generateKey) {
        if (!generateKey) {
            if (properties.getKey() != null && !properties.getKey().isEmpty()) {
                return algorithm.cipher(properties.getCipher(), properties.getContent(), properties.getKey());
            } else {
                LOGGER.warn(
                        "Program could not find KEY file, key will be generated for file '{} | {}'.",
                        properties.getDirectory(),
                        properties.getFilename()
                );
            }
        } else {
            LOGGER.debug(
                    "Program will generate key for '{} | {}' forcibly.",
                    properties.getDirectory(),
                    properties.getFilename()
            );
        }
        return algorithm.cipher(properties.getCipher(), properties.getContent());
    }
}
