package com.youzan.zan;

import org.jetbrains.annotations.NotNull;

import java.io.*;

public class $ {

    public static String exec(@NotNull final String[] cmdarray) {
        InputStream input = null;
        try {
            Process p = Runtime.getRuntime().exec(cmdarray);
            int exitStatus = p.waitFor();
            if (exitStatus != 0) {
                return "";
            }

            StringBuilder sb = new StringBuilder();
            input = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            String line = "";
            boolean isFirst = true;
            while ((line = reader.readLine()) != null) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    sb.append("\n");
                }
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException | InterruptedException ignore) {
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ignore) {
                }
            }
        }
        return "";
    }

    public static String execPhp(@NotNull final String code) {
        return exec(new String[]{"/bin/bash", "-c", "php -r 'error_reporting(0);" + code + "'"});
    }

    public static boolean fileExist(@NotNull String path) {
        return new File(path).exists();
    }

    public static<T> void echo(T t)  {
        System.out.println(t);
    }

}
