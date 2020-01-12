package mday.wordbrain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 */
public class LetterGrid {
    private final int size;
    private final Letter[][] letters;

    /**
     * @param size the size of the grid
     */
    private LetterGrid(final int size, final Letter[][] letters) {
        this.size = size;
        this.letters = letters;
    }

    /**
     * @return the size of the letter grid
     */
    public int getSize() {
        return this.size;
    }

    /**
     * @param row the row for which the character should be retrieved
     * @param col the column for which the character should be retrieved
     *
     * @return the requested character
     */
    public Letter get(final int row, final int col) {
        if (row >= getSize() || col >= getSize()) {
            throw new IllegalArgumentException(
                    String.format("Out of grid bounds: %d, %d (max: %d)", row, col, getSize() - 1));
        }

        return this.letters[row][col];
    }

    /**
     * @param letter the letter indicating the starting position
     * @return the letter west of the provided letter, possibly empty
     */
    public Optional<Letter> getWest(final Letter letter) {
        if (letter.getCol() == 0) {
            return Optional.empty();
        }

        return Optional.of(get(letter.getRow(), letter.getCol() - 1));
    }

    /**
     * @param letter the letter indicating the starting position
     * @return the letter west of the provided letter, possibly empty
     */
    public Optional<Letter> getNorthWest(final Letter letter) {
        if (letter.getRow() == 0 || letter.getCol() == 0) {
            return Optional.empty();
        }

        return Optional.of(get(letter.getRow() - 1, letter.getCol() - 1));
    }

    /**
     * @param letter the letter indicating the starting position
     * @return the letter west of the provided letter, possibly empty
     */
    public Optional<Letter> getNorth(final Letter letter) {
        if (letter.getRow() == 0) {
            return Optional.empty();
        }

        return Optional.of(get(letter.getRow() - 1, letter.getCol()));
    }

    /**
     * @param letter the letter indicating the starting position
     * @return the letter west of the provided letter, possibly empty
     */
    public Optional<Letter> getNorthEast(final Letter letter) {
        if (letter.getRow() == 0 || letter.getCol() == getSize() - 1) {
            return Optional.empty();
        }

        return Optional.of(get(letter.getRow() - 1, letter.getCol() + 1));
    }

    /**
     * @param letter the letter indicating the starting position
     * @return the letter west of the provided letter, possibly empty
     */
    public Optional<Letter> getEast(final Letter letter) {
        if (letter.getCol() == getSize() - 1) {
            return Optional.empty();
        }

        return Optional.of(get(letter.getRow(), letter.getCol() + 1));
    }

    /**
     * @param letter the letter indicating the starting position
     * @return the letter west of the provided letter, possibly empty
     */
    public Optional<Letter> getSouthEast(final Letter letter) {
        if (letter.getRow() == getSize() - 1 || letter.getCol() == getSize() - 1) {
            return Optional.empty();
        }

        return Optional.of(get(letter.getRow() + 1, letter.getCol() + 1));
    }

    /**
     * @param letter the letter indicating the starting position
     * @return the letter west of the provided letter, possibly empty
     */
    public Optional<Letter> getSouth(final Letter letter) {
        if (letter.getRow() == getSize() - 1) {
            return Optional.empty();
        }

        return Optional.of(get(letter.getRow() + 1, letter.getCol()));
    }

    /**
     * @param letter the letter indicating the starting position
     * @return the letter west of the provided letter, possibly empty
     */
    public Optional<Letter> getSouthWest(final Letter letter) {
        if (letter.getRow() == getSize() - 1 || letter.getCol() == 0) {
            return Optional.empty();
        }

        return Optional.of(get(letter.getRow() + 1, letter.getCol() - 1));
    }

    /**
     * @param letter the letter indicating the starting position
     * @return the adjacent non-empty letters
     */
    public List<Letter> getAdjacent(final Letter letter) {
        final List<Optional<Letter>> adjacent = new ArrayList<>(8);
        adjacent.add(getWest(letter));
        adjacent.add(getNorthWest(letter));
        adjacent.add(getNorth(letter));
        adjacent.add(getNorthEast(letter));
        adjacent.add(getEast(letter));
        adjacent.add(getSouthEast(letter));
        adjacent.add(getSouth(letter));
        adjacent.add(getSouthWest(letter));
        return adjacent.stream().filter(l -> l.isPresent() && !l.get().isEmpty()).map(l -> l.get())
                .collect(Collectors.toList());
    }

    /**
     * @return the non-empty letters in this grid
     */
    public List<Letter> getLetters() {
        final List<Letter> list = new ArrayList<>(getSize() * getSize());
        for (int r = 0; r < getSize(); r++) {
            for (int c = 0; c < getSize(); c++) {
                final Letter l = get(r, c);
                if (!l.isEmpty()) {
                    list.add(l);
                }
            }
        }
        return list;
    }

    /**
     * @return the separator string value for the {@code toString} implementation
     */
    private String getToStringSeparator() {
        final StringBuilder str = new StringBuilder();
        str.append("+");
        for (int s = 0; s < getSize(); s++) {
            str.append("---+");
        }
        str.append(System.lineSeparator());
        return str.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append(getToStringSeparator());
        for (int r = 0; r < getSize(); r++) {
            for (int c = 0; c < getSize(); c++) {
                str.append("| ");
                str.append(get(r, c));
                str.append(" ");
            }
            str.append("|");
            str.append(System.lineSeparator());
            str.append(getToStringSeparator());
        }
        return str.toString();
    }

    /**
     * Used to build letter grids.
     */
    public static class Builder {
        private final int size;
        private final Letter[][] letters;

        /**
         * @param other the letter grid to copy
         */
        public Builder(final LetterGrid other) {
            this.size = Objects.requireNonNull(other).getSize();
            this.letters = new Letter[this.size][this.size];

            for (int r = 0; r < this.size; r++) {
                for (int c = 0; c < this.size; c++) {
                    this.letters[r][c] = other.get(r, c);
                }
            }
        }

        /**
         * @param size the size of the grid
         */
        public Builder(final int size) {
            if (size < 0 || size > 8) {
                throw new IllegalArgumentException("Invalid size: " + size);
            }

            this.size = size;
            this.letters = new Letter[this.size][this.size];
            clear();
        }

        /**
         * @param row the row for which the character should be set
         * @param col the column for which the character should be set
         * @param c the new character value
         * @return {@code this} for fluent-style usage
         */
        public Builder set(final int row, final int col, final char c) {
            if (row >= this.size || col >= this.size) {
                throw new IllegalArgumentException(
                        String.format("Out of grid bounds: %d, %d (max: %d)", row, col, this.size - 1));
            }

            this.letters[row][col] = new Letter.Builder(row, col, c).build();
            return this;
        }

        /**
         * @param row the row for which the character values should be set
         * @param line the new character values for the row
         * @return {@code this} for fluent-style usage
         */
        public Builder setRow(final int row, final String line) {
            if (row >= this.size) {
                throw new IllegalArgumentException(
                        String.format("Out of grid bounds: %d (max: %d)", row, this.size - 1));
            }
            if (Objects.requireNonNull(line).length() != this.size) {
                throw new IllegalArgumentException(String.format("Expected line to be length %d", this.size));
            }

            final char[] chars = line.toCharArray();
            for (int c = 0; c < this.size; c++) {
                set(row, c, chars[c]);
            }
            return this;
        }

        /**
         * @param rows the lines of characters to set in this grid
         * @return {@code this} for fluent-style usage
         */
        public Builder set(final String... rows) {
            if (Objects.requireNonNull(rows).length != this.size) {
                throw new IllegalArgumentException(String.format("Expected rows to be length %d", this.size));
            }
            for (int r = 0; r < rows.length; r++) {
                setRow(r, rows[r]);
            }
            return this;
        }

        /**
         * Clear all the characters to the empty value.
         *
         * @return {@code this} for fluent-style usage
         */
        public Builder clear() {
            for (int r = 0; r < this.size; r++) {
                for (int c = 0; c < this.size; c++) {
                    clear(r, c);
                }
            }
            return this;
        }

        /**
         * @param word the word to be cleared from this grid
         * @return {@code this} for fluent-style usage
         */
        public Builder clear(final Word word) {
            Objects.requireNonNull(word);
            for (final Letter letter : word.getLetters()) {
                clear(letter.getRow(), letter.getCol());
            }
            return this;
        }

        /**
         * @param letter the letter indicating the location to be cleared
         * @return {@code this} for fluent-style usage
         */
        public Builder clear(final Letter letter) {
            Objects.requireNonNull(letter);
            return clear(letter.getRow(), letter.getCol());
        }

        /**
         * @param row the row for which a character should be cleared
         * @param col the column for which a character should be cleared
         * @return {@code this} for fluent-style usage
         */
        public Builder clear(final int row, final int col) {
            if (row >= this.size || col >= this.size) {
                throw new IllegalArgumentException(
                        String.format("Out of grid bounds: %d, %d (max: %d)", row, col, this.size - 1));
            }

            this.letters[row][col] = new Letter.Builder(row, col).build();
            return this;
        }

        /**
         * Make the letters fall through empty squares to the bottom of the grid.
         * @return {@code this} for fluent-style usage
         */
        public Builder applyGravity() {
            for (int i = 0; i < this.size; i++) {
                for (int r = this.size - 2; r >= 0; r--) {
                    for (int c = 0; c < this.size; c++) {
                        if (!this.letters[r][c].isEmpty() && this.letters[r + 1][c].isEmpty()) {
                            this.letters[r + 1][c] = new Letter.Builder(r + 1, c, this.letters[r][c].getChar()).build();
                            this.letters[r][c] = new Letter.Builder(r, c).build();
                        }
                    }
                }
            }
            return this;
        }

        /**
         * @return the created letter grid
         */
        public LetterGrid build() {
            return new LetterGrid(this.size, this.letters);
        }
    }
}
