package solopov;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTMLDownloader {
    public void getText(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();


        int responseCode = connection.getResponseCode();
        InputStream inputStream;
        if (200 <= responseCode && responseCode <= 299) {
            inputStream = connection.getInputStream();
        } else {
            inputStream = connection.getErrorStream();
        }

        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        inputStream));

        String currentLine;

        BufferedWriter writer = new BufferedWriter(new FileWriter("doc.html"));

        while ((currentLine = in.readLine()) != null) {
            writer.write(currentLine);
            writer.write("/n");
        }

        in.close();
        writer.close();
    }
}
