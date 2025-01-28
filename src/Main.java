import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();

        Map<Integer, Set<String>> words = loadAllWords();
        Map<Integer, Set<String>> mapFilteredWords = new HashMap<>();

        mapFilteredWords.computeIfAbsent(1, k -> new HashSet<>()).add("I");
        mapFilteredWords.computeIfAbsent(1, k -> new HashSet<>()).add("A");

        int filteredWordsCount = 2;//it starts from 2 because of "I" and "A"
        for (int n = 2; n <= 9; n++) {
            Set<String> filteredWords = filterWordsRecursively(words, n);
            filteredWordsCount += filteredWords.size();
            //mapFilteredWords.computeIfAbsent(n, k -> new HashSet<>()).addAll(filteredWords);
        }

        long endTime = System.currentTimeMillis();
        double elapsedTimeInSeconds = (endTime - startTime) / 1000.0;

        System.out.println("Time: " + elapsedTimeInSeconds + "; Number of words: " + filteredWordsCount);
    }

    private static Map<Integer, Set<String>> loadAllWords() throws IOException {
        URL wordsUrl = new URL("https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(wordsUrl.openStream()))) {
            return br.lines()
                    .skip(2)
                    .filter(word -> word.length() <= 9)
                    .filter(word -> word.contains("A") || word.contains("I"))
                    .collect(Collectors.groupingBy(String::length,
                            Collectors.toSet()));
        }
    }
    public static Set<String> filterWordsRecursively(Map<Integer, Set<String>> wordsByLength, int n) {
        // Base case: If n is 2, return the list of words of size 2 (no filtering needed)
        if (n == 2) {
            return wordsByLength.getOrDefault(2, Collections.emptySet());
        }

        Set<String> smallerWords = new HashSet<>(filterWordsRecursively(wordsByLength, n - 1));
        Set<String> currentWords = wordsByLength.getOrDefault(n, Collections.emptySet());

        return getStrings(n, currentWords, smallerWords);
    }

    private static Set<String> getStrings(int n, Set<String> currentWords, Set<String> smallerWords) {
        Set<String> filteredWords = new HashSet<>();

        for(var currentWord : currentWords){
            for(int i = 0; i < currentWord.length(); i++){
                String newWord = new StringBuilder(currentWord).deleteCharAt(i).toString();
                if(smallerWords.contains(newWord)){
                    filteredWords.add(currentWord);
                    break;
                }
            }
        }

        return filteredWords;
    }
}