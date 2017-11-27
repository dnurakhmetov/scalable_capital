package capital.scalable.webcrawler.crawler;

import java.util.List;

/**
 * Created by Nurakhmetov on 26.11.2017.
 */

/**
 * A class that supplies factory method to retrieve Search Engine Requester according to the <tt>SE</tt> type provided
 */
public class SEFactory {

    private SEFactory() {
    }

    /**
     * A factory method that resolves Search engine Requester class according to <tt>SE</tt> type provided
     * @param seType
     * @return
     */
    static SERequester getSEREquester(SE seType) {
        switch (seType) {
            case GOOGLE:
                return new GoogleRequester();
            case YANDEX:
            case YAHOO:
            default:
                throw new UnsupportedOperationException();
        }
    }

    public enum SE {
        GOOGLE,
        YANDEX,
        YAHOO
    }

    interface SERequester {
        /**
         * Returns list of website links from search result query from Search engine by keyword
         * @param query a keyword to query Search Engine
         */
        List<String> getFetchedResults(String query);
    }
}
