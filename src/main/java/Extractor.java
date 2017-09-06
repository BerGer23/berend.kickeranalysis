
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Extractor {

    public static void main(final String[] args) {
        final List<Player> tor = parseHtml("tor");
        final List<Player> abwehr = parseHtml("abwehr");
        final List<Player> mittelfeld = parseHtml("mittelfeld");
        final List<Player> sturm = parseHtml("sturm");

        TeamCreator.createTeamStefan(tor, abwehr, mittelfeld, sturm);
        TeamCreator.createTeam(tor, abwehr, mittelfeld, sturm, 1, 3, 4, 3);
    }

    private static List<Player> parseHtml(final String filename) {
        Document doc = null;
        try {
            doc = Jsoup.parse(readFile("html/" + filename + ".htm", Charset.defaultCharset()));
        } catch (final IOException e) {
            e.printStackTrace();
        }
        final Element playerTable = doc.select("#ovContent > div.tborder-b-s > div > div > div > table > tbody").first();
        final List<Player> playerList = new ArrayList<>();
        for (final Element child : playerTable.children()) {
            if ("alt".equals(child.className())
                    || (!child.outerHtml().contains("headlink") && child.className().isEmpty())) {
                playerList.add(createPlayer(child));
            }
        }
        return playerList;
    }

    private static Player createPlayer(final Element child) {
        try {
            final String name = ((TextNode) child.childNodes().get(3).childNode(0).childNode(0)).text();
            final String pos = ((TextNode) child.childNodes().get(7).childNode(0)).text();
            final int pointsThis = (child.childNodes().get(11).childNodeSize() > 0) ? Integer.valueOf(((TextNode) child.childNodes().get(11).childNode(0)).text()) : 0;
            final float price = Float.valueOf(((TextNode) child.childNodes().get(9).childNode(0)).text().replace(',', '.'));
            final int pointsLast = (child.childNodes().get(15).childNodeSize() > 0) ? Integer.valueOf(((TextNode) child.childNodes().get(15).childNode(0)).text()) : 0;
            return new Player(name, pos, pointsThis, price, pointsLast);
        } catch (final Exception e) {
            final int a = 5;
        }
        return null;
    }

    private static String readFile(final String path, final Charset encoding)
            throws IOException {
        final byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
