package com.example;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
    public static byte[] getByteArray(String adress) throws IOException {
        InputStream inputStream = getStream(adress);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(inputStream.available());
        byte[] chunk = new byte[1 << 16];
        int bytesRead;
        while ((bytesRead = inputStream.read(chunk)) > 0) {
            outputStream.write(chunk, 0, bytesRead);
        }

        return outputStream.toByteArray();
    }

    public static String get(String address) {
        try {
            URL url = new URL(address);
            HttpURLConnection connection = ((HttpURLConnection) url.openConnection());
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[inputStream.available()];
            int lenght;
            while ((lenght = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, lenght);
            }
            inputStream.close();

            return result.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Error";
    }

    public static InputStream getStream(String address) throws IOException {
        URL url = new URL(address);
        HttpURLConnection connection = ((HttpURLConnection) url.openConnection());
        return connection.getInputStream();
    }
}
