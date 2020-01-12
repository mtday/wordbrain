package mday.wordbrain.model;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single solution to the WORDBRAIN puzzle.
 */
public class Solution implements Comparable<Solution> {
    private final List<Word> words;

    /**
     * @param words the words that make up the solution
     */
    private Solution(final List<Word> words) {
        this.words = new ArrayList<>(words);
    }

    /**
     * @return the words that make up the solution
     */
    public List<Word> getWords() {
        return Collections.unmodifiableList(this.words);
    }

    /**
     * @return the number of words in this solution
     */
    public int getSize() {
        return this.words.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final List<String> strings = new ArrayList<>();
        strings.add("Solution:");
        getWords().forEach(word -> strings.add(word.toString()));
        return StringUtils.join(strings, "  ");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final Solution other) {
        if (other == null) {
            return 1;
        }

        return toString().compareTo(other.toString());
    }

    /**
     * Used to build solutions
     */
    public static class Builder {
        private final List<Word> words = new ArrayList<>();

        /**
         * Default constructor.
         */
        public Builder() {
        }

        /**
         * @param other the solution to duplicate
         */
        public Builder(final Solution other) {
            Objects.requireNonNull(other);
            add(other.getWords());
        }

        /**
         * @param word the word to add
         * @return {@code this} for fluent-style usage
         */
        public Builder add(final Word word) {
            this.words.add(word);
            return this;
        }

        /**
         * @param words the words to add
         * @return {@code this} for fluent-style usage
         */
        public Builder add(final Collection<Word> words) {
            this.words.addAll(words);
            return this;
        }

        /**
         * @return the created solution
         */
        public Solution build() {
            if (this.words.isEmpty()) {
                throw new IllegalStateException("No words provided");
            }

            return new Solution(this.words);
        }
    }
}
