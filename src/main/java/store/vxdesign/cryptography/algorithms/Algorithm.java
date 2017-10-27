/*
 * Copyright 2017 Roman Mashenkin
 * Licensed under the Apache License, Version 2.0
 */
package store.vxdesign.cryptography.algorithms;

import store.vxdesign.cryptography.framework.enums.Cipher;

/**
 * General interface of algorithms.
 *
 * @author Roman Mashenkin
 * @since 18.10.2017
 */
public interface Algorithm {

    /**
     * Ciphers some input data through prepared key.
     *
     * @param cipher {@link Cipher} mode.
     * @param input plaintext data.
     * @param key prepared key data.
     * @return ciphertext string.
     */
    String cipher(Cipher cipher, String input, String key);

    /**
     * Ciphers some input data through generated key.
     *
     * @param cipher {@link Cipher} mode.
     * @param input plaintext data.
     * @return ciphertext string.
     */
    String cipher(Cipher cipher, String input);
}
