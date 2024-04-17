import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WebScrape{
    //search query
    private static String search;
    private static String initialUrl;
    private static String websiteURL;

    public WebScrape(String search){
        this.search = search;
        this.initialUrl = "https://www.google.com/search?client=firefox-b-d&q=" + search;
    }

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

    public static String scrapeTitleAndSummary(String url) {
        try {
            Document doc = Jsoup.connect(url).timeout(6000).get();
            Element titleElement = doc.head().select("title").first();
            String title = titleElement.text();
            System.out.println("Title: " + title);


            Element body = doc.body();
            Element firstParagraph = body.select("p").first();
            if (firstParagraph != null) {
                String summary = firstParagraph.text();
                System.out.println("Summary: " + summary);
                return summary;
            }
            else {
                System.out.println("No summary found.");
                return null;
            }


        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    //main for testing purposes
    public static void main(String[] args) throws IOException {
        try {
            WebScrape c = new WebScrape("top news");
            WebScrape.getLink();
            // Pass the websiteURL to the scrapeTitleAndSummary method
            scrapeTitleAndSummary(websiteURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
