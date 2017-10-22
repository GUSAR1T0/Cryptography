package store.vxdesign.cryptography.algorithms.mh;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import store.vxdesign.cryptography.framework.enums.Cipher;
import store.vxdesign.cryptography.framework.utilities.ConverterUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static store.vxdesign.cryptography.algorithms.mh.MerkleHellmanResultType.*;

/**
 * @author Roman Mashenkin
 * @since 21.10.2017
 */
@RunWith(Parameterized.class)
public class MerkleHellmanTest {

    @Parameterized.Parameter
    public String input;

    @Parameterized.Parameter(1)
    public String key;

    @Parameterized.Parameter(2)
    public String[] binaryInputBlocks;

    @Parameterized.Parameter(3)
    public String[] privateKey;

    @Parameterized.Parameter(4)
    public String outputSums;

    @Parameterized.Parameter(5)
    public String inputSums;

    @Parameterized.Parameter(6)
    public String output;

    @Parameterized.Parameter(7)
    public String[] binaryOutputBlocks;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        List<String> plaintextList = Arrays.asList("abasiufds", "23456wfs", "Fib9vTbvt6");
        List<String> keyList = Arrays.asList(
                "2, 7, 11, 21, 42, 89, 180, 354; 881; 588",
                "2, 14, 22, 54, 105, 221, 485, 998; 3797; 1456",
                "1, 3, 24, 46, 114, 246, 471, 930; 3709; 1359"
        );
        List<String> ciphertextList = Arrays.asList(
                "1129, 1013, 1129, 1263, 1157, 1496, 1366, 1246, 1263",
                "8055, 10689, 7168, 9802, 10883, 14916, 9598, 12088",
                "3013, 8979, 5453, 11781, 9127, 4042, 5453, 9127, 6986, 8759"
        );

        Object[][] data = new Object[3][8];

        for (int i = 0; i < 3; i++) {
            data[i][0] = data[i][6] = plaintextList.get(i);
            data[i][1] = keyList.get(i);
            data[i][2] = data[i][7] = ConverterUtils.toBinaryStringBlocks(plaintextList.get(i), MerkleHellmanConstants.SIZE_OF_BLOCKS);
            data[i][3] = keyList.get(i).split(";\\s+");
            data[i][4] = data[i][5] = ciphertextList.get(i);
        }

        return Arrays.asList(data);
    }

    @Test
    public void encryptionTest() throws Exception {
        MerkleHellman mh = new MerkleHellman();
        String result = mh.cipher(Cipher.ENCRYPT, input, key);

        Assert.assertTrue("Another text input is expected",
                result.contains(createExpectedLine(TEXT_INPUT, input)));
        Assert.assertTrue("Another binary input is expected",
                result.contains(createExpectedLine(BINARY_INPUT, Arrays.stream(binaryInputBlocks).collect(Collectors.joining()))));
        Assert.assertTrue("Another hex input is expected",
                result.contains(createExpectedLine(HEX_INPUT, ConverterUtils.toHexString(binaryInputBlocks))));
        Assert.assertTrue("Another private key is expected",
                result.contains(createExpectedLine(PRIVATE_KEY,
                        String.format("w=[%s], modulus=%s, multiplier=%s", privateKey[0], privateKey[1], privateKey[2]))));
        Assert.assertTrue("Another output sums are expected",
                result.contains(createExpectedLine(OUTPUT_SUMS, String.format("[%s]", outputSums))));
    }

    @Test
    public void decryptionTest() throws Exception {
        MerkleHellman mh = new MerkleHellman();
        String result = mh.cipher(Cipher.DECRYPT, inputSums, key);

        Assert.assertTrue("Another input sums are expected",
                result.contains(createExpectedLine(INPUT_SUMS, String.format("[%s]", inputSums))));
        Assert.assertTrue("Another private key is expected",
                result.contains(createExpectedLine(PRIVATE_KEY,
                        String.format("w=[%s], modulus=%s, multiplier=%s", privateKey[0], privateKey[1], privateKey[2]))));
        Assert.assertTrue("Another text output is expected",
                result.contains(createExpectedLine(TEXT_OUTPUT, output)));
        Assert.assertTrue("Another binary output is expected",
                result.contains(createExpectedLine(BINARY_OUTPUT, Arrays.stream(binaryOutputBlocks).collect(Collectors.joining()))));
        Assert.assertTrue("Another hex output is expected",
                result.contains(createExpectedLine(HEX_OUTPUT, ConverterUtils.toHexString(binaryOutputBlocks))));

    }

    private String createExpectedLine(MerkleHellmanResultType type, String expected) {
        return String.format("%s: %s", type, expected);
    }
}
