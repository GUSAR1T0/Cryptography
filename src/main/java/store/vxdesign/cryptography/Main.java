package store.vxdesign.cryptography;

import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import store.vxdesign.cryptography.algorithms.Algorithm;
import store.vxdesign.cryptography.algorithms.AlgorithmType;
import store.vxdesign.cryptography.algorithms.des.DataEncryptionStandard;
import store.vxdesign.cryptography.algorithms.mh.MerkleHellman;
import store.vxdesign.cryptography.framework.cli.CommandLineInterface;
import store.vxdesign.cryptography.framework.files.FileProperties;
import store.vxdesign.cryptography.framework.files.FilesReader;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Roman Mashenkin
 * @since 17.10.2017
 */
public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        CommandLine cmd = new CommandLineInterface(args).getParsedArguments();

        String[] paths = cmd.getOptionValues('p');
        if (paths == null || paths.length > 0) {
            LOGGER.warn("Program will choose files from encrypt directory by default.");
            paths = new String[]{};
        }

        List<FileProperties> filePropertiesList = FilesReader.readFiles(paths);
        for (AlgorithmType type: AlgorithmType.values()) {
            filePropertiesList.stream().filter(properties -> type.equals(properties.getAlgorithmType()))
                    .forEach(properties -> {
                        try {
                            LOGGER.debug("Prepare to encrypt: '{}'.", properties);
                            Algorithm algorithm = getAlgorithmInstance(type, properties, cmd.hasOption('k'));
                            LOGGER.debug("Start to encrypt: '{}'.", properties);
                            String result = algorithm.encrypt();
                            LOGGER.debug("Encryption result of '{}' is:\n\n{}", properties, result);
                        } catch (Exception e) {
                            LOGGER.error("Encryption is failed: {}.", e.getMessage());
                        }
                    });
        }
    }

    private static Algorithm getAlgorithmInstance(AlgorithmType type, FileProperties properties, boolean generateKey) {
        switch (type) {
            case DATA_ENCRYPTION_STANDARD:
                return getDataEncryptionStandardInstance(properties, generateKey);
            case MERKLE_HELLMAN:
                return getMerkleHellmanInstance(properties, generateKey);
            default:
                throw new RuntimeException(
                        String.format(
                                "Failed to get any algorithm instance: type='%s', properties='%s', generate_key='%b'.",
                                type,
                                properties,
                                generateKey
                        )
                );
        }
    }

    private static Algorithm getAlgorithmInstance(FileProperties properties, boolean generateKey, Supplier<Algorithm> supplier1, Supplier<Algorithm> supplier2) {
        if (!generateKey) {
            if (properties.getKey() != null) {
                return supplier1.get();
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
        return supplier2.get();
    }

    private static Algorithm getDataEncryptionStandardInstance(FileProperties properties, boolean generateKey) {
        return getAlgorithmInstance(
                properties,
                generateKey,
                () -> new DataEncryptionStandard(properties.getContent(), properties.getKey()),
                () -> new DataEncryptionStandard(properties.getContent())
        );
    }

    private static Algorithm getMerkleHellmanInstance(FileProperties properties, boolean generateKey) {
        return getAlgorithmInstance(
                properties,
                generateKey,
                () -> new MerkleHellman(properties.getContent(), properties.getKey()),
                () -> new MerkleHellman(properties.getContent())
        );
    }
}
