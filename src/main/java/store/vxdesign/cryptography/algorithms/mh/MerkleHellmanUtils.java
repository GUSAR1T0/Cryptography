/*
 * Copyright 2017 Roman Mashenkin
 * Licensed under the Apache License, Version 2.0
 */
package store.vxdesign.cryptography.algorithms.mh;

import store.vxdesign.cryptography.framework.utilities.ConverterUtils;
import store.vxdesign.cryptography.framework.utilities.StringUtils;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Merkle-Hellman knapsack cryptosystem util class.
 *
 * @author Roman Mashenkin
 * @since 19.10.2017
 */
final class MerkleHellmanUtils {

    static List<Integer> parseStringSequence(String sequence) {
        return Arrays.stream(sequence.split(",\\s+|\\s+|,"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    static String generateKey(int length) {
        List<Integer> superincreasingSequence = generateSuperincreasingSequence(length);
        String superincreasingSequenceString = superincreasingSequence.stream()
                .map(integer -> Integer.toString(integer))
                .collect(Collectors.joining(", "));
        int modulus = generateNumber(superincreasingSequence.stream().mapToInt(Integer::intValue).sum());
        int multiplier = generateCoprimeNumber(MerkleHellmanConstants.BEGIN_NUMBER, modulus);
        return String.format("%s; %s; %s", superincreasingSequenceString, modulus, multiplier);
    }

    static List<Integer> createPublicKey(List<Integer> superincreasingSequence, int modulus, int multiplier) {
        return superincreasingSequence.stream()
                .map(integer -> integer * multiplier % modulus)
                .collect(Collectors.toList());
    }

    static List<Integer> getOutputSums(String[] binaryInputBlocks, List<Integer> publicKey) {
        List<Integer> outputSums = new ArrayList<>();

        for (String binaryInputBlock : binaryInputBlocks) {
            int count = 0;
            for (int j = 0; j < binaryInputBlock.length(); j++) {
                count += Integer.parseInt(binaryInputBlock.charAt(j) + "") * publicKey.get(j);
            }
            outputSums.add(count);
        }

        return outputSums;
    }

    private static List<Integer> generateSuperincreasingSequence(int length) {
        List<Integer> lowerBound = getLowerBound(MerkleHellmanConstants.BEGIN_NUMBER, length);
        List<Integer> upperBound = getUpperBound(MerkleHellmanConstants.END_NUMBER, length);
        List<Integer> list = new ArrayList<>();
        Random random = new SecureRandom();
        while (list.size() < 8) {
            int integer = random.ints(1, lowerBound.get(list.size()), upperBound.get(list.size())).sum();
            if (integer > list.stream().mapToInt(Integer::intValue).sum()) {
                list.add(integer);
            }
        }
        return list;
    }

    private static List<Integer> getLowerBound(int begin, int sequenceSize) {
        List<Integer> lowerBound = new ArrayList<>();
        lowerBound.add(begin);
        for (int i = 1; i < sequenceSize; i++) {
            lowerBound.add(lowerBound.get(i - 1) * 2);
        }
        return lowerBound;
    }

    private static List<Integer> getUpperBound(int end, int sequenceSize) {
        List<Integer> upperBound = new ArrayList<>();
        upperBound.add(end);
        for (int i = sequenceSize; i > 1; i--) {
            upperBound.add(0, upperBound.get(0) / 2);
        }
        return upperBound;
    }

    private static int generateCoprimeNumber(int begin, int modulus) {
        return getRandomNumber(() -> IntStream.range(begin, modulus)
                .filter(multiplier -> getGreatestCommonDivisor(multiplier, modulus) == 1));
    }

    private static int generateNumber(int begin) {
        return getRandomNumber(() -> IntStream.rangeClosed(begin + 1,
                MerkleHellmanConstants.END_NUMBER * MerkleHellmanConstants.SEQUENCE_ELEMENTS)
                .filter(i -> IntStream.rangeClosed(2, (int) Math.sqrt(i)).allMatch(j -> i % j != 0)));
    }

    private static int getRandomNumber(Supplier<IntStream> supplier) {
        Random r = new SecureRandom();
        List<Integer> list = supplier.get().boxed().collect(Collectors.toList());
        return list.stream().skip(r.nextInt(list.size() - 1)).findFirst().orElse(-1);
    }

    private static int getGreatestCommonDivisor(int a, int b) {
        return b != 0 ? getGreatestCommonDivisor(b, a % b) : a;
    }

    static int getMultiplicativeInverseNumber(int n, int m) {
        int val = 0;
        while (true) {
            val = val + 1;
            int result = (val * n) % m;
            if (result == 1) {
                break;
            }
        }
        return val;
    }

    static List<Integer> prepareForDecomposing(List<Integer> inputSums, int multiplicativeInverseNumber, int modulus) {
        return inputSums.stream()
                .map(integer -> integer * multiplicativeInverseNumber % modulus)
                .collect(Collectors.toList());
    }

    static String[] decompose(List<Integer> inputSums, List<Integer> superincreasingSequence, List<Integer> result) {
        String[] binaryOutputBlocks = new String[inputSums.size()];

        for (int i = 0; i < result.size(); i++) {
            Boolean[] b = new Boolean[8];
            int temp = result.get(i);

            for (int j = 7; j >= 0; j--) {
                if (superincreasingSequence.get(j) > temp) {
                    b[j] = false;
                    continue;
                }
                temp -= superincreasingSequence.get(j);
                b[j] = true;
            }

            binaryOutputBlocks[i] = Arrays.stream(b).map(flag -> flag ? "1" : "0").collect(Collectors.joining());
        }

        return binaryOutputBlocks;
    }

    static String getStringResult(String[] binaryInputBlocks, List<Integer> superincreasingSequence,
                                  int modulus, int multiplier, List<Integer> publicKey, List<Integer> outputSums) {
        Map<MerkleHellmanResultType, String> resultMap = new EnumMap<>(MerkleHellmanResultType.class);

        resultMap.put(MerkleHellmanResultType.TEXT_INPUT, ConverterUtils.toTextString(binaryInputBlocks));
        resultMap.put(MerkleHellmanResultType.BINARY_INPUT, Arrays.stream(binaryInputBlocks).collect(Collectors.joining()));
        resultMap.put(MerkleHellmanResultType.HEX_INPUT, ConverterUtils.toHexString(binaryInputBlocks));
        resultMap.put(MerkleHellmanResultType.PRIVATE_KEY,
                String.format("w=%s, modulus=%d, multiplier=%d", superincreasingSequence, modulus, multiplier));
        resultMap.put(MerkleHellmanResultType.PUBLIC_KEY, publicKey.toString());
        resultMap.put(MerkleHellmanResultType.OUTPUT_SUMS, outputSums.toString());

        return resultMap.entrySet().stream().map(StringUtils::createLineFromEntry).collect(Collectors.joining());
    }

    static String getStringResult(List<Integer> inputSums, List<Integer> superincreasingSequence,
                                  int modulus, int multiplier, String[] binaryOutputBlocks) {
        Map<MerkleHellmanResultType, String> resultMap = new EnumMap<>(MerkleHellmanResultType.class);

        resultMap.put(MerkleHellmanResultType.INPUT_SUMS, inputSums.toString());
        resultMap.put(MerkleHellmanResultType.PRIVATE_KEY, String.format("w=%s, modulus=%d, multiplier=%d",
                superincreasingSequence, modulus, multiplier));
        resultMap.put(MerkleHellmanResultType.TEXT_OUTPUT, ConverterUtils.toTextString(binaryOutputBlocks));
        resultMap.put(MerkleHellmanResultType.BINARY_OUTPUT, Arrays.stream(binaryOutputBlocks).collect(Collectors.joining()));
        resultMap.put(MerkleHellmanResultType.HEX_OUTPUT, ConverterUtils.toHexString(binaryOutputBlocks));

        return resultMap.entrySet().stream().map(StringUtils::createLineFromEntry).collect(Collectors.joining());
    }
}
