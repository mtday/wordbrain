package mday.wordbrain.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Solve the provided WORDBRAIN puzzle.
 */
public class Solver {
    private final Dictionary dictionary;
    private final LetterGrid letterGrid;
    private final List<Integer> wordLengths;

    private final SortedSet<Word> allWords;

    /**
     * @param dictionary the dictionary containing all of the known words
     * @param letterGrid the letter grid representing the puzzle to solve
     * @param wordLengths the length of the words to find in the letter grid
     */
    public Solver(final Dictionary dictionary, final LetterGrid letterGrid, final List<Integer> wordLengths) {
        this.dictionary = Objects.requireNonNull(dictionary);
        this.letterGrid = Objects.requireNonNull(letterGrid);
        this.wordLengths = Objects.requireNonNull(wordLengths);
        this.allWords = new TreeSet<>(new Word.SimpleComparator());
    }

    /**
     * @return the identified words, empty if no solution was found
     */
    public SortedSet<Solution> solve() {
        final List<Solution> solutions = solve(Optional.empty(), this.letterGrid, new ArrayList<>(this.wordLengths));
        return new TreeSet<>(solutions.stream().filter(solution -> solution.getSize() == this.wordLengths.size())
                .collect(Collectors.toList()));
    }

    /**
     * @return all of the words seen in the letter grid, whether they fit into a solution or not
     */
    public SortedSet<Word> getAllWords() {
        return Collections.unmodifiableSortedSet(this.allWords);
    }

    private List<Solution> solve(
            final Optional<Solution> solution, final LetterGrid grid, final List<Integer> wordLengths) {
        if (wordLengths.isEmpty()) {
            return (solution.isPresent()) ? Arrays.asList(solution.get()) : Collections.emptyList();
        }

        final int wordLength = wordLengths.remove(0);
        final List<Word> words = new LinkedList<>();
        findWords(this.dictionary, grid, words, wordLength, new ArrayList<>());
        this.allWords.addAll(words);

        final List<Solution> solutions = new ArrayList<>();
        for (final Word word : words) {
            final Solution.Builder builder = new Solution.Builder();
            if (solution.isPresent()) {
                builder.add(solution.get().getWords());
            }
            final Solution newSolution = builder.add(word).build();

            final LetterGrid newGrid = new LetterGrid.Builder(grid).clear(word).applyGravity().build();
            final List<Solution> s = solve(Optional.of(newSolution), newGrid, new ArrayList<>(wordLengths));
            solutions.addAll(s);
        }
        return solutions;
    }

    private void findWords(
            final Dictionary dict, final LetterGrid grid, final List<Word> words, final int wordLength,
            final List<Letter> letters) {
        if (letters.size() == wordLength) {
            final Word word = new Word.Builder(letters).build();
            if (dict.exists(word)) {
                words.add(word);
            }
            return;
        }

        if (letters.isEmpty()) {
            for (final Letter letter : grid.getLetters()) {
                final LetterGrid newGrid = new LetterGrid.Builder(grid).clear(letter).build();
                final List<Letter> newLetters = Arrays.asList(letter);
                findWords(dict, newGrid, words, wordLength, newLetters);
            }
        } else {
            final Word word = new Word.Builder(letters).build();
            if (!dict.isPrefix(word)) {
                // The word so far does not exist in the dictionary, no need to continue down this path.
            } else {
                final Letter letter = letters.get(letters.size() - 1);

                final List<Letter> adjacent = grid.getAdjacent(letter);
                for (final Letter adj : adjacent) {
                    final List<Letter> combined = new ArrayList<>(letters.size() + 1);
                    combined.addAll(letters);
                    combined.add(adj);

                    final LetterGrid newGrid = new LetterGrid.Builder(grid).clear(adj).build();
                    findWords(dict, newGrid, words, wordLength, combined);
                }
            }
        }
    }
}
