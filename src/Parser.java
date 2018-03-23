import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static Pattern patternInfoLog = Pattern.compile("((([0-2]?[1-9])|([3][0,1])|10|20|30)-" +
            "(Jan|Feb|Mar|Apr|May|June|July|Aug|Sept|Oct|Nov|Dec)-" +
            "([0-9]{4}))\\s" +
            "(([0-1][0-9]|[2][0-4]):" +
            "([0-5][0-9]|60):" +
            "([0-5][0-9]|60)[.]" +
            "([0-9][0-9][0-9]))\\s" +
            "(INFO)\\s" +
            "(\\W\\w*\\W)\\s" +
            "((\\S?[a-zA-Z]+\\S?)+)");
    private static Pattern patternErrorLog = Pattern.compile("([0-9]*)\\s" +
            "(\\W[a-zA-Z-0-9]*\\W)\\s" +
            "(ERROR)\\s" +
            "((\\S?[a-zA-Z]+\\S?)+)\\s-\\s(.+)?" +
            "((\\n+.+)+(omitted))");
    private static Pattern patternWarningLog = Pattern.compile("([0-9]*)\\s\\[(.+)\\]\\s(WARN)\\s+((\\.?[a-zA-Z]+)*)\\s-\\s(.+)" +
            "-\\s(.+)");
    private static ArrayList<Pattern> arrayOfPattern = new ArrayList<>(Arrays.asList(
                    Pattern.compile("^(Jan|Feb|Mar|Apr|May|June|July|Aug|Sept|Oct|Nov|Dec)\\s" +
            "(([0-2]?[1-9])|([3][0,1])|10|20|30),\\s" +
            "([0-9]{4})\\" +
            "s([1]?[0-9]:[0-6][0-9]:[0-6][0-9]\\s" +
            "PM|AM)"),
                    Pattern.compile("^(Mon|Tue|Wed|Thu|Fri|Sat|Sun)\\s" +
            "(Jan|Feb|Mar|Apr|May|June|July|Aug|Sept|Oct|Nov|Dec)\\s" +
            "(([0-2]?[1-9])|([3][0,1])|10|20|30)\\s" +
            "(([0-1][0-9]|[2][0-4]):([0-6][0-9]):([0-6][0-9]))\\s" +
            "(ACST|AEST|AKT|ART|AST|AWST|BDT|BTT|CAT|CET|СST|CXT|ChT" +
            "|EAT|EET|EST|FET|GALT|GMT|HAST|HKT|IRST|IST|JST|MT|" +
            "MSK|MST|NFT|NST|PET|PHT|PKT|PMST|PST|SLT|SST|ST|THA|UTC|WAT|WET)\\s" +
            "[0-9]{4}"),
                    Pattern.compile("^((([0-2]?[1-9])|([3][0,1])|10|20|30)-" +
            "(Jan|Feb|Mar|Apr|May|June|July|Aug|Sept|Oct|Nov|Dec)-" +
            "([0-9]{4}))\\s" +
            "(([0-1][0-9]|[2][0-4]):" +
            "([0-5][0-9]|60):" +
            "([0-5][0-9]|60)[.]" +
            "([0-9][0-9][0-9]))"),
                    Pattern.compile("([0-9]*)\\s(\\[.+\\])\\s" +
                            "(WARN)")));
    private static ArrayList<InfoLog> infoLogs = new ArrayList<>();
    private static ArrayList<ErrorLog> errorLogs = new ArrayList<>();
    private static ArrayList<WarningLog> warningLogs = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ArrayList<String> data = getData();
        getWARNING(data);
        System.out.println(warningLogs);
    }


    private static void getError(ArrayList<String> data){
        for (String error : data) {
            Matcher match = patternErrorLog.matcher(error);
            if (match.find()) {
                errorLogs.add(new ErrorLog(match.group(1),
                        match.group(2),
                        match.group(3),
                        match.group(4),
                        match.group(6) == null ? "No data about error" : match.group(6),
                        match.group(7) == null ? "No data about error" : match.group(7)
                ));
            }
        }
    }

    private static void getINFO(ArrayList<String> data) {
        for (String error : data) {
            Matcher match = patternInfoLog.matcher(error);
            if (match.find()) {
                infoLogs.add(new InfoLog(match.group(1),
                        match.group(7),
                        match.group(12),
                        "Some Description",
                        match.group(13),
                        match.group(14)
                ));

            }
        }
    }

    private static void getWARNING(ArrayList<String> data ) {
        for (String error : data) {
            Matcher match = patternWarningLog.matcher(error);
            if (match.matches()) {
                warningLogs.add(new WarningLog(match.group(1),
                        match.group(3),
                        match.group(2),
                        match.group(4),
                        match.group(6)
                ));
            }
        }
    }

    private static ArrayList<String> getData() throws IOException {
        ArrayList<String> data = new ArrayList<>();
        FileReader file = new FileReader("src//catalina.out");
        BufferedReader buffer = new BufferedReader(file);
        String line;
        String bufferLine = buffer.readLine();
        while ((line = buffer.readLine()) != null) {
            for (Pattern pattern : arrayOfPattern) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    data.add(bufferLine);
                    bufferLine = "";
                    break;
                }
            }
            bufferLine += line;
        }
        buffer.close();
        return data;
    }


}
