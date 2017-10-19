package store.vxdesign.cryptography.algorithms.mh;

import store.vxdesign.cryptography.algorithms.Algorithm;
import store.vxdesign.cryptography.framework.utilities.ConverterUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Roman Mashenkin
 * @since 19.10.2017
 */
public final class MerkleHellman implements Algorithm {

    private final String input;
    private final String[] binaryInputBlocks;
    private final List<Integer> superincreasingSequence;

    private List<Integer> outputSums;

    public MerkleHellman(String input) {
        this(input, MerkleHellmanUtils.generateSuperincreasingSequence());
    }

    public MerkleHellman(String input, String superincreasingSequence) {
        this(input,MerkleHellmanUtils.parseStringSuperincreasingSequence(superincreasingSequence));
    }

    private MerkleHellman(String input, List<Integer> superincreasingSequence) {
        this.input = input;
        this.binaryInputBlocks = ConverterUtils.toBinaryStringBlocks(input, MerkleHellmanConstants.SIZE_OF_BLOCKS);
                this.superincreasingSequence = superincreasingSequence;

        if (this.superincreasingSequence.size() != MerkleHellmanConstants.SEQUENCE_ELEMENTS) {
            throw new RuntimeException(
                    String.format(
                            "Integer sequence length should be equal '%s'",
                            MerkleHellmanConstants.SEQUENCE_ELEMENTS
                    )
            );
        }
    }

    @Override
    public String encrypt() {
        // Step 1: get sum of sequence
        int sequenceSum = superincreasingSequence.stream().mapToInt(Integer::intValue).sum();

        // Step 2: generate prime number more than sum of sequence
        int q = MerkleHellmanUtils.generatePrimeNumber(sequenceSum);

        // Step 3: generate number which placed into range [1, q)
        int r = MerkleHellmanUtils.generateNumber(MerkleHellmanConstants.BEGIN_NUMBER, q);

        // Step 4: create public key
        List<Integer> publicKey = superincreasingSequence.stream()
                .map(integer -> integer * r % q)
                .collect(Collectors.toList());

        // Step 5: get output sums
        outputSums = new ArrayList<>();
        for (int i = 0; i < binaryInputBlocks.length; i++) {
            int count = 0;
            for (int j = 0; j < binaryInputBlocks[i].length(); j++) {
                count += Integer.parseInt(binaryInputBlocks[i].charAt(j) + "") * publicKey.get(i);
            }
            outputSums.add(count);
        }

        return MerkleHellmanUtils.getResult(
                input,
                binaryInputBlocks,
                superincreasingSequence,
                q,
                r,
                publicKey,
                outputSums,
                MerkleHellmanConstants.SIZE_OF_BLOCKS
        );
    }

    @Override
    public String toString() {
        return "MerkleHellman{" +
                "input='" + input + '\'' +
                ", binaryInputBlocks=" + Arrays.toString(binaryInputBlocks) +
                ", superincreasingSequence=" + superincreasingSequence +
                ", outputSums=" + outputSums +
                '}';
    }
}
