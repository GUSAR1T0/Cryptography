package store.vxdesign.cryptography.algorithms.mh;

import store.vxdesign.cryptography.framework.utilities.StringUtils;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Roman Mashenkin
 * @since 19.10.2017
 */
class MerkleHellmanUtils {

    static List<Integer> parseStringSuperincreasingSequence(String superincreasingSequence) {
        return Arrays.stream(superincreasingSequence.split(",\\s+|\\s+|,"))
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toList());
    }

    static List<Integer> generateSuperincreasingSequence() {
        return new SecureRandom().ints(
                MerkleHellmanConstants.SEQUENCE_ELEMENTS,
                MerkleHellmanConstants.BEGIN_NUMBER,
                MerkleHellmanConstants.END_NUMBER
        ).sorted().boxed().collect(Collectors.toList());
    }

    static int generatePrimeNumber(int begin) {
        return getRandomNumber(() -> IntStream.rangeClosed(begin + 1, MerkleHellmanConstants.END_NUMBER * MerkleHellmanConstants.SEQUENCE_ELEMENTS)
                .filter(i -> IntStream.rangeClosed(2, (int) Math.sqrt(i))
                        .allMatch(j -> i % j != 0))
                .boxed()
                .collect(Collectors.toList()));
    }

    static int generateNumber(int begin, int end) {
        return getRandomNumber(() -> IntStream.range(begin, end).boxed().collect(Collectors.toList()));
    }

    private static int getRandomNumber(Supplier<List<Integer>> supplier) {
        Random r = new SecureRandom();
        List<Integer> list = supplier.get();
        return list.stream().skip(r.nextInt(list.size() - 1)).findFirst().orElse(-1);
    }

    static String getResult(String input, String[] binaryInputBlocks, List<Integer> superincreasingSequence, int q, int r, List<Integer> publicKey, List<Integer> outputSums, int blockSize) {
        Map<MerkleHellmanResultType, String> resultMap = new EnumMap<>(MerkleHellmanResultType.class);

        resultMap.put(MerkleHellmanResultType.TEXT_INPUT, input);
        resultMap.put(MerkleHellmanResultType.BINARY_INPUT, Arrays.stream(binaryInputBlocks).collect(Collectors.joining()));
        resultMap.put(MerkleHellmanResultType.PRIVATE_KEY, String.format("w=%s, q=%d, r=%d", superincreasingSequence, q, r));
        resultMap.put(MerkleHellmanResultType.PUBLIC_KEY, publicKey.toString());
        resultMap.put(MerkleHellmanResultType.OUTPUT_SUM, outputSums.toString());

        return resultMap.entrySet().stream().map(StringUtils::createLineFromEntry).collect(Collectors.joining());
    }
}
