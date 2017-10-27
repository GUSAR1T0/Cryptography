/*
 * Copyright 2017 Roman Mashenkin
 * Licensed under the Apache License, Version 2.0
 */
package store.vxdesign.cryptography.framework.enums;

import store.vxdesign.cryptography.framework.utilities.EnumUtils;
import store.vxdesign.cryptography.framework.utilities.StringUtils;

/**
 * Class of cipher modes.
 *
 * @author Roman Mashenkin
 * @since 20.10.2017
 */
public enum Cipher {

    ENCRYPT("encryption"), DECRYPT("decryption"), ALL;

    private String name = "";

    Cipher() {
    }

    Cipher(String name) {
        this.name = name;
    }

    public static Cipher value(String name) {
        return EnumUtils.value(name, values());
    }

    public String getName(boolean isCapitalize) {
        return isCapitalize ? StringUtils.capitalize(name) : name.toLowerCase();
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
