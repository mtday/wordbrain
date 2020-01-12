package mday.wordbrain.runner;

import mday.wordbrain.model.Dictionary;
import mday.wordbrain.model.LetterGrid;
import mday.wordbrain.model.Solution;
import mday.wordbrain.model.Solver;
import mday.wordbrain.model.Word;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

/**
 *
 */
public class Run {
    /**
     * @param args the command line parameters
     */
    public static void main(final String... args) throws IOException {
        final Dictionary dictionary = new Dictionary();
        dictionary.load();

        final LetterGrid letterGrid =
                new LetterGrid.Builder(5).set("ALABH", "LFLLO", "ERMSU", "BNLAS", "UMAEE").build();
        final List<Integer> wordLengths = Arrays.asList(8, 3, 5, 4, 5);

        final Solver solver = new Solver(dictionary, letterGrid, wordLengths);
        final Set<Solution> solutions = solver.solve();
        System.out.println("Solutions: " + solutions.size());
        solutions.forEach(System.out::println);

        if (solutions.size() < 10) {
            final SortedSet<Word> allWords = solver.getAllWords();
            System.out.println("All words found: " + allWords.size());
            allWords.forEach(word -> System.out.println("  " + word));
        }
    }
}
