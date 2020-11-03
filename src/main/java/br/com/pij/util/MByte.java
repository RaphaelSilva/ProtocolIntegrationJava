package br.com.pij.util;

import java.util.Arrays;

public class MByte {
    public static byte[] concatAll(byte[]... args) {
        byte[] ret = args[0];
        for (var i = 1; i < args.length; i++) {
            byte[] aux = ret;
            ret = new byte[aux.length+args[i].length];
            System.arraycopy(aux, 0, ret, 0, aux.length);
            System.arraycopy(args[i], 0, ret, aux.length, args[i].length);
        }
        return ret;
    }
}
