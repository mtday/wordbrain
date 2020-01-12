package mday.wordbrain.model;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Represents a collection of letters.
 */
public class Word implements Comparable<Word> {
    private final List<Letter> letters;

    /**
     * @param letters the letters contained in this word
     */
    private Word(final Collection<Letter> letters) {
        this.letters = new ArrayList<>(letters);
    }

    /**
     * @return the letters contained in this word
     */
    public List<Letter> getLetters() {
        return Collections.unmodifiableList(this.letters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        for (final Letter letter : getLetters()) {
            str.append(Character.toUpperCase(letter.getChar()));
        }
        return str.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final Word other) {
        if (other == null) {
            return 1;
        }

        final Iterator<Letter> iterA = getLetters().iterator();
        final Iterator<Letter> iterB = other.getLetters().iterator();

        final CompareToBuilder cmp = new CompareToBuilder();
        while (iterA.hasNext() && iterB.hasNext()) {
            cmp.append(iterA.next(), iterB.next());
        }
        if (iterA.hasNext()) {
            cmp.append(iterA.next(), (char) 255);
        }
        if (iterB.hasNext()) {
            cmp.append((char) 255, iterB.next());
        }
        return cmp.toComparison();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object other) {
        return other instanceof Word && compareTo((Word) other) == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final HashCodeBuilder hash = new HashCodeBuilder();
        getLetters().forEach(l -> hash.append(l));
        return hash.toHashCode();
    }

    /**
     * Do simple string comparisons for words.
     */
    public static class SimpleComparator implements Comparator<Word> {
        /**
         * {@inheritDoc}
         */
        @Override
        public int compare(final Word a, final Word b) {
            final String sa = (a == null) ? "" : a.toString();
            final String sb = (b == null) ? "" : b.toString();
            return sa.compareTo(sb);
        }
    }

    /**
     * Used to build word objects.
     */
    public static class Builder {
        private List<Letter> letters = new ArrayList<>();

        /**
         * Default constructor.
         */
        public Builder() {
        }

        /**
         * @param other the word to copy
         */
        public Builder(final Word other) {
            add(Objects.requireNonNull(other).getLetters());
        }

        /**
         * @param letters the letters to include in the word
         */
        public Builder(final Collection<Letter> letters) {
            add(letters);
        }

        /**
         * @param letters the letters to include in the word
         * @return {@code this} for fluent-style usage
         */
        public Builder add(final Collection<Letter> letters) {
            this.letters.addAll(Objects.requireNonNull(letters));
            return this;
        }

        /**
         * @return the created word
         */
        public Word build() {
            if (this.letters.isEmpty()) {
                throw new IllegalStateException("Unable to build without letters");
            }

            return new Word(this.letters);
        }
    }
}
