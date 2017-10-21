package store.vxdesign.cryptography.algorithms.des;

import store.vxdesign.cryptography.algorithms.AbstractAlgorithm;
import store.vxdesign.cryptography.framework.enums.Cipher;
import store.vxdesign.cryptography.framework.utilities.ConverterUtils;

/**
 * @author Roman Mashenkin
 * @since 17.10.2017
 */
public final class DataEncryptionStandard extends AbstractAlgorithm {

    @Override
    public String cipher(Cipher cipher, String input) {
        return cipher(cipher, input, DataEncryptionStandardUtils::generateKey);
    }

    @Override
    protected String encrypt(String input, String key) {
        return cipherData(Cipher.ENCRYPT, input, key);
    }

    @Override
    protected String decrypt(String input, String key) {
        return cipherData(Cipher.DECRYPT, input, key);
    }

    private String cipherData(Cipher cipher, String input, String key) {
        // Step 0: prepare for encryption/decryption
        String[] binaryInputBlocks = ConverterUtils.toBinaryStringBlocks(input,
                DataEncryptionStandardConstants.SIZE_OF_BLOCKS);
        String binaryKeyBlock = ConverterUtils.toBinaryString(key);

        // Step 1: initial permutation
        String[] initialPermutation = DataEncryptionStandardUtils.executeInitialPermutation(binaryInputBlocks);

        // Step 2: preparing for Feitsel function
        String[][] l = FeitselFunction.prepareForFeitselFunction(
                binaryInputBlocks.length,
                initialPermutation,
                0,
                DataEncryptionStandardConstants.SIZE_OF_BLOCKS / 2
        );
        String[][] r = FeitselFunction.prepareForFeitselFunction(
                binaryInputBlocks.length,
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
        FeitselFunction.executeFeitselFunction(binaryInputBlocks.length, roundKeysPermutation, l, r, cipher);

        // Step 7: final permutation
        String[] preparingFinalPermutation = DataEncryptionStandardUtils.prepareFinalPermutation(
                binaryInputBlocks.length,
                l,
                r
        );
        String[] binaryOutputBlocks = DataEncryptionStandardUtils.executeFinalPermutation(preparingFinalPermutation);

        return DataEncryptionStandardUtils.getStringResult(
                input,
                binaryInputBlocks,
                key,
                binaryKeyBlock,
                binaryOutputBlocks
        );
    }
}
