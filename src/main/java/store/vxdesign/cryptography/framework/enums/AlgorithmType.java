/*
 * Copyright 2017 Roman Mashenkin
 * Licensed under the Apache License, Version 2.0
 */
package store.vxdesign.cryptography.framework.enums;

import store.vxdesign.cryptography.framework.utilities.EnumUtils;

/**
 * Class of algorithm types.
 *
 * @author Roman Mashenkin
 * @since 17.10.2017
 */
public enum AlgorithmType {

    DATA_ENCRYPTION_STANDARD("des"), MERKLE_HELLMAN("mh");

    private final String name;

    AlgorithmType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static AlgorithmType value(String name) {
        return EnumUtils.value(name, values());
    }
}
