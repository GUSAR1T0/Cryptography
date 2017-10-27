/*
 * Copyright 2017 Roman Mashenkin
 * Licensed under the Apache License, Version 2.0
 */
package store.vxdesign.cryptography.framework.utilities;

/**
 * Util class for enumeration handling.
 *
 * @author Roman Mashenkin
 * @since 18.10.2017
 */
public final class EnumUtils {

    /**
     * Hidden constructor.
     */
    private EnumUtils() {
    }

    /**
     * Get instance from enums.
     *
     * @param name name of enum value.
     * @param values all values from enum.
     * @param <R> eny enum type.
     * @return null or found instance from enum.
     */
    public static <R extends Enum> R value(String name, R[] values) {
        for (R value : values) {
            if (value.toString().equals(name)) {
                return value;
            }
        }
        return null;
    }
}
