package com.orion.utils.math;

/**
 * 进制转换
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/16 10:26
 */
public class Hex {

    private Hex() {
    }

    /**
     * byte转十六进制
     *
     * @param b byte
     * @return 十六进制
     */
    public static String byteToHex(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * byte[]转十六进制
     *
     * @param bs byte[]
     * @return 十六进制
     */
    public static String bytesToHex(byte[] bs) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bs) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 十六进制转byte
     *
     * @param s hexString
     * @return byte
     */
    public static byte hexToByte(String s) {
        return (byte) Integer.parseInt(s, 16);
    }

    /**
     * 十六进制转byte[]
     *
     * @param s hexString
     * @return byte[]
     */
    public static byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] result;
        if (len % 2 == 1) {
            // 奇数
            len++;
            result = new byte[(len / 2)];
            s = "0" + s;
        } else {
            // 偶数
            result = new byte[(len / 2)];
        }
        int j = 0;
        for (int i = 0; i < len; i += 2) {
            result[j] = hexToByte(s.substring(i, i + 2));
            j++;
        }
        return result;
    }

    public static String encode(String s) {
        return bytesToHex(s.getBytes());
    }

    public static String decode(String s) {
        return new String(hexToBytes(s));
    }

}
