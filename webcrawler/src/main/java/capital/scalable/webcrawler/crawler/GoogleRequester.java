package capital.scalable.webcrawler.crawler;

import com.google.common.annotations.VisibleForTesting;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

/**
 *
 * A class of Requester object that implements <tt>SERequester</tt> interface for Google Search engine.
 *
 */
public class GoogleRequester implements SEFactory.SERequester {
    private static final Pattern HREF_PATTERN = Pattern.compile("(.*)(href=\")(.*)(\")(.*)");
    private static final String DEFAULT_URL = "http://www.google.com/search?q=";

    @Override
    public List<String> getFetchedResults(String query) {
        String googlePage = String.join("\n", downloadRemote(DEFAULT_URL + query));
        return Arrays.stream(googlePage.split("<h3 class=\"r\">")).skip(1)
                .map(this::extractSearchResultHref).filter(Optional::isPresent).map(Optional::get)
                .collect(toList());
    }

    @VisibleForTesting
    List<String> downloadRemote(String link) {
        return CrawlerUtils.downloadRemote(link);
    }

    private Optional<String> extractSearchResultHref(String line) {
        line = line.substring(0, line.indexOf("</h3>"));
        Matcher matcher = HREF_PATTERN.matcher(line);
        if (matcher.matches()) {
            String match = matcher.group(3);
            if (match.startsWith("/url?q=") && match.contains("&amp;sa=U")) {
                return Optional.of(match.substring(7, match.indexOf("&amp;sa=U")));
            }
        }
        return Optional.empty();
    }
}
