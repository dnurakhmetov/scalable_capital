package capital.scalable.webcrawler.crawler;

import capital.scalable.webcrawler.model.ParsedData;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

/**
 * Created by Nurakhmetov on 26.11.2017.
 */
public class CrawlerTest {

    private Crawler crawler;

    private List<String> getHtmlData(URI path) throws IOException {
        return Files.lines(Paths.get(path)).collect(toList());
    }


    @Before
    public void prepare() throws URISyntaxException, IOException {
        crawler = spy(new Crawler(SEFactory.SE.GOOGLE));
        doReturn(ImmutableList.of("https://nl.wikipedia.org/wiki/Kunststof", "https://nl.wikipedia.org/wiki/Kunststof2",
                "https://nl.wikipedia.org/wiki/Kunststof3", "https://nl.wikipedia.org/wiki/Kunststof4",
                "https://nl.wikipedia.org/wiki/Kunststof5", "https://nl.wikipedia.org/wiki/Kunststof6",
                "https://nl.wikipedia.org/wiki/Kunststof7", "https://nl.wikipedia.org/wiki/Kunststof8",
                "https://nl.wikipedia.org/wiki/Kunststof9"))
                .when(crawler).fetchSearchResults(any(), eq(SEFactory.SE.GOOGLE));
        doReturn(getHtmlData(getClass().getResource("/Kunststof.html").toURI()))
                .when(crawler).downloadRemote("https://nl.wikipedia.org/wiki/Kunststof");
        doReturn(getHtmlData(getClass().getResource("/Kunststof2.html").toURI()))
                .when(crawler).downloadRemote("https://nl.wikipedia.org/wiki/Kunststof2");
        doReturn(getHtmlData(getClass().getResource("/Kunststof3.html").toURI()))
                .when(crawler).downloadRemote("https://nl.wikipedia.org/wiki/Kunststof3");
        doReturn(getHtmlData(getClass().getResource("/Kunststof4.html").toURI()))
                .when(crawler).downloadRemote("https://nl.wikipedia.org/wiki/Kunststof4");
        doReturn(getHtmlData(getClass().getResource("/Kunststof5.html").toURI()))
                .when(crawler).downloadRemote("https://nl.wikipedia.org/wiki/Kunststof5");
        doReturn(getHtmlData(getClass().getResource("/Kunststof6.html").toURI()))
                .when(crawler).downloadRemote("https://nl.wikipedia.org/wiki/Kunststof6");
        doReturn(getHtmlData(getClass().getResource("/Kunststof7.html").toURI()))
                .when(crawler).downloadRemote("https://nl.wikipedia.org/wiki/Kunststof7");
        doReturn(getHtmlData(getClass().getResource("/Kunststof8.html").toURI()))
                .when(crawler).downloadRemote("https://nl.wikipedia.org/wiki/Kunststof8");
        doReturn(getHtmlData(getClass().getResource("/Kunststof9.html").toURI()))
                .when(crawler).downloadRemote("https://nl.wikipedia.org/wiki/Kunststof9");
        doReturn(getHtmlData(getClass().getResource("/jquery.js").toURI()))
                .when(crawler).downloadRemote("https://code.jquery.com/jquery-3.2.1.min.js");
        doReturn(getHtmlData(getClass().getResource("/video.js").toURI()))
                .when(crawler).downloadRemote("http://vjs.zencdn.net/5.19/video.min.js");
        doReturn(getHtmlData(getClass().getResource("/chart.js").toURI()))
                .when(crawler).downloadRemote("http://chart.bundle.min.js");
        doReturn(getHtmlData(getClass().getResource("/moment.js").toURI()))
                .when(crawler).downloadRemote("https://moment.js");
        doReturn(getHtmlData(getClass().getResource("/chart.js").toURI()))
                .when(crawler).downloadRemote("https://chart.js");
        doReturn(getHtmlData(getClass().getResource("/test.js").toURI()))
                .when(crawler).downloadRemote("https://test.js");
        doReturn(getHtmlData(getClass().getResource("/angular.js").toURI()))
                .when(crawler).downloadRemote("https://angular.js");
        doReturn(getHtmlData(getClass().getResource("/react.js").toURI()))
                .when(crawler).downloadRemote("https://react.js");
        doReturn(getHtmlData(getClass().getResource("/prototype.js").toURI()))
                .when(crawler).downloadRemote("https://prototype.js");

    }

    @Test
    public void testCrawl() throws Exception {
        List<String> result = crawler.crawl("test");
        assertEquals(8, result.size());
        assertEquals("jquery-3.2.1.min.js", result.get(0));
        assertEquals("moment.js", result.get(1));
        assertEquals("react.js", result.get(2));
        assertEquals("chart.bundle.min.js", result.get(3));
        assertEquals("angular.js", result.get(4));
        assertEquals("test.js", result.get(5));
        assertEquals("prototype.js", result.get(6));
        assertEquals("video.min.js", result.get(7));
    }

    @Test
    public void testMapSearchResult() throws Exception {
        List<ParsedData> result = crawler.mapSearchResult("https://nl.wikipedia.org/wiki/Kunststof").collect(toList());
        assertEquals(1, result.size());
        assertEquals("jquery-3.2.1.min.js", result.iterator().next().getName());
    }
}