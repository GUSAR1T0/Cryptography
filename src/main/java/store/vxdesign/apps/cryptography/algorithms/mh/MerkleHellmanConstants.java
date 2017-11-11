/*
 * Copyright 2017 Roman Mashenkin
 * Licensed under the Apache License, Version 2.0
 */
package store.vxdesign.apps.cryptography.algorithms.mh;

/**
 * Merkle-Hellman knapsack cryptosystem class of constants.
 *
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

    static final int BEGIN_NUMBER = 1;

    static final int STEP_NUMBER = 125;
}
