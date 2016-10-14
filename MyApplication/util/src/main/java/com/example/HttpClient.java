package com.example;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
    public static String get(String adress) {
        try {
            URL url = new URL(adress);
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
}
