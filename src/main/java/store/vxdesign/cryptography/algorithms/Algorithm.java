/*
 * Copyright 2017 Roman Mashenkin
 * Licensed under the Apache License, Version 2.0
 */
package store.vxdesign.cryptography.algorithms;

import store.vxdesign.cryptography.framework.enums.Cipher;

/**
 * @author Roman Mashenkin
 * @since 18.10.2017
 */
public interface Algorithm {

    String cipher(Cipher cipher, String input, String key);

    String cipher(Cipher cipher, String input);
}
