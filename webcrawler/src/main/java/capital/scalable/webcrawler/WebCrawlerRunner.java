package capital.scalable.webcrawler;

import capital.scalable.webcrawler.crawler.Crawler;
import capital.scalable.webcrawler.crawler.SEFactory;

public class WebCrawlerRunner {
    private static final SEFactory.SE DEFAULT_SE = SEFactory.SE.GOOGLE;

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No query supplied");
        }
        String query = args[0];
        new Crawler(DEFAULT_SE).crawl(query).stream().limit(5).forEach(System.out::println);
    }
}
