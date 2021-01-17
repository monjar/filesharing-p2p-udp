package util;

import java.nio.charset.StandardCharsets;

public class DataHelpers {
    public static String parseBytes(byte[] a) {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a.length > i && a[i] != 0) {
            ret.append((char) a[i]);
            i++;
        }
        return ret.toString();
    }
}
