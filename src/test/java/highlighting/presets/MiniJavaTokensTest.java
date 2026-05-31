package highlighting.presets;

import static org.junit.jupiter.api.Assertions.*;

import highlighting.core.HighlightRegion;
import highlighting.regex.Token;
import java.util.List;
import org.junit.jupiter.api.Test;

class MiniJavaTokensTest {

  private final List<Token> tokens = MiniJavaTokens.defaultTokens();

  @Test
  void testKeywords() {
    // Sucht das Keyword-Token (das letzte in der Liste)
    Token keywordToken = tokens.get(6);

    // Normaler Treffer
    List<HighlightRegion> regions = keywordToken.test("public class Main");
    assertEquals(2, regions.size(), "Sollte 'public' und 'class' finden");

    // Darf nicht als Teil eines anderen Wortes matchen (Grenzfall)
    List<HighlightRegion> regions2 = keywordToken.test("myclass is not a keyword");
    assertTrue(regions2.isEmpty(), "'class' innerhalb eines Wortes darf nicht matchen");
  }

  @Test
  void testAnnotations() {
    Token annotationToken = tokens.get(5);

    List<HighlightRegion> regions = annotationToken.test("@Override public void");
    assertEquals(1, regions.size(), "Sollte die Annotation finden");
    assertEquals(0, regions.get(0).start(), "Sollte am Anfang starten");
    assertEquals(9, regions.get(0).end(), "Sollte bis zum Ende von @Override gehen");
  }

  @Test
  void testComments() {
    Token lineCommentToken = tokens.get(2);

    // Einzeiliger Kommentar am Ende des Textes
    List<HighlightRegion> regions = lineCommentToken.test("int x = 5; // ein kommentar");
    assertEquals(1, regions.size());
    assertEquals(11, regions.get(0).start());

    // Javadoc vs Blockkommentar
    Token javadocToken = tokens.get(0);
    Token blockToken = tokens.get(1);

    String javadocText = "/** Mein Javadoc */";
    assertEquals(1, javadocToken.test(javadocText).size(), "Javadoc-Token sollte matchen");

    // Das Javadoc-Token steht in der Liste vor dem Blockkommentar-Token

  }

  @Test
  void testStrings() {
    Token stringToken = tokens.get(3);

    // String mit // im Inhalt (darf nicht als Kommentar gewertet werden vom Regex her)
    List<HighlightRegion> regions = stringToken.test("String s = \"Hallo // Welt\";");
    assertEquals(1, regions.size(), "Sollte den kompletten String finden");
    assertEquals(11, regions.get(0).start());
    assertEquals(26, regions.get(0).end());
  }
}
