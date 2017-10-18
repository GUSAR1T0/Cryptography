package store.vxdesign.cryptography.framework.utilities;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * @author Roman Mashenkin
 * @since 17.10.2017
 */
public final class StringUtils {

    /**
     * Hidden constructor.
     */
    private StringUtils() {
    }

    public static String divideOnBlocksPattern(int blockSize) {
        return String.format("(?<=\\G.{%d})", blockSize);
    }

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

    public static String createLineFromEntry(Map.Entry entry) {
        return String.format("%15s: %s%n", entry.getKey(), entry.getValue());
    }
}
