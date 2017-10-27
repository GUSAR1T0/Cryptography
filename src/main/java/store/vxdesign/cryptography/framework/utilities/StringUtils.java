/*
 * Copyright 2017 Roman Mashenkin
 * Licensed under the Apache License, Version 2.0
 */
package store.vxdesign.cryptography.framework.utilities;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * Util class for string handling.
 *
 * @author Roman Mashenkin
 * @since 17.10.2017
 */
public final class StringUtils {

    /**
     * Hidden constructor.
     */
    private StringUtils() {
    }

    /**
     * Obtains capitalized string from any string.
     *
     * @param text any string.
     * @return capitalized string.
     */
    public static String capitalize(String text) {
        return Character.toUpperCase(text.charAt(0)) + text.substring(1).toLowerCase();
    }

    /**
     * Obtains string pattern for dividing any string on blocks.
     *
     * @param blockSize size of block.
     * @return string pattern.
     */
    public static String divideOnBlocksPattern(int blockSize) {
        return String.format("(?<=\\G.{%d})", blockSize);
    }

    /**
     * Generates some string.
     *
     * @param length count of symbols.
     * @return generated string.
     */
    public static String generateString(int length) {
        final String digits = "0123456789";
        final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String lower = upper.toLowerCase(Locale.ROOT);

        char[] buffer = new char[length];
        char[] symbols = (digits + upper + lower).toCharArray();
        Random random = new SecureRandom();

        for (int i = 0; i < buffer.length; ++i) {
            buffer[i] = symbols[random.nextInt(symbols.length)];
        }

        return new String(buffer);
    }

    /**
     * Creates line for output result.
     *
     * @param entry {@link java.util.Map.Entry} key and value which create output line.
     * @return string line.
     */
    public static String createLineFromEntry(Map.Entry entry) {
        return String.format("%15s: %s%n", entry.getKey(), entry.getValue());
    }
}
