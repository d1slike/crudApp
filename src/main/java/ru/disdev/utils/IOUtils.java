package ru.disdev.utils;

import ru.disdev.MainApplication;

import java.io.*;
import java.nio.charset.Charset;

public class IOUtils {
    public static InputStreamReader getResourceAsReader(String path) {
        return new InputStreamReader(MainApplication.class.getResourceAsStream(path));
    }

    public static Reader getWin1251FileReader(File file) throws FileNotFoundException {
        return new InputStreamReader(new FileInputStream(file), Charset.forName("windows-1251"));
    }

    public static Writer getWin1251FileWriter(File file) throws FileNotFoundException {
        return new OutputStreamWriter(new FileOutputStream(file), Charset.forName("windows-1251"));
    }
}
