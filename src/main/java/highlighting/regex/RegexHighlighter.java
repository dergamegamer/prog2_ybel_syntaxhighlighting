package highlighting.regex;

import highlighting.core.HighlightRegion;
import highlighting.core.SyntaxHighlighter;
import highlighting.presets.MiniJavaTokens;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RegexHighlighter extends SyntaxHighlighter {

  @Override
  public List<HighlightRegion> collectMatches(String text) {

    return MiniJavaTokens.defaultTokens().stream()
        .flatMap(token -> token.test(text).stream())
        .collect(Collectors.toList());
  }

  @Override
  public List<HighlightRegion> resolveConflicts(List<HighlightRegion> regions) {
    List<HighlightRegion> resolved = new ArrayList<>();

    for (HighlightRegion r : regions) {
      boolean overlaps = false;

      for (HighlightRegion s : resolved) {
        // Logik für Überlappung bei Intervallen: max(start) < min(end)
        int maxStart = Math.max(r.start(), s.start());
        int minEnd = Math.min(r.end(), s.end());

        if (maxStart < minEnd) {
          overlaps = true;
          break;
        }
      }

      if (!overlaps) {
        resolved.add(r);
      }
    }

    return resolved;
  }
}
