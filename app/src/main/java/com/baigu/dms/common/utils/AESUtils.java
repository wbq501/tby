package com.baigu.dms.common.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/10/16 21:30
 */
public class AESUtils {

    public static byte[] t = new byte[]{48, 48, 49, 55, 68, 67, 49, 66, 69, 50, 50, 53, 56, 53, 53, 52, 67, 70, 48, 50, 67, 53, 55, 66, 55, 56, 69, 55, 52, 48, 65, 53};

    public AESUtils() {
    }

    public static String encrypt(String var0, String var1) {
        byte[] var2 = null;

        try {
            var2 = encrypt(getRawKey(var0.getBytes()), var1.getBytes());
        } catch (Exception var3) {
            ;
        }

        return var2 != null?toHex(var2):null;
    }

    public static String decrypt(String var0, String var1) {
        try {
            byte[] var3 = getRawKey(var0.getBytes());
            byte[] var4 = toByte(var1);
            var3 = decrypt(var3, var4);
            return new String(var3);
        } catch (Exception var2) {
            return null;
        }
    }

    private static byte[] getRawKey(byte[] var0) throws Exception {
        return toByte(new String(t, 0, 32));
    }

    private static byte[] encrypt(byte[] var0, byte[] var1) throws Exception {
        SecretKeySpec var3 = new SecretKeySpec(var0, "AES");
        Cipher var2;
        (var2 = Cipher.getInstance("AES/CBC/PKCS5Padding")).init(1, var3, new IvParameterSpec(new byte[var2.getBlockSize()]));
        return var2.doFinal(var1);
    }

    private static byte[] decrypt(byte[] var0, byte[] var1) throws Exception {
        SecretKeySpec var3 = new SecretKeySpec(var0, "AES");
        Cipher var2;
        (var2 = Cipher.getInstance("AES/CBC/PKCS5Padding")).init(2, var3, new IvParameterSpec(new byte[var2.getBlockSize()]));
        return var2.doFinal(var1);
    }

    public static String toHex(String var0) {
        return toHex(var0.getBytes());
    }

    public static String fromHex(String var0) {
        return new String(toByte(var0));
    }

    public static byte[] toByte(String var0) {
        int var1;
        byte[] var2 = new byte[var1 = var0.length() / 2];

        for(int var3 = 0; var3 < var1; ++var3) {
            var2[var3] = Integer.valueOf(var0.substring(2 * var3, 2 * var3 + 2), 16).byteValue();
        }

        return var2;
    }

    public static String toHex(byte[] var0) {
        if(var0 == null) {
            return "";
        } else {
            StringBuffer var1 = new StringBuffer(2 * var0.length);

            for(int var2 = 0; var2 < var0.length; ++var2) {
                appendHex(var1, var0[var2]);
            }

            return var1.toString();
        }
    }

    private static void appendHex(StringBuffer var0, byte var1) {
        var0.append("0123456789ABCDEF".charAt(var1 >> 4 & 15)).append("0123456789ABCDEF".charAt(var1 & 15));
    }
}
