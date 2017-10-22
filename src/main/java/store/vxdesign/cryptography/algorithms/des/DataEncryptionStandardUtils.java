/*
 * Copyright 2017 Roman Mashenkin
 * Licensed under the Apache License, Version 2.0
 */
package store.vxdesign.cryptography.algorithms.des;

import store.vxdesign.cryptography.framework.utilities.ConverterUtils;
import store.vxdesign.cryptography.framework.utilities.StringUtils;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Roman Mashenkin
 * @since 17.10.2017
 */
final class DataEncryptionStandardUtils {

    /**
     * Hidden constructor.
     */
    private DataEncryptionStandardUtils() {
    }

    static String generateKey() {
        return StringUtils.generateString(DataEncryptionStandardConstants.SIZE_OF_BLOCKS / Byte.SIZE);
    }

    static String[] executeInitialPermutation(String[] blocks) {
        return executePermutation(DataEncryptionStandardConstants.IP, blocks);
    }

    static String executeKeyPermutation(String block) {
        return executePermutation(DataEncryptionStandardConstants.PC1, new String[]{block})[0];
    }

    static String[] executeRoundKeysPermutation(String[] blocks) {
        return executePermutation(DataEncryptionStandardConstants.PC2, blocks);
    }

    static String executeExpansionPermutation(String block) {
        return executePermutation(DataEncryptionStandardConstants.E, new String[]{block})[0];
    }

    static String executeSBoxResultPermutation(String block) {
        return executePermutation(DataEncryptionStandardConstants.P, new String[]{block})[0];
    }

    static String[] executeFinalPermutation(String[] blocks) {
        return executePermutation(DataEncryptionStandardConstants.FP, blocks);
    }

    private static String[] executePermutation(byte[] permutationTable, String[] binaryStringBlocks) {
        String[] newBinaryStringBlocks = new String[binaryStringBlocks.length];

        for (int i = 0; i < binaryStringBlocks.length; i++) {
            char[] binaryCharArrayBlock = new char[permutationTable.length];

            for (int j = 0; j < permutationTable.length; j++) {
                binaryCharArrayBlock[j] = binaryStringBlocks[i].charAt(permutationTable[j] - 1);
            }

            newBinaryStringBlocks[i] = new String(binaryCharArrayBlock);
        }

        return newBinaryStringBlocks;
    }

    static String leftShiftBits(String binaryStringBlock, int round) {
        return leftShiftBits(DataEncryptionStandardConstants.SR, binaryStringBlock, round);
    }

    private static String leftShiftBits(byte[] shiftTable, String binaryStringBlock, int round) {
        return String.format(
                "%s%s",
                binaryStringBlock.substring(shiftTable[round - 1]),
                binaryStringBlock.substring(0, shiftTable[round - 1])
        );
    }

    static String[] prepareFinalPermutation(int binaryInputBlocksLength, String[][] l, String[][] r) {
        String[] preparingFinalPermutation = new String[binaryInputBlocksLength];
        for (int i = 0; i < binaryInputBlocksLength; i++) {
            preparingFinalPermutation[i] = String.format(
                    "%s%s",
                    r[i][DataEncryptionStandardConstants.COUNT_OF_ROUNDS],
                    l[i][DataEncryptionStandardConstants.COUNT_OF_ROUNDS]
            );
        }
        return preparingFinalPermutation;
    }

    static String executeXor(String binaryStringBlock1, String binaryStringBlock2) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < binaryStringBlock1.length(); i++) {
            builder.append(
                    Integer.parseInt(binaryStringBlock1.charAt(i) + "") ^ Integer.parseInt(binaryStringBlock2.charAt(i) + "")
            );
        }

        return builder.toString();
    }

    static String getStringResult(String[] binaryInputBlocks, String binaryKeyBlock, String[] binaryOutputBlocks) {
        Map<DataEncryptionStandardResultType, String> resultMap = new EnumMap<>(DataEncryptionStandardResultType.class);

        resultMap.put(DataEncryptionStandardResultType.TEXT_INPUT, ConverterUtils.toTextString(binaryInputBlocks));
        resultMap.put(DataEncryptionStandardResultType.BINARY_INPUT, Arrays.stream(binaryInputBlocks).collect(Collectors.joining()));
        resultMap.put(DataEncryptionStandardResultType.HEX_INPUT, ConverterUtils.toHexString(binaryInputBlocks));
        resultMap.put(DataEncryptionStandardResultType.TEXT_KEY, ConverterUtils.toTextString(binaryKeyBlock));
        resultMap.put(DataEncryptionStandardResultType.BINARY_KEY, binaryKeyBlock);
        resultMap.put(DataEncryptionStandardResultType.HEX_KEY, ConverterUtils.toHexString(binaryKeyBlock));
        resultMap.put(DataEncryptionStandardResultType.TEXT_OUTPUT, ConverterUtils.toTextString(binaryOutputBlocks));
        resultMap.put(DataEncryptionStandardResultType.BINARY_OUTPUT, Arrays.stream(binaryOutputBlocks).collect(Collectors.joining()));
        resultMap.put(DataEncryptionStandardResultType.HEX_OUTPUT, ConverterUtils.toHexString(binaryOutputBlocks));

        return resultMap.entrySet().stream().map(StringUtils::createLineFromEntry).collect(Collectors.joining());
    }
}
