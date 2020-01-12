package mday.wordbrain.model;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the known words available.
 */
public class Dictionary {
    private final static String DICTIONARY_FILE = "words";

    private final LetterNode root = new LetterNode();

    /**
     * @throws IOException if there is a problem loading all of the words from the file
     */
    public void load() throws IOException {
        try (final FileReader fileReader = new FileReader(new File(DICTIONARY_FILE));
             final BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String word = StringUtils.trimToEmpty(line).toLowerCase();
                if (!StringUtils.isEmpty(word)) {
                    add(word);
                }
            }
        }
    }

    /**
     * @param word the word to add to this dictionary
     */
    public void add(final String word) {
        LetterNode node = this.root;
        for (final char c : word.toCharArray()) {
            node = node.getChild(c);
        }
        node.setWord(true);
    }

    /**
     * @param word the word to check to see it exists in this dictionary
     * @return whether the specified word exists in this dictionary
     */
    public boolean exists(final Word word) {
        LetterNode node = this.root;
        for (final Letter letter : word.getLetters()) {
            if (node.hasChild(letter.getChar())) {
                node = node.getChild(letter.getChar());
            } else {
                return false;
            }
        }
        return node.isWord();
    }

    /**
     * @param word the word prefix to check to see if it exists in this dictionary
     * @return whether the specified prefix exists in this dictionary
     */
    public boolean isPrefix(final Word word) {
        LetterNode node = this.root;
        for (final Letter letter : word.getLetters()) {
            if (node.hasChild(letter.getChar())) {
                node = node.getChild(letter.getChar());
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Represents a node in the tree of words contained in this dictionary.
     */
    private static class LetterNode {
        private final char letter;
        private final Map<Character, LetterNode> children = new HashMap<>();

        private boolean isWord = false;

        /**
         * Creates an empty node, only used for the root node of the tree.
         */
        public LetterNode() {
            this.letter = ' ';
        }

        /**
         * @param letter the lowercase letter in this node
         */
        public LetterNode(final char letter) {
            this.letter = Character.toLowerCase(letter);
        }

        /**
         * @param letter the letter to check as a child of this node
         * @return whether the child node exists at the specified letter
         */
        public boolean hasChild(final char letter) {
            return this.children.containsKey(letter);
        }

        /**
         * @param letter the letter for which a child will be returned
         * @return returns the child with the specified letter, creating it if necessary
         */
        public LetterNode getChild(final char letter) {
            LetterNode node = children.get(letter);
            if (node == null) {
                node = new LetterNode(letter);
                this.children.put(letter, node);
            }
            return node;
        }

        /**
         * @return whether this node represents the last character of a word
         */
        public boolean isWord() {
            return this.isWord;
        }

        /**
         * @param isWord the new value specifying whether this node is the last character of a word
         */
        public void setWord(final boolean isWord) {
            this.isWord = isWord;
        }
    }
}
