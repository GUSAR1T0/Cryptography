package store.vxdesign.cryptography.algorithms.des;

import store.vxdesign.cryptography.algorithms.Algorithm;
import store.vxdesign.cryptography.framework.utilities.ConverterUtils;

import java.util.Arrays;

/**
 * @author Roman Mashenkin
 * @since 17.10.2017
 */
public final class DataEncryptionStandard implements Algorithm {

    private final String input;
    private final String[] binaryInputBlocks;
    private final String key;
    private final String binaryKeyBlock;

    private String[] binaryOutputBlocks;

    public DataEncryptionStandard(String input) {
        this(input, DataEncryptionStandardUtils.generateKey());
    }

    public DataEncryptionStandard(String input, String key) {
        this.input = input;
        this.binaryInputBlocks = ConverterUtils.toBinaryStringBlocks(input, DataEncryptionStandardConstants.SIZE_OF_BLOCKS);
        this.key = key;
        this.binaryKeyBlock = ConverterUtils.toBinaryString(key);
    }

    @Override
    public String encrypt() {
        // Step 1: initial permutation
        String[] initialPermutation = DataEncryptionStandardUtils.executeInitialPermutation(binaryInputBlocks);

        // Step 2: preparing for Feitsel function
        String[][] l = FeitselFunction.prepareForFeitselFunction(
                binaryInputBlocks,
                initialPermutation,
                0,
                DataEncryptionStandardConstants.SIZE_OF_BLOCKS / 2
        );
        String[][] r = FeitselFunction.prepareForFeitselFunction(
                binaryInputBlocks,
                initialPermutation,
                DataEncryptionStandardConstants.SIZE_OF_BLOCKS / 2
        );

        // Step 3: key permutation
        String keyPermutation = DataEncryptionStandardUtils.executeKeyPermutation(binaryKeyBlock);

        // Step 4: dividing of key and creating 16 + 1 sequences
        String[] c = RoundKeyGenerator.createRoundHalfKeysWithLeftShift(
                keyPermutation,
                0,
                DataEncryptionStandardConstants.SIZE_OF_KEYS / 2
        );
        String[] d = RoundKeyGenerator.createRoundHalfKeysWithLeftShift(
                keyPermutation,
                DataEncryptionStandardConstants.SIZE_OF_KEYS / 2
        );

        // Step 5: concatenate c and d arrays and perform permutation
        String[] roundKeysPermutation = DataEncryptionStandardUtils.executeRoundKeysPermutation(
                RoundKeyGenerator.createRoundKeysSequences(c, d)
        );

        // Step 6: execute Feitsel function
        FeitselFunction.executeFeitselFunction(binaryInputBlocks, roundKeysPermutation, l, r);

        // Step 7: final permutation
        String[] preparingFinalPermutation = DataEncryptionStandardUtils.prepareFinalPermutation(binaryInputBlocks, l, r);
        binaryOutputBlocks = DataEncryptionStandardUtils.executeFinalPermutation(preparingFinalPermutation);

        return DataEncryptionStandardUtils.getResult(
                input,
                binaryInputBlocks,
                key,
                binaryKeyBlock,
                binaryOutputBlocks,
                DataEncryptionStandardConstants.SIZE_OF_BLOCKS
        );
    }

    @Override
    public String toString() {
        return "DataEncryptionStandard [" +
                "input='" + input + '\'' +
                ", key='" + key + '\'' +
                ", binaryInputBlocks=" + Arrays.toString(binaryInputBlocks) +
                ", binaryKeyBlock='" + binaryKeyBlock + '\'' +
                ", binaryOutputBlocks=" + Arrays.toString(binaryOutputBlocks) +
                ']';
    }
}
