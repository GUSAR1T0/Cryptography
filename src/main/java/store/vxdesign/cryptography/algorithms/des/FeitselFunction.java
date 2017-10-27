/*
 * Copyright 2017 Roman Mashenkin
 * Licensed under the Apache License, Version 2.0
 */
package store.vxdesign.cryptography.algorithms.des;

import store.vxdesign.cryptography.framework.enums.Cipher;
import store.vxdesign.cryptography.framework.utilities.StringUtils;

/**
 * Class of execution Feitsel function.
 *
 * @author Roman Mashenkin
 * @since 18.10.2017
 */
final class FeitselFunction {

    static String[][] prepareForFeitselFunction(int binaryInputBlocksLength, String[] initialPermutation, int begin, int end) {
        String[][] a = new String[binaryInputBlocksLength][DataEncryptionStandardConstants.COUNT_OF_ROUNDS + 1];
        for (int i = 0; i < binaryInputBlocksLength; i++) {
            a[i][0] = end != -1 ? initialPermutation[i].substring(begin, end) : initialPermutation[i].substring(begin);
        }
        return a;
    }

    static String[][] prepareForFeitselFunction(int binaryInputBlocksLength, String[] initialPermutation, int begin) {
        return prepareForFeitselFunction(binaryInputBlocksLength, initialPermutation, begin, -1);
    }

    static void executeFeitselFunction(int binaryInputBlocksLength, String[] roundKeysPermutation,
                                       String[][] l, String[][] r, Cipher cipher) {
        for (int i = 0; i < binaryInputBlocksLength; i++) {
            for (int round = 0; round < DataEncryptionStandardConstants.COUNT_OF_ROUNDS; round++) {
                String expansionPermutation = DataEncryptionStandardUtils.executeExpansionPermutation(r[i][round]);
                String xorResult = DataEncryptionStandardUtils.executeXor
                        (
                                expansionPermutation,
                                roundKeysPermutation
                                        [
                                        cipher.equals(Cipher.ENCRYPT) ?
                                                round + 1 :
                                                DataEncryptionStandardConstants.COUNT_OF_ROUNDS - round
                                        ]
                        );
                String[] preparedForSBox = xorResult.split(
                        StringUtils.divideOnBlocksPattern(DataEncryptionStandardConstants.SIZE_OF_SBOX_BLOCKS)
                );
                String substitution = executeSBoxSubstitution(preparedForSBox);
                String p = DataEncryptionStandardUtils.executeSBoxResultPermutation(substitution.replaceAll("\\s", "0"));
                r[i][round + 1] = DataEncryptionStandardUtils.executeXor(l[i][round], p);
                l[i][round + 1] = r[i][round];
            }
        }
    }

    private static String executeSBoxSubstitution(String[] preparedForSBox) {
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

        return builder.toString();
    }
}
