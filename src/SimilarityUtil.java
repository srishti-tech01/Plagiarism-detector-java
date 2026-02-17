import java.util.*;

public class SimilarityUtil {

    public static String clean(String s) {
        return s.replaceAll("//.*", "")
                .replaceAll("\\s+", " ")
                .toLowerCase();
    }

    public static List<String> tokenize(String s) {
        return Arrays.asList(s.split("\\W+"));
    }

    public static Map<String,Integer> freqMap(List<String> words) {
        Map<String,Integer> map = new HashMap<>();
        for (String w: words) {
            if (!w.isEmpty())
                map.put(w, map.getOrDefault(w,0)+1);
        }
        return map;
    }

    public static double similarity(Map<String,Integer> a, Map<String,Integer> b) {
        int match=0, ta=0, tb=0;
        for (int v:a.values()) ta+=v;
        for (int v:b.values()) tb+=v;

        for (String k:a.keySet())
            if (b.containsKey(k))
                match += Math.min(a.get(k), b.get(k));

         return (2.0*match/(ta+tb))*100;
    }
}
