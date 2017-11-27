package capital.scalable.webcrawler.crawler;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

/**
 * Created by Nurakhmetov on 26.11.2017.
 */
public class GoogleRequesterTest {
    private final String[] fetchedResultsExpected = new String[]{
            "https://nl.wikipedia.org/wiki/Kunststof",
            "https://en.wikipedia.org/wiki/Plastic",
            "https://nl.wiktionary.org/wiki/plastic",
            "https://www.plasticsoupfoundation.org/",
            "https://www.scientias.nl/zo-kunnen-we-99-plastic-oceanen-verdwenen-is-opsporen/",
            "https://www.volkskrant.nl/wetenschap/achteraf-scheiden-van-plastic-in-afvalfabrieken-is-goedkoper-en-efficienter-dan-vooraf-klopt-dit-wel~a4532556/",
            "https://nos.nl/artikel/2193860-plastic-afval-recyclen-heeft-weinig-effect-op-milieu.html",
            "https://www.schooltv.nl/video/het-klokhuis-plastic-2/",
            "http://www.plasticheroes.nl/wat"
    };
    private GoogleRequester googleRequester;


    private List<String> getHtmlData(URI path) throws IOException {
        return Files.lines(Paths.get(path)).collect(toList());
    }

    @Before
    public void prepare() throws IOException, URISyntaxException {
        googleRequester = spy(GoogleRequester.class);
        doReturn(getHtmlData(getClass().getResource("/testGetFetchedResults.html").toURI()))
                .when(googleRequester).downloadRemote(any());

    }

    @Test
    public void testGetFetchedResults() throws Exception {
        List<String> fetchedResults = googleRequester.getFetchedResults("test");
        assertArrayEquals(fetchedResultsExpected, fetchedResults.toArray());
    }
}