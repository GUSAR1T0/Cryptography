/*
 * Copyright 2017 Roman Mashenkin
 * Licensed under the Apache License, Version 2.0
 */
package store.vxdesign.apps.cryptography.framework.utilities;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Util class for converting.
 *
 * @author Roman Mashenkin
 * @since 17.10.2017
 */
public final class ConverterUtils {

    /**
     * Hidden constructor.
     */
    private ConverterUtils() {
    }

    /**
     * Obtains binary blocks from string.
     *
     * @param textString simple text.
     * @param blockSize size of blocks.
     * @return binary blocks.
     */
    public static String[] toBinaryStringBlocks(String textString, int blockSize) {
        String binary = toBinaryString(textString);

        if (binary.length() % blockSize != 0) {
            StringBuilder builder = new StringBuilder(binary);

            for (int i = 0; i < blockSize - (binary.length() % blockSize); i++) {
                builder.append(0);
            }

            binary = builder.toString();
        }

        return binary.split(StringUtils.divideOnBlocksPattern(blockSize));
    }

    /**
     * Obtains binary block from string.
     *
     * @param textString simple text.
     * @return binary block.
     */
    public static String toBinaryString(String textString) {
        byte[] bytes = textString.getBytes();
        StringBuilder builder = new StringBuilder();

        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                builder.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }

        return builder.toString();
    }

    /**
     * Obtains string from binary blocks.
     *
     * @param binaryStringBlocks binary blocks.
     * @return string text.
     */
    public static String toTextString(String... binaryStringBlocks) {
        StringBuilder builder = new StringBuilder();

        for (String block : binaryStringBlocks) {
            builder.append(
                    Arrays.stream(block.split(StringUtils.divideOnBlocksPattern(Byte.SIZE)))
                            .map(s -> ((char) Integer.parseInt(s, 2)) + "")
                            .collect(Collectors.joining())
            );
        }

        return builder.toString();
    }

    /**
     * Obtains HEX string from binary blocks.
     *
     * @param binaryStringBlocks binary blocks.
     * @return HEX string text.
     */
    public static String toHexString(String... binaryStringBlocks) {
        StringBuilder builder = new StringBuilder();

        for (String block : binaryStringBlocks) {
            builder.append(
                    Arrays.stream(block.split(StringUtils.divideOnBlocksPattern(Byte.SIZE)))
                            .map(s ->
                                    String.format("%2s", Integer.toHexString(Integer.parseInt(s, 2)))
                                            .replaceAll("\\s", "0")
                                            .toUpperCase()
                            )
                            .collect(Collectors.joining())
            );
        }

        return builder.toString();
    }
}
