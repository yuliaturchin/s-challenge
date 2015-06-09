package com.challenge.utils;

import java.util.StringTokenizer;
import java.util.UUID;

/**
 * Created by YULIAT on 06/07/2015.
 */
public class CommonMethods {

    private static final char [] CHARS = new char [] {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    private final static String IGNORABLE_CHARS = " ,_.:\\?-!";

    public static String normalizeString(String string, String tokensSeparator) {

        //regex takes too much time
//        return productName.replaceAll(NORMALIZATION_REGEX, tokensSeparator).toLowerCase();
        String lowerCase = string.toLowerCase();
        StringTokenizer tokens = new StringTokenizer(lowerCase, IGNORABLE_CHARS);
        StringBuilder builder = new StringBuilder();
        while (tokens.hasMoreTokens())
        {
            builder.append(tokens.nextToken());
            if (tokens.hasMoreTokens())
            {
                builder.append(tokensSeparator);
            }
        }
        return builder.toString();
    }

    public static  StringTokenizer getNormalizedTokens(String string) {

        //regex takes too much time
//        return productName.replaceAll(NORMALIZATION_REGEX, tokensSeparator).toLowerCase();
        String lowerCase = string.toLowerCase();
        StringTokenizer tokens = new StringTokenizer(lowerCase, IGNORABLE_CHARS);
        return tokens;
    }

    /**
     * Gets an universally unique identifier (UUID).
     *
     * @return String representation of generated UUID.
     */
    public static String nextUUID() {
        UUID uuid = UUID.randomUUID();

        StringBuilder buff = new StringBuilder(32);
        long2string(uuid.getMostSignificantBits(), buff);
        long2string(uuid.getLeastSignificantBits(), buff);

        return buff.toString();
    }

    private static void long2string(long l, StringBuilder buff) {
        for (int i = 0; i < 16; i++) {
            long nextByte = l & 0xF000000000000000l;
            l <<= 4;
            boolean isNegative = nextByte < 0;
            nextByte = rightShift(nextByte, 60);

            if (isNegative) {
                nextByte |= 0x08;
            }

            buff.append(CHARS[(int) nextByte]);
        }
    }

    private static long rightShift(long l, int n) {
        return l  >>> n;
    }

}
