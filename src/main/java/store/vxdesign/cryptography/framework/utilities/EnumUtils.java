/*
 * Copyright 2017 Roman Mashenkin
 * Licensed under the Apache License, Version 2.0
 */
package store.vxdesign.cryptography.framework.utilities;

/**
 * @author Roman Mashenkin
 * @since 18.10.2017
 */
public final class EnumUtils {

    /**
     * Hidden constructor.
     */
    private EnumUtils() {
    }

    public static <R> R value(String name, R[] values) {
        for (R value : values) {
            if (value.toString().equals(name)) {
                return value;
            }
        }
        return null;
    }
}
