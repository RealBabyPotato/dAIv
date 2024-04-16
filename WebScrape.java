import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebScrape{
    // Response from ChatGPT
    private static String keywordsString;
    // Extract keywords from response
//    private static String[] keywords = keywordsString.split(",");
    static String[] keywords = {"mallard" , "duck"};
    private static String initialUrl = "https://www.google.com/search?client=firefox-b-d&q=";

    public static String setURL(){
        for(int j = 0; j<keywords.length-1; j++){
            initialUrl+=keywords[j];
            initialUrl+="+";
        }
        initialUrl+=keywords[keywords.length-1];
        return initialUrl;
    }

    public static String getLink(){
        Document doc = (Document) Jsoup.connect(initialUrl);
        return ""; // whoever is working on this method make sure it runs (i.e. semicolon on line above and make sure this method actually returns a string so that we don't get any errors when running Main.java
    }

    public WebScrape(String keywordsString){
        this.keywordsString = keywordsString;
    }

    //main for testing purposes
    public static void main(String[] args){
        WebScrape c = new WebScrape("mallard,duck");
        System.out.println(WebScrape.setURL());
    }
}
