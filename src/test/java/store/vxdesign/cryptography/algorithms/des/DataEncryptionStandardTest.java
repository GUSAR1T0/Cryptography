/*
 * Copyright 2017 Roman Mashenkin
 * Licensed under the Apache License, Version 2.0
 */
package store.vxdesign.cryptography.algorithms.des;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import store.vxdesign.cryptography.framework.enums.Cipher;
import store.vxdesign.cryptography.framework.utilities.ConverterUtils;
import store.vxdesign.cryptography.framework.utilities.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static store.vxdesign.cryptography.algorithms.des.DataEncryptionStandardConstants.SIZE_OF_BLOCKS;
import static store.vxdesign.cryptography.algorithms.des.DataEncryptionStandardResultType.*;

/**
 * Data Encryption Standard unit tests class.
 *
 * @author Roman Mashenkin
 * @since 21.10.2017
 */
@RunWith(Parameterized.class)
public class DataEncryptionStandardTest {

    @Parameterized.Parameter
    public String[] input;

    @Parameterized.Parameter(1)
    public String[] key;

    @Parameterized.Parameter(2)
    public String[][] binaryInputBlocks;

    @Parameterized.Parameter(3)
    public String[] binaryKeyBlock;

    @Parameterized.Parameter(4)
    public String[][] binaryOutputBlocks;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        List<String> plaintextList = Arrays.asList("kalsdf23", "R76wbSj0", "183ERTEY");
        List<String> keyList = Arrays.asList("12345678", "nckuaeu3", "128hfan8");
        List<String> ciphertextList = Arrays.asList(
                "0000010111010000101011101110110010000011001110001000101101000100",
                "0101001111100111011111111011001101011001101111101010000111001010",
                "1111001101100111100110101001101010010110010000110001000100010001"
        );

        Object[][] data = new Object[3][5];

        for (int i = 0; i < 3; i++) {
            data[i][0] = new String[]{plaintextList.get(i), ciphertextList.get(i)};
            data[i][1] = new String[]{keyList.get(i), keyList.get(i)};
            data[i][2] = new String[][]{ConverterUtils.toBinaryStringBlocks(plaintextList.get(i), SIZE_OF_BLOCKS),
                    ciphertextList.get(i).split(StringUtils.divideOnBlocksPattern(SIZE_OF_BLOCKS))};
            data[i][3] = new String[]{ConverterUtils.toBinaryString(keyList.get(i)),
                    ConverterUtils.toBinaryString(keyList.get(i))};
            data[i][4] = new String[][]{ciphertextList.get(i).split(StringUtils.divideOnBlocksPattern(SIZE_OF_BLOCKS)),
                    ConverterUtils.toBinaryStringBlocks(plaintextList.get(i), SIZE_OF_BLOCKS)};
        }

        return Arrays.asList(data);
    }

    @Test
    public void encryptionTest() throws Exception {
        cipherTest(Cipher.ENCRYPT, input[0], key[0], binaryInputBlocks[0], binaryKeyBlock[0], binaryOutputBlocks[0]);
    }

    @Test
    public void decryptionTest() throws Exception {
        cipherTest(Cipher.DECRYPT, input[1], key[1], binaryInputBlocks[1], binaryKeyBlock[1], binaryOutputBlocks[1]);
    }

    private void cipherTest(Cipher cipher, String input, String key, String[] binaryInputBlocks, String binaryKeyBlock,
                            String[] binaryOutputBlocks) throws Exception {
        DataEncryptionStandard des = new DataEncryptionStandard();
        String result = des.cipher(cipher, input, key);

        Assert.assertTrue("Another text input is expected",
                result.contains(createExpectedLine(TEXT_INPUT, ConverterUtils.toTextString(binaryInputBlocks))));
        Assert.assertTrue("Another binary input is expected",
                result.contains(createExpectedLine(BINARY_INPUT, Arrays.stream(binaryInputBlocks).collect(Collectors.joining()))));
        Assert.assertTrue("Another hex input is expected",
                result.contains(createExpectedLine(HEX_INPUT, ConverterUtils.toHexString(binaryInputBlocks))));
        Assert.assertTrue("Another text key is expected",
                result.contains(createExpectedLine(TEXT_KEY, ConverterUtils.toTextString(binaryKeyBlock))));
        Assert.assertTrue("Another binary key is expected",
                result.contains(createExpectedLine(BINARY_KEY, binaryKeyBlock)));
        Assert.assertTrue("Another hex key is expected",
                result.contains(createExpectedLine(HEX_KEY, ConverterUtils.toHexString(binaryKeyBlock))));
        Assert.assertTrue("Another text output is expected",
                result.contains(createExpectedLine(TEXT_OUTPUT, ConverterUtils.toTextString(binaryOutputBlocks))));
        Assert.assertTrue("Another binary output is expected",
                result.contains(createExpectedLine(BINARY_OUTPUT, Arrays.stream(binaryOutputBlocks).collect(Collectors.joining()))));
        Assert.assertTrue("Another hex output is expected",
                result.contains(createExpectedLine(HEX_OUTPUT, ConverterUtils.toHexString(binaryOutputBlocks))));
    }

    private String createExpectedLine(DataEncryptionStandardResultType type, String expected) {
        return String.format("%s: %s", type, expected);
    }
}
