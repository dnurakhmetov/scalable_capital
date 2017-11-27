package capital.scalable.webcrawler.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A utilities class that provides useful method to perform web crawling
 *
 */
public class CrawlerUtils {

    private static final String USER_AGENT =
            "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2";

    private CrawlerUtils() {
    }

    public static List<String> downloadRemote(String link) {
        try {
            URL url = new URL(link);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("User-Agent", USER_AGENT);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                String line = br.readLine();
                List<String> downloadedLines = new ArrayList<>();
                while (line != null) {
                    downloadedLines.add(line);
                    line = br.readLine();
                }
                return downloadedLines;
            }
        } catch (IOException e) {
            System.err.println("Couldn't download remote from URL: " + link);
            return Collections.emptyList();
        }
    }
}
