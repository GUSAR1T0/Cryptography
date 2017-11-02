/*
 * Copyright 2017 Roman Mashenkin
 * Licensed under the Apache License, Version 2.0
 */
package store.vxdesign.cryptography.algorithms.mh;

import store.vxdesign.cryptography.algorithms.AbstractAlgorithm;
import store.vxdesign.cryptography.framework.enums.Cipher;
import store.vxdesign.cryptography.framework.utilities.ConverterUtils;

import java.util.List;

/**
 * Merkle-Hellman knapsack cryptosystem class for encryption and decryption some plaintext.
 *
 * @author Roman Mashenkin
 * @since 19.10.2017
 */
public final class MerkleHellman extends AbstractAlgorithm {

    @Override
    public String cipher(Cipher cipher, String input) {
        return cipher(cipher, input, MerkleHellmanUtils.generateKey(input.length()));
    }

    @Override
    protected String encrypt(String input, String keys) {
        // Step 0: prepare for encryption
        String[] binaryInputBlocks = ConverterUtils.toBinaryStringBlocks(input, MerkleHellmanConstants.SIZE_OF_BLOCKS);
        String[] keysBlocks = keys.split(";\\s+");
        List<Integer> superincreasingSequence = MerkleHellmanUtils.parseStringSequence(keysBlocks[0]);
        int modulus = Integer.parseInt(keysBlocks[1]);
        int multiplier = Integer.parseInt(keysBlocks[2]);

        // Step 1: create public key
        List<Integer> publicKey = MerkleHellmanUtils.createPublicKey(superincreasingSequence, modulus, multiplier);

        // Step 2: get output sums
        List<Integer> outputSums = MerkleHellmanUtils.getOutputSums(binaryInputBlocks, publicKey);

        return MerkleHellmanUtils.getStringResult(
                binaryInputBlocks,
                superincreasingSequence,
                modulus,
                multiplier,
                publicKey,
                outputSums
        );
    }

    @Override
    protected String decrypt(String input, String keys) {
        // Step 0: prepare for decryption
        List<Integer> inputSums = MerkleHellmanUtils.parseStringSequence(input);
        String[] keysBlocks = keys.split(";\\s+");
        List<Integer> superincreasingSequence = MerkleHellmanUtils.parseStringSequence(keysBlocks[0]);
        int modulus = Integer.parseInt(keysBlocks[1]);
        int multiplier = Integer.parseInt(keysBlocks[2]);

        // Step 1: calculate multiplicative inverse number
        int multiplicativeInverseNumber = MerkleHellmanUtils.getMultiplicativeInverseNumber(multiplier, modulus);

        // Step 2: prepare for decomposing
        List<Integer> preparedForDecomposing = MerkleHellmanUtils.prepareForDecomposing(
                inputSums,
                multiplicativeInverseNumber,
                modulus
        );

        // Step 3: decompose
        String[] binaryOutputBlocks = MerkleHellmanUtils.decompose(
                inputSums,
                superincreasingSequence,
                preparedForDecomposing
        );

        return MerkleHellmanUtils.getStringResult(
                inputSums,
                superincreasingSequence,
                modulus,
                multiplier,
                binaryOutputBlocks
        );
    }
}
