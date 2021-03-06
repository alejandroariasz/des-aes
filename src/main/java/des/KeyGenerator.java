package des;

import util.Util;

import java.util.BitSet;
import java.util.stream.IntStream;


public class KeyGenerator {

    // Subtract 1 to get the correct position
    private static final int[] PC1 = {
        57, 49, 41, 33, 25, 17,  9,
         1, 58, 50, 42, 34, 26, 18,
        10,  2, 59, 51, 43, 35, 27,
        19, 11,  3, 60, 52, 44, 36,
        63, 55, 47, 39, 31, 23, 15,
         7, 62, 54, 46, 38, 30, 22,
        14,  6, 61, 53, 45, 37, 29,
        21, 13,  5, 28, 20, 12,  4
    };

    // Subtract 1 to get the correct position
    private static final int[] PC2 = {
        14, 17, 11, 24,  1,  5,
         3, 28, 15,  6, 21, 10,
        23, 19, 12,  4, 26,  8,
        16,  7, 27, 20, 13,  2,
        41, 52, 31, 37, 47, 55,
        30, 40, 51, 45, 33, 48,
        44, 49, 39, 56, 34, 53,
        46, 42, 50, 36, 29, 32
    };

    public static BitSet[] generateKeys(BitSet initialKey) {
        BitSet[] subKeys = new BitSet[16];
        BitSet pc1Output = getPC1(initialKey);
        BitSet C_i = pc1Output.get(28, 56);
        BitSet D_i = pc1Output.get(0, 28);

        for (int i = 1; i <= 16; ++i) {

            C_i = leftShift(C_i, i, 28);
            D_i = leftShift(D_i, i, 28);
            subKeys[i - 1] = getPC2(Util.concatenateBitStrings(C_i, D_i, 56));
        }
        return subKeys;
    }

    private static BitSet getPC1(BitSet key) {
        BitSet pc1 = new BitSet(56);
        for (int i = 0; i < PC1.length; i++)
            pc1.set(55 - i, key.get(64 - PC1[i]));
        return pc1;
    }

    private static BitSet getPC2(BitSet subKey) {
        BitSet pc2 = new BitSet(48);
        for (int i = 0; i < PC2.length; ++i)
            pc2.set(47 - i, subKey.get(56 - PC2[i]));
        return pc2;
    }

    private static BitSet leftShift(BitSet bitString, int index, int n) {
        if (bitString.length() == 0)
            return bitString;
        BitSet shiftedBitString;
        long key = bitString.toLongArray()[0];
        long shiftedKey;
        switch (index) {
            case 1:
            case 2:
            case 9:
            case 16:
                shiftedKey = (key << 1)|(key >> (n - 1));
                shiftedBitString = BitSet.valueOf(new long[]{shiftedKey});
                break;
            default:
                shiftedKey = (key << 2)|(key >> (n - 2));
                shiftedBitString = BitSet.valueOf(new long[]{shiftedKey});
        }
        return shiftedBitString;
    }

     public static void main(String[] args) {
        long keyNumber = 1383827165325090801L; //133457799BBCDFF1 in hex
        BitSet bitKey = BitSet.valueOf(new long[]{keyNumber});
        System.out.println("key " +Util.convertBitSetToString(bitKey, 64));
        System.out.println(Util.convertBitSetToString(leftShift(bitKey, 2, 64), 64));
        System.out.println(Util.convertBitSetToString(leftShift(bitKey, 3, 64), 64));

        BitSet pc1 = getPC1(bitKey);
        System.out.println("pc1 " + Util.convertBitSetToString(pc1, 56));

        System.out.println("\nGenerated keys\n");
        BitSet[] keys = generateKeys(bitKey);
        for (BitSet key : keys)
            System.out.println(Util.convertBitSetToString(key, 48));
    }

}
