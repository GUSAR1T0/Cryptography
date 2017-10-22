/*
 * Copyright 2017 Roman Mashenkin
 * Licensed under the Apache License, Version 2.0
 */
package store.vxdesign.cryptography.algorithms.mh;

/**
 * @author Roman Mashenkin
 * @since 19.10.2017
 */
final class MerkleHellmanConstants {

    /**
     * Hidden constructor.
     */
    private MerkleHellmanConstants() {
    }

    static final int SIZE_OF_BLOCKS = Byte.SIZE;

    static final int SEQUENCE_ELEMENTS = 8;

    static final int BEGIN_NUMBER = 1;

    static final int END_NUMBER = 1000;
}
