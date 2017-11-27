package capital.scalable.webcrawler.crawler;

import capital.scalable.webcrawler.model.ParsedData;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.hash.Hashing;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * A class of crawler object that performs Search Engine lookup by keyword and returns list
 * of registered Javascript libraries in sorted by descending order
 *
 */
public class Crawler {
    private static final Pattern JAVASCRIPT_PATTERN =
            Pattern.compile("^.*<script.*src=\"(.*)\".*$|^.*<script.*src=\'(.*)\'.*$");
    private final SEFactory.SE seType;

    public Crawler(SEFactory.SE seType) {
        this.seType = seType;
    }

    /**
     * Returns sorted <tt>List</tt> in descending order of their occurrence of all parsed Javascript libraries
     * registered on websites that are retrieved from Search Engine by specified keyword
     *
     * @param query a keyword used to query Search Engine
     */
    public List<String> crawl(String query) {
        Map<String, List<ParsedData>> result =
                fetchSearchResults(query, seType).stream().parallel().flatMap(this::mapSearchResult)
                        .collect(groupingBy(ParsedData::getHash));
        return result.entrySet().stream().sorted((entry1, entry2) -> entry2.getValue().size() - entry1.getValue().size())
                .map(entry -> entry.getValue().iterator().next().getName()).collect(toList());

    }

    /**
     * Returns <tt>List</tt> of website links fetched from Search engine
     *
     * @param query a keyword used to query Search Engine
     * @param seType <tt>SE</tt> enum of Search Engines used in factory method
     */
    @VisibleForTesting
    List<String> fetchSearchResults(String query, SEFactory.SE seType) {
        return SEFactory.getSEREquester(seType).getFetchedResults(query);
    }

    /**
     * Returns <tt>List</tt> of rows of downloaded remote object
     * <p>This method is required for mock in unit testing</p>
     * @param link URL link to remote object
     */
    @VisibleForTesting
    List<String> downloadRemote(String link) {
        return CrawlerUtils.downloadRemote(link);
    }

    /**
     * Returns <tt>Stream</tt> of <tt>ParsedData</tt> objects from website that is fetched from Search Engine result
     *
     * @param link website link
     */
    @VisibleForTesting
    Stream<ParsedData> mapSearchResult(String link) {
        List<String> jsScripts = downloadRemote(link).stream().map(this::extractJSSrc)
                .filter(Optional::isPresent).map(Optional::get).collect(toList());

        return jsScripts.parallelStream().map(script -> new ParsedData(extractJsName(script),
                extractJsHash(link, script)));
    }

    /**
     * Returns Javascript library name from extracted 'SRC' attribute value
     *
     * @param path
     * @return
     */
    private String extractJsName(String path) {
        if (path.lastIndexOf("/") >= 0) {
            return path.substring(path.lastIndexOf("/") + 1);
        } else {
            return path.trim();
        }
    }

    /**
     * Downloads Javascript library based on provided url and path and returns calculated hash of downloaded contents.
     *
     * @param url main source link of fetched website from Search Engine
     * @param path a path extracted from 'SRC' tag
     */
    private String extractJsHash(String url, String path) {
        if (path.trim().startsWith("http")) {
            return calculateHash(String.join("/n", downloadRemote(path)));
        } else {
            return calculateHash(String.join("/n", downloadRemote(buildUrl(url, path))));
        }
    }

    /**
     * Return <tt>String</tt> of URL to remote using path extracted from 'SRC' attribute.
     *
     * <p>Combines with domain extracted from main source link if path was relative</p>
     *
     * @param source main source link of fetched website from Search Engine
     * @param path a path extracted from 'SRC' tag
     */
    private String buildUrl(String source, String path) {
        try {
            URL url = new URL(source);
            return String.format("%s://%s/%s", url.getProtocol(), url.getHost(), path);
        } catch (MalformedURLException e) {
            System.err.println("Couldn't build URL from remote " + source);
            return null;
        }
    }


    /**
     * Return hash of Javascript library contents using SHA-256 algorithm
     *
     * @param value contents of Javascript library
     */
    private String calculateHash(String value) {
        return Hashing.sha256().hashString(value, StandardCharsets.UTF_8).toString();
    }

    /**
     * Returns <tt>Optional</tt> of <tt>String</tt> value of the 'SRC' attribute of 'SCRIPT' HTML tag.
     *
     * @param line HTML row to process
     */
    private Optional<String> extractJSSrc(String line) {
        Matcher matcher = JAVASCRIPT_PATTERN.matcher(line.trim());
        if (matcher.matches()) {
            return Optional.of(matcher.group(1));
        } else {
            return Optional.empty();
        }
    }

}
