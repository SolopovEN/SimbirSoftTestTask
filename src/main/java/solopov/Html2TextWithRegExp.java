package solopov;


import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Html2TextWithRegExp {

    private final String[] array = {makeRegexWholeTag("script"), makeRegexWholeTag("noscript"),
    makeRegexWholeTag("title"), "<!--.*?-->", "<(.|\n)*?>", "[\"]",
            "/", "[?]", "[,]", ":", "!", "\\.", "[(]", "[)]", "В»", "В«", "\\t", "\\r",
            "\\n", "\\u00A9", "&(.*?);", ";", "\u200C", "\\s+"};

    public void read() throws Exception {
        String body = getBodyFromHtml(readFromFile("doc.html"));
        body = replaceSubStrings(body, array, " ");

        String[] newArray = body.trim().toUpperCase().split("");

        Map<String, Long> map = countByStreamToMap(new ArrayList<>(Arrays.asList(newArray)));

        writeResultToFile(sortMap(map));
    }

    private <T> Map<T, Long> countByStreamToMap(List<T> inputList) {
        return inputList.stream().collect(Collectors.toMap(Function.identity(), v -> 1L, Long::sum));
    }

    private <T> Map<T, Long> sortMap(Map <T, Long> map) {
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private String makeRegexInsideTag(String tagName) {
        return String.format(".*?<%s.*?>(.*?)</%s>.*?", tagName, tagName);
    }

    private String makeRegexWholeTag(String tagName) {
        return String.format("<%s.*?>(.*?)</%s>", tagName, tagName);
    }

    private String replaceSubStrings(String string, String[] args, String replacement) {
        for (String pattern : args) {
            string = string.replaceAll(pattern, replacement);
        }
        return string;
    }

    private String getBodyFromHtml(String html) throws Exception {
        Matcher matcher = getMatcher(makeRegexInsideTag("body"), html);
        if (matcher.find())
            return matcher.group(1);
        else
            throw new Exception();
    }

    private Matcher getMatcher(String regex, String html) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(html);
    }

    private String readFromFile(String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    private <T> void writeResultToFile(Map<T, Long> map) {
        File file = new File("result.txt");

        try (BufferedWriter bf = new BufferedWriter(new FileWriter(file))) {

            for (Map.Entry<T, Long> entry : map.entrySet()) {

                bf.write(entry.getKey() + " = " + entry.getValue());

                bf.newLine();
            }
            bf.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
