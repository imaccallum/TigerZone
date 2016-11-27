package server;
import javafx.util.Pair;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProtocolMessageParser {

    public String parseWelcomePID(String input) {
        Pattern p = Pattern.compile("WELCOME .+ PLEASE WAIT FOR THE NEXT CHALLENGE");
        Matcher m = p.matcher(input);

        if (m.find()) {
            String pid = m.group(0);
            return pid;
        } else {
            return null;
        }
    }

    public String parseOpponentPID(String input) {
        Pattern p = Pattern.compile("YOUR OPPONENT IS PLAYER (.+)");
        Matcher m = p.matcher(input);

        if (m.find()) {
            String pid = m.group(1);
            return pid;
        } else {
            return null;
        }
    }

    public Pair<Integer, String[]> parseRemainingTiles(String input) {

        Pattern p = Pattern.compile("THE REMAINING (\\d+) TILES ARE (\\[.+\\])");
        Matcher m = p.matcher(input);

        if (m.find()) {
            Integer count = Integer.parseInt(m.group(1));
            String arr = m.group(2);
            String[] items = arr.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
            return new Pair(count, items);
        } else {
            return null;
        }




    }
}
