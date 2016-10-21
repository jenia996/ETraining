package com.example;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpClient {
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

    public static InputStream getStream(String address) {
        try {
            URL url = new URL(address);
            HttpURLConnection connection = ((HttpURLConnection) url.openConnection());
            return connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
    }
}
