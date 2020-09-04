package com.example.boot.utils;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class TestHex {
    /**
     * precomputed translate table for chars 0..'f'
     */
    private static byte[] correspondingNibble = new byte['f' + 1];
// -------------------------- PUBLIC STATIC METHODS --------------------------

    /**
     * Convert a hex string to an unsigned byte array.
     * Permits upper or lower case hex.
     *
     * @param s String must have even number of characters.
     *          and be formed only of digits 0-9 A-F or
     *          a-f. No spaces, minus or plus signs.
     * @return corresponding unsigned byte array. see http://mindprod.com/jgloss/unsigned.html
     */
    public static byte[] fromHexString(String s) {
        int stringLength = s.length();
        if ((stringLength & 0x1) != 0) {
            throw new IllegalArgumentException("fromHexString requires an even number of hex characters");
        }
        byte[] bytes = new byte[stringLength / 2];

        for (int i = 0, j = 0; i < stringLength; i += 2, j++) {
            int high = charToNibble(s.charAt(i));
            int low = charToNibble(s.charAt(i + 1));
            // You can store either unsigned 0..255 or signed -128..127 bytes in a byte type.
            bytes[j] = (byte) ((high << 4) | low);
        }
        return bytes;
    }

// -------------------------- STATIC METHODS --------------------------

    static {
        // only 0..9 A..F a..f have meaning. rest are errors.
        for (int i = 0; i <= 'f'; i++) {
            correspondingNibble[i] = -1;
        }
        for (int i = '0'; i <= '9'; i++) {
            correspondingNibble[i] = (byte) (i - '0');
        }
        for (int i = 'A'; i <= 'F'; i++) {
            correspondingNibble[i] = (byte) (i - 'A' + 10);
        }
        for (int i = 'a'; i <= 'f'; i++) {
            correspondingNibble[i] = (byte) (i - 'a' + 10);
        }
    }

    /**
     * convert  a single char to corresponding nibble using a precalculated array.
     * Based on code by:
     * Brian Marquis
     * Orion Group Software Engineers http://www.ogse.com
     *
     * @param c char to convert. must be 0-9 a-f A-F, no
     *          spaces, plus or minus signs.
     * @return corresponding integer  0..15
     * @throws IllegalArgumentException on invalid c.
     */
    private static int charToNibble(char c) {
        if (c > 'f') {
            throw new IllegalArgumentException("Invalid hex character: " + c);
        }
        int nibble = correspondingNibble[c];
        if (nibble < 0) {
            throw new IllegalArgumentException("Invalid hex character: " + c);
        }
        return nibble;
    }

    /**
     * code not used, for explanation only.
     * convert a single char to corresponding nibble.
     * Slow version, easier to understand.
     *
     * @param c char to convert. must be 0-9 a-f A-F, no
     *          spaces, plus or minus signs.
     * @return corresponding integer
     */
    private static int slowCharToNibble(char c) {
        if ('0' <= c && c <= '9') {
            return c - '0';
        } else if ('a' <= c && c <= 'f') {
            return c - 'a' + 0xa;
        } else if ('A' <= c && c <= 'F') {
            return c - 'A' + 0xa;
        } else {
            throw new IllegalArgumentException("Invalid hex character: " + c);
        }
    }

// --------------------------- main() method ---------------------------

    /**
     * Test harness
     *
     * @param args not used
     */
    public static void main(String[] args) {
        String hexString = "FFD8FFE000104A46494600010100000100010000FFDB0043000503040404030504040405050506070C08070707070F";
        System.out.println(hexString);
        // convert hex string to an array of bytes
        byte[] bytes = fromHexString(hexString);
        System.out.println(bytes.length + " bytes long");
        try {
            OutputStream os = new FileOutputStream("d:/a.data");
            os.write(bytes);
            os.flush();
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
