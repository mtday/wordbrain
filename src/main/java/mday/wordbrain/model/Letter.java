package mday.wordbrain.model;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Objects;

/**
 * Represents a single letter within the grid, along with the location where the letter is located.
 */
public class Letter implements Comparable<Letter> {
    public final static char EMPTY = ' ';

    private final int row;
    private final int col;
    private final char c;

    /**
     * Create a new empty letter.
     *
     * @param row the row from which this letter came
     * @param col the column from which this letter came
     */
    private Letter(final int row, final int col) {
        this(row, col, EMPTY);
    }

    /**
     * Create a new letter with a value.
     *
     * @param row the row from which this letter came
     * @param col the column from which this letter came
     * @param c the character value
     */
    private Letter(final int row, final int col, final char c) {
        this.row = row;
        this.col = col;
        this.c = c;
    }

    /**
     * @return the row from which this letter came
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return the column from which this letter came
     */
    public int getCol() {
        return this.col;
    }

    /**
     * @return the lowercase character value
     */
    public char getChar() {
        return this.c;
    }

    /**
     * @return whether this letter is empty
     */
    public boolean isEmpty() {
        return getChar() == EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.valueOf(Character.toUpperCase(getChar()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final Letter other) {
        if (other == null) {
            return 1;
        }

        final CompareToBuilder cmp = new CompareToBuilder();
        cmp.append(getChar(), other.getChar());
        cmp.append(getRow(), other.getRow());
        cmp.append(getCol(), other.getCol());
        return cmp.toComparison();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object other) {
        return other instanceof Letter && compareTo((Letter) other) == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final HashCodeBuilder hash = new HashCodeBuilder();
        hash.append(getChar());
        hash.append(getRow());
        hash.append(getCol());
        return hash.toHashCode();
    }

    /**
     * Used to build letter objects.
     */
    public static class Builder {
        private int row;
        private int col;
        private char c = EMPTY;

        /**
         * @param other the letter to copy
         */
        public Builder(final Letter other) {
            Objects.requireNonNull(other);
            setRow(other.getRow());
            setCol(other.getCol());
            setChar(other.getChar());
        }

        /**
         * @param other the letter to copy, except with the provided character
         * @param c the new character value
         */
        public Builder(final Letter other, final char c) {
            Objects.requireNonNull(other);
            setRow(other.getRow());
            setCol(other.getCol());
            setChar(c);
        }

        /**
         * @param row the row where the letter is located
         * @param col the column where the letter is located
         * @return {@code this} for fluent-style usage
         */
        public Builder(final int row, final int col) {
            setRow(row);
            setCol(col);
        }

        /**
         * @param row the row where the letter is located
         * @param col the column where the letter is located
         * @param c the new character value
         * @return {@code this} for fluent-style usage
         */
        public Builder(final int row, final int col, final char c) {
            setRow(row);
            setCol(col);
            setChar(c);
        }

        /**
         * @param row the new row value where the letter is located
         * @return {@code this} for fluent-style usage
         */
        public Builder setRow(final int row) {
            if (row < 0 || row > 8) {
                throw new IllegalArgumentException("Invalid row: " + row);
            }

            this.row = row;
            return this;
        }

        /**
         * @param col the new column value where the letter is located
         * @return {@code this} for fluent-style usage
         */
        public Builder setCol(final int col) {
            if (col < 0 || col > 8) {
                throw new IllegalArgumentException("Invalid column: " + col);
            }

            this.col = col;
            return this;
        }

        /**
         * Set the character value to the EMPTY value.
         * @return {@code this} for fluent-style usage
         */
        public Builder clear() {
            this.c = EMPTY;
            return this;
        }

        /**
         * @param c the new character value
         * @return {@code this} for fluent-style usage
         */
        public Builder setChar(final char c) {
            if (!Character.isAlphabetic(c) && c != EMPTY) {
                throw new IllegalArgumentException("Invalid character: " + c);
            }

            this.c = Character.toLowerCase(c);
            return this;
        }

        /**
         * @return the created letter
         */
        public Letter build() {
            return new Letter(this.row, this.col, this.c);
        }
    }
}
