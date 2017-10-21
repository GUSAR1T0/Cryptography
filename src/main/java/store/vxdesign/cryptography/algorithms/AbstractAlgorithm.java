package store.vxdesign.cryptography.algorithms;

import store.vxdesign.cryptography.framework.enums.Cipher;

import java.util.function.Supplier;

/**
 * @author Roman Mashenkin
 * @since 21.10.2017
 */
public abstract class AbstractAlgorithm implements Algorithm {

    @Override
    public String cipher(Cipher cipher, String input, String keys) {
        switch (cipher) {
            case ENCRYPT:
                return encrypt(input, keys);
            case DECRYPT:
                return decrypt(input, keys);
            default:
                throw new RuntimeException(String.format("Unknown cipher: cipher='%s'.", cipher));
        }
    }

    protected String cipher(Cipher cipher, String input, Supplier<String> supplier) {
        switch (cipher) {
            case ENCRYPT:
                return encrypt(input, supplier.get());
            case DECRYPT:
                throw new RuntimeException("Failed to decrypt because file with key extension didn't find.");
            default:
                throw new RuntimeException(String.format("Unknown cipher: cipher='%s'.", cipher));
        }
    }

    protected abstract String encrypt(String input, String key);

    protected abstract String decrypt(String input, String key);
}
