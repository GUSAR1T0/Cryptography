/*
 * Copyright 2017 Roman Mashenkin
 * Licensed under the Apache License, Version 2.0
 */
package store.vxdesign.apps.cryptography.framework.files;

import org.junit.Assert;
import org.junit.Test;
import store.vxdesign.apps.cryptography.framework.enums.AlgorithmType;
import store.vxdesign.apps.cryptography.framework.enums.Cipher;

import java.util.List;

/**
 * Files reader unit tests class.
 *
 * @author Roman Mashenkin
 * @since 22.10.2017
 */
public class FilesReaderTest {

    @Test
    public void testReadFilesForEncryption() throws Exception {
        String[] paths = new String[]
                {
                        "src/test/resources/encrypt/file.des",
                        "src/test/resources/encrypt/file.key",
                        "src/test/resources/encrypt/file2.mh",
                        "src/test/resources/encrypt/file2.key",
                };
        List<FileProperties> properties = FilesReader.readFiles(Cipher.ENCRYPT, paths);
        Assert.assertTrue(properties.size() == 2);
        Assert.assertTrue("file".equals(properties.get(0).getFilename()));
        Assert.assertTrue(properties.get(0).getDirectory().contains("encrypt"));
        Assert.assertTrue(Cipher.ENCRYPT.equals(properties.get(0).getCipher()));
        Assert.assertTrue(AlgorithmType.DATA_ENCRYPTION_STANDARD.equals(properties.get(0).getAlgorithmType()));
        Assert.assertTrue("kalsdf23".equals(properties.get(0).getContent()));
        Assert.assertTrue("12345678".equals(properties.get(0).getKey()));
        Assert.assertTrue("file2".equals(properties.get(1).getFilename()));
        Assert.assertTrue(properties.get(1).getDirectory().contains("encrypt"));
        Assert.assertTrue(Cipher.ENCRYPT.equals(properties.get(1).getCipher()));
        Assert.assertTrue(AlgorithmType.MERKLE_HELLMAN.equals(properties.get(1).getAlgorithmType()));
        Assert.assertTrue("abasiufds".equals(properties.get(1).getContent()));
        Assert.assertTrue("2, 7, 11, 21, 42, 89, 180, 354; 881; 588".equals(properties.get(1).getKey()));
    }

    @Test
    public void testReadFilesForDecryption() throws Exception {
        String[] paths = new String[]
                {
                        "src/test/resources/decrypt/file.des",
                        "src/test/resources/decrypt/file.key",
                        "src/test/resources/decrypt/file2.mh",
                        "src/test/resources/decrypt/file2.key",
                };
        List<FileProperties> properties = FilesReader.readFiles(Cipher.DECRYPT, paths);
        Assert.assertTrue(properties.size() == 2);
        Assert.assertTrue("file".equals(properties.get(0).getFilename()));
        Assert.assertTrue(properties.get(0).getDirectory().contains("decrypt"));
        Assert.assertTrue(Cipher.DECRYPT.equals(properties.get(0).getCipher()));
        Assert.assertTrue(AlgorithmType.DATA_ENCRYPTION_STANDARD.equals(properties.get(0).getAlgorithmType()));
        Assert.assertTrue("0000010111010000101011101110110010000011001110001000101101000100".equals(properties.get(0).getContent()));
        Assert.assertTrue("12345678".equals(properties.get(0).getKey()));
        Assert.assertTrue("file2".equals(properties.get(1).getFilename()));
        Assert.assertTrue(properties.get(1).getDirectory().contains("decrypt"));
        Assert.assertTrue(Cipher.DECRYPT.equals(properties.get(1).getCipher()));
        Assert.assertTrue(AlgorithmType.MERKLE_HELLMAN.equals(properties.get(1).getAlgorithmType()));
        Assert.assertTrue("6905, 7819, 6905, 11583, 7342, 10709, 8355, 6945, 11583".equals(properties.get(1).getContent()));
        Assert.assertTrue("5, 10, 29, 56, 111, 238, 460, 951; 4451; 1648".equals(properties.get(1).getKey()));
    }

    @Test
    public void testReadFilesForAllCipher() throws Exception {
        String[] paths = new String[]
                {
                        "src/test/resources/decrypt/file.des",
                        "src/test/resources/decrypt/file.key",
                        "src/test/resources/decrypt/file2.mh",
                        "src/test/resources/decrypt/file2.key",
                        "src/test/resources/encrypt/file.des",
                        "src/test/resources/encrypt/file.key",
                        "src/test/resources/encrypt/file2.mh",
                        "src/test/resources/encrypt/file2.key",
                };
        List<FileProperties> properties = FilesReader.readFiles(Cipher.ALL, paths);
        Assert.assertTrue(properties.size() == 4);
        Assert.assertTrue("file".equals(properties.get(0).getFilename()));
        Assert.assertTrue(properties.get(0).getDirectory().contains("decrypt"));
        Assert.assertTrue(Cipher.DECRYPT.equals(properties.get(0).getCipher()));
        Assert.assertTrue(AlgorithmType.DATA_ENCRYPTION_STANDARD.equals(properties.get(0).getAlgorithmType()));
        Assert.assertTrue("0000010111010000101011101110110010000011001110001000101101000100".equals(properties.get(0).getContent()));
        Assert.assertTrue("12345678".equals(properties.get(0).getKey()));
        Assert.assertTrue("file2".equals(properties.get(1).getFilename()));
        Assert.assertTrue(properties.get(1).getDirectory().contains("decrypt"));
        Assert.assertTrue(Cipher.DECRYPT.equals(properties.get(1).getCipher()));
        Assert.assertTrue(AlgorithmType.MERKLE_HELLMAN.equals(properties.get(1).getAlgorithmType()));
        Assert.assertTrue("6905, 7819, 6905, 11583, 7342, 10709, 8355, 6945, 11583".equals(properties.get(1).getContent()));
        Assert.assertTrue("5, 10, 29, 56, 111, 238, 460, 951; 4451; 1648".equals(properties.get(1).getKey()));
        Assert.assertTrue("file".equals(properties.get(2).getFilename()));
        Assert.assertTrue(properties.get(2).getDirectory().contains("encrypt"));
        Assert.assertTrue(Cipher.ENCRYPT.equals(properties.get(2).getCipher()));
        Assert.assertTrue(AlgorithmType.DATA_ENCRYPTION_STANDARD.equals(properties.get(2).getAlgorithmType()));
        Assert.assertTrue("kalsdf23".equals(properties.get(2).getContent()));
        Assert.assertTrue("12345678".equals(properties.get(2).getKey()));
        Assert.assertTrue("file2".equals(properties.get(3).getFilename()));
        Assert.assertTrue(properties.get(3).getDirectory().contains("encrypt"));
        Assert.assertTrue(Cipher.ENCRYPT.equals(properties.get(3).getCipher()));
        Assert.assertTrue(AlgorithmType.MERKLE_HELLMAN.equals(properties.get(3).getAlgorithmType()));
        Assert.assertTrue("abasiufds".equals(properties.get(3).getContent()));
        Assert.assertTrue("2, 7, 11, 21, 42, 89, 180, 354; 881; 588".equals(properties.get(3).getKey()));
    }
}
