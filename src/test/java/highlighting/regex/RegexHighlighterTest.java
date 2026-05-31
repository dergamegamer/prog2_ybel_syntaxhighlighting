package highlighting.regex;

import static org.junit.jupiter.api.Assertions.*;

import highlighting.core.HighlightRegion;
import highlighting.presets.MiniJavaColours;
import java.util.List;
import org.junit.jupiter.api.Test;

class RegexHighlighterTest {

  private final RegexHighlighter highlighter = new RegexHighlighter();

  @Test
  void testEmptyOrNoMatch() {
    List<HighlightRegion> regions = highlighter.computeRegions("");
    assertTrue(regions.isEmpty(), "Leerer Text sollte keine Regionen liefern");

    List<HighlightRegion> regions2 = highlighter.computeRegions("normaler text ohne keywords");
    assertTrue(regions2.isEmpty(), "Text ohne Matches sollte keine Regionen liefern");
  }

  @Test
  void testSimpleSequentialRegions() {

    List<HighlightRegion> regions = highlighter.computeRegions("public class");
    assertEquals(2, regions.size());
    assertEquals(0, regions.get(0).start());
    assertEquals(6, regions.get(0).end());
    assertEquals(7, regions.get(1).start());
    assertEquals(12, regions.get(1).end());
  }

  @Test
  void testOverlapKeywordInComment() {
    // Ein Kommentar, der ein Keyword enthält.
    // Das Keyword "class" überlappt mit dem Kommentar, der Kommentar startet früher
    List<HighlightRegion> regions = highlighter.computeRegions("// das ist eine class");
    assertEquals(1, regions.size(), "Nur der Kommentar darf übrig bleiben");
    assertEquals(MiniJavaColours.LINE_COMMENT_COLOUR, regions.get(0).colour());
  }

  @Test
  void testJavadocVsBlockComment() {
    // "/** ... */" matcht sowohl auf Javadoc als auch auf Blockkommentar.
    // Da Javadoc in MiniJavaTokens-Liste weiter oben steht, gewinnt es bei gleicher Startposition.
    List<HighlightRegion> regions = highlighter.computeRegions("/** Javadoc */");
    assertEquals(1, regions.size());
    assertEquals(
        MiniJavaColours.JAVADOC_COMMENT_COLOUR,
        regions.get(0).colour(),
        "Javadoc muss Vorrang haben");
  }
}
