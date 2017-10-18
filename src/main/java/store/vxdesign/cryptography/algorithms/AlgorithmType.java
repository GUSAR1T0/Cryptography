package store.vxdesign.cryptography.algorithms;

import store.vxdesign.cryptography.framework.utilities.EnumUtils;

/**
 * @author Roman Mashenkin
 * @since 17.10.2017
 */
public enum AlgorithmType {

    DATA_ENCRYPTION_STANDARD("des");

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
