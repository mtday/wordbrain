package mday.wordbrain.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;

/**
 *
 */
public class LetterGridTest {
    @Test
    public void testGetAndSet() {
        LetterGrid letterGrid = new LetterGrid.Builder(3).build();
        assertTrue(letterGrid.get(0, 0).isEmpty());

        letterGrid = new LetterGrid.Builder(letterGrid).set(0, 0, 'A').build();
        assertEquals(' ', letterGrid.get(0, 0).getChar()); // gravity makes the 'a' fall to row 2.
        assertEquals('a', letterGrid.get(2, 0).getChar());
    }

    @Test
    public void testRemoveWordAndGravity() {
        // +---+---+---+---+
        // | N | H | Y | N |
        // +---+---+---+---+
        // | P | R | M | O |
        // +---+---+---+---+
        // | R | O | S | L |
        // +---+---+---+---+
        // | U | T | E | E |
        // +---+---+---+---+
        final LetterGrid grid = new LetterGrid.Builder(4).set("NHYN", "PRMO", "ROSL", "UTEE").build();

        final Letter M = grid.get(1, 2);
        final Letter O = grid.get(1, 3);
        final Letter L = grid.get(2, 3);
        final Letter E = grid.get(3, 3);
        final Letter S = grid.get(2, 2);
        final Letter T = grid.get(3, 1);

        final Word word = new Word.Builder().add(Arrays.asList(M, O, L, E, S, T)).build();

        final LetterGrid modified = new LetterGrid.Builder(grid).clear(word).applyGravity().build();

        final StringBuilder expected = new StringBuilder();
        expected.append("+---+---+---+---+" + System.lineSeparator());
        expected.append("| N |   |   |   |" + System.lineSeparator());
        expected.append("+---+---+---+---+" + System.lineSeparator());
        expected.append("| P | H |   |   |" + System.lineSeparator());
        expected.append("+---+---+---+---+" + System.lineSeparator());
        expected.append("| R | R | Y |   |" + System.lineSeparator());
        expected.append("+---+---+---+---+" + System.lineSeparator());
        expected.append("| U | O | E | N |" + System.lineSeparator());
        expected.append("+---+---+---+---+" + System.lineSeparator());

        assertEquals(expected.toString(), modified.toString());
    }

    @Test
    public void testGetAdjacent() {
        // +---+---+---+
        // | A |   | C |
        // +---+---+---+
        // | D | B | F |
        // +---+---+---+
        // | G | H | I |
        // +---+---+---+
        final LetterGrid letterGrid = new LetterGrid.Builder(3).set("ABC", "d f", "GhI").build();

        assertEquals("BD", new Word.Builder(letterGrid.getAdjacent(letterGrid.get(0, 0))).build().toString());
        assertEquals("ACFBD", new Word.Builder(letterGrid.getAdjacent(letterGrid.get(0, 1))).build().toString());
        assertEquals("FB", new Word.Builder(letterGrid.getAdjacent(letterGrid.get(0, 2))).build().toString());
        assertEquals("ABHG", new Word.Builder(letterGrid.getAdjacent(letterGrid.get(1, 0))).build().toString());
        assertEquals("DACFIHG", new Word.Builder(letterGrid.getAdjacent(letterGrid.get(1, 1))).build().toString());
        assertEquals("BCIH", new Word.Builder(letterGrid.getAdjacent(letterGrid.get(1, 2))).build().toString());
        assertEquals("DBH", new Word.Builder(letterGrid.getAdjacent(letterGrid.get(2, 0))).build().toString());
        assertEquals("GDBFI", new Word.Builder(letterGrid.getAdjacent(letterGrid.get(2, 1))).build().toString());
        assertEquals("HBF", new Word.Builder(letterGrid.getAdjacent(letterGrid.get(2, 2))).build().toString());
    }

    @Test
    public void testToStringEmpty() {
        final StringBuilder expected = new StringBuilder();
        expected.append("+---+---+---+" + System.lineSeparator());
        expected.append("|   |   |   |" + System.lineSeparator());
        expected.append("+---+---+---+" + System.lineSeparator());
        expected.append("|   |   |   |" + System.lineSeparator());
        expected.append("+---+---+---+" + System.lineSeparator());
        expected.append("|   |   |   |" + System.lineSeparator());
        expected.append("+---+---+---+" + System.lineSeparator());

        final LetterGrid empty = new LetterGrid.Builder(3).build();
        assertEquals(expected.toString(), empty.toString());
    }

    @Test
    public void testToStringFull() {
        final StringBuilder expected = new StringBuilder();
        expected.append("+---+---+---+" + System.lineSeparator());
        expected.append("| A | B | C |" + System.lineSeparator());
        expected.append("+---+---+---+" + System.lineSeparator());
        expected.append("| D | E | F |" + System.lineSeparator());
        expected.append("+---+---+---+" + System.lineSeparator());
        expected.append("| G | H | I |" + System.lineSeparator());
        expected.append("+---+---+---+" + System.lineSeparator());

        final LetterGrid letterGrid = new LetterGrid.Builder(3).set("ABC", "def", "GhI").build();
        assertEquals(expected.toString(), letterGrid.toString());
    }
}
