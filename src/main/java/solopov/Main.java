package solopov;

public class Main {

    public static void main(String[] args) throws Exception {
        HTMLDownloader htmlDownloader = new HTMLDownloader();
        htmlDownloader.getText("https://www.simbirsoft.com/");
        Html2TextWithRegExp html2TextWithRegExp = new Html2TextWithRegExp();
        html2TextWithRegExp.read();
    }
}
