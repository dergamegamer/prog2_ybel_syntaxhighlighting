package highlighting.presets;

import highlighting.regex.Token;
import java.util.List;
import java.util.regex.Pattern;

public final class MiniJavaTokens {

  // TODO (Phase I+II: RegexHighlighter/ScanningHighlighter)
  // TODO: Define the MiniJava tokens used by the highlighters. Each token is a mapping from a
  // regular expression to a colour (and, if applicable, a specific matching group). The order of
  // tokens in this list determines their relative priority during highlighting. One example token
  // definition is provided below; define the remaining tokens in an analogous way.

  // Basic token set for MiniJava. Extend this list with further tokens as needed (e.g. identifiers,
  // numeric literals, operators, brackets, whitespace), following the same pattern. Each token is
  // defined by a regular expression and a colour. Optionally, a specific capturing group within the
  // pattern can be selected as the "highlighted" region.
  public static List<Token> defaultTokens() {
      return List.of(
          // Javadoc-Kommentare: /** ... */
          // Muss vor normalen Blockkommentaren stehen! (?s) lässt den Punkt auch Zeilenumbrüche matchen.
          Token.of(Pattern.compile("(?s)/\\*\\*.*?\\*/"), MiniJavaColours.JAVADOC_COMMENT_COLOUR),

          // Mehrzeilige Blockkommentare: /* ... */
          // (?s) lässt den Punkt auch Zeilenumbrüche matchen. .*? ist non-greedy, damit es beim ersten */ stoppt.
          Token.of(Pattern.compile("(?s)/\\*.*?\\*/"), MiniJavaColours.BLOCK_COMMENT_COLOUR),

          // Einzeilige Kommentare: // bis zum Ende der Zeile
          Token.of(Pattern.compile("//.*"), MiniJavaColours.LINE_COMMENT_COLOUR),

          // Strings: alles zwischen " und "
          // " [^"\\]* (escape seq oder normales zeichen)* " -> Stark vereinfacht, reicht laut Aufgabenstellung.
          Token.of(Pattern.compile("\"([^\"\\\\]|\\\\.)*\""), MiniJavaColours.STRING_LITERAL_COLOUR),

          // Characters: genau ein Zeichen zwischen ' und ' (inkl. einfache Escape-Sequenzen)
          Token.of(Pattern.compile("'([^'\\\\]|\\\\.)'"), MiniJavaColours.CHAR_LITERAL_COLOUR),

          // Annotationen: Beginnen mit @, gefolgt von Buchstaben/Zahlen/Unterstrich/Minus
          Token.of(Pattern.compile("@[A-Za-z0-9_-]+"), MiniJavaColours.ANNOTATION_COLOUR),

          // Keywords als ganze Wörter (\b sorgt für Wortgrenzen)
          Token.of(Pattern.compile("\\b(?:package|import|class|public|private|final|return|null|new)\\b"), MiniJavaColours.KEYWORD_COLOUR)
      );
  }
}
