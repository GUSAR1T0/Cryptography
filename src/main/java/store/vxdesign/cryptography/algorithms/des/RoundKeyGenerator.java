package store.vxdesign.cryptography.algorithms.des;

/**
 * @author Roman Mashenkin
 * @since 18.10.2017
 */
final class RoundKeyGenerator {

    static String[] createRoundHalfKeysWithLeftShift(String keyPermutation, int begin, int end) {
        String[] a = new String[DataEncryptionStandardConstants.COUNT_OF_ROUNDS + 1];
        a[0] = end != -1 ? keyPermutation.substring(begin, end) : keyPermutation.substring(begin);
        for (int round = 1; round <= DataEncryptionStandardConstants.COUNT_OF_ROUNDS; round++) {
            a[round] = DataEncryptionStandardUtils.leftShiftBits(a[round - 1], round);
        }
        return a;
    }

    static String[] createRoundHalfKeysWithLeftShift(String keyPermutation, int begin) {
        return createRoundHalfKeysWithLeftShift(keyPermutation, begin, -1);
    }

    static String[] createRoundKeysSequences(String[] c, String[] d) {
        String[] k = new String[DataEncryptionStandardConstants.COUNT_OF_ROUNDS + 1];
        for (int round = 0; round < DataEncryptionStandardConstants.COUNT_OF_ROUNDS + 1; round++) {
            k[round] = String.format("%s%s", c[round], d[round]);
        }
        return k;
    }
}
