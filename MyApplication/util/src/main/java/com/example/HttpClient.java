package com.example;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpClient {

    public static byte[] getByteArray(final String adress) throws IOException {
        final InputStream inputStream = getStream(adress);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(inputStream.available());
        final byte[] chunk = new byte[1 << 16];
        int bytesRead;
        while ((bytesRead = inputStream.read(chunk)) > 0) {
            outputStream.write(chunk, 0, bytesRead);
        }

        return outputStream.toByteArray();
    }

    public static String get(final String address) throws IOException {
        final URL url = new URL(address);
        final HttpsURLConnection connection = ((HttpsURLConnection) url.openConnection());
        final InputStream inputStream = connection.getInputStream();
        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        final byte[] buffer = new byte[Math.max(inputStream.available(), 1 << 16)];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        inputStream.close();
        return result.toString();
    }

    private static InputStream getStream(final String address) throws IOException {
        final URL url = new URL(address);
        final HttpURLConnection connection = ((HttpURLConnection) url.openConnection());
        return connection.getInputStream();
    }
}
