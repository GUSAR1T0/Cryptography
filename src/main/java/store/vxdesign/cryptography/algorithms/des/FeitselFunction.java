package store.vxdesign.cryptography.algorithms.des;

import store.vxdesign.cryptography.framework.utilities.StringUtils;

/**
 * @author Roman Mashenkin
 * @since 18.10.2017
 */
final class FeitselFunction {

    static String[][] prepareForFeitselFunction(String[] binaryInputBlocks, String[] initialPermutation, int begin, int end) {
        String[][] a = new String[binaryInputBlocks.length][DataEncryptionStandardConstants.COUNT_OF_ROUNDS + 1];
        for (int i = 0; i < binaryInputBlocks.length; i++) {
            a[i][0] = end != -1 ? initialPermutation[i].substring(begin, end) : initialPermutation[i].substring(begin);
        }
        return a;
    }

    static String[][] prepareForFeitselFunction(String[] binaryInputBlocks, String[] initialPermutation, int begin) {
        return prepareForFeitselFunction(binaryInputBlocks, initialPermutation, begin, -1);
    }

    static void executeFeitselFunction(String[] binaryInputBlocks, String[] roundKeysPermutation, String[][] l, String[][] r) {
        for (int i = 0; i < binaryInputBlocks.length; i++) {
            for (int round = 0; round < DataEncryptionStandardConstants.COUNT_OF_ROUNDS; round++) {
                String expansionPermutation = DataEncryptionStandardUtils.executeExpansionPermutation(r[i][round]);
                String xorResult = DataEncryptionStandardUtils.executeXor(expansionPermutation, roundKeysPermutation[round + 1]);
                String[] preparedForSBox = xorResult.split(
                        StringUtils.divideOnBlocksPattern(DataEncryptionStandardConstants.SIZE_OF_SBOX_BLOCKS)
                );
                StringBuilder builder = new StringBuilder();
                for (int j = 0; j < 8; j++) {
                    int row = Integer.parseInt(
                            String.format(
                                    "%c%c",
                                    preparedForSBox[j].charAt(0),
                                    preparedForSBox[j].charAt(DataEncryptionStandardConstants.SIZE_OF_SBOX_BLOCKS - 1)
                            ),
                            2
                    );
                    int column = Integer.parseInt(
                            preparedForSBox[j].substring(1, DataEncryptionStandardConstants.SIZE_OF_SBOX_BLOCKS - 1),
                            2
                    );
                    String s = Integer.toBinaryString(DataEncryptionStandardConstants.SBOX[j][row][column]);
                    builder.append(String.format("%4s", s));
                }
                String p = DataEncryptionStandardUtils.executeSBoxResultPermutation(builder.toString().replaceAll("\\s", "0"));
                r[i][round + 1] = DataEncryptionStandardUtils.executeXor(l[i][round], p);
                l[i][round + 1] = r[i][round];
            }
        }
    }
}
