import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WebScrape{
    //search query
    private static String search;
    private static String initialUrl = "https://www.google.com/search?client=firefox-b-d&q=" + search;
    private static String websiteURL;

    public static void getLink() throws IOException{
        Document doc = Jsoup.connect(initialUrl).get();

        Elements results = doc.select("div.g");

        for (Element result : results) {
            Element urlElement = result.selectFirst("a[href]");
            String link = urlElement.attr("href");

            //remove random google links from our search
            if (link.startsWith("/url?q=")) {
                link = link.substring(7);
                int endIndex = link.indexOf("&");
                if (endIndex != -1) {
                    link = link.substring(0, endIndex);
                }
            }
            websiteURL = link;
            break;
        }
    }

    public WebScrape(String search){
        this.search = search;
    }

    //main for testing purposes
    public static void main(String[] args) throws IOException {
        WebScrape c = new WebScrape("top news");
        WebScrape.getLink();
    }
}
