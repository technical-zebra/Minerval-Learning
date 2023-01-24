package sg.jcu.kezhang.minervalearning;

public class Term {
    /* Declared Instance Variables. */
    private String word;
    private String definition;

    /* Constructor. */

    /***
     * Creates a new Term object.
     * @param word The word of the term.
     * @param definition The definition of the term.
     */
    public Term(String word, String definition) {
        this.word = word;
        this.definition = definition;
    }

    /* Getter and Setter. */

    /***
     * Get the word.
     * @return The word.
     */
    public String getWord() {
        return word;
    }

    /***
     * Set the word.
     * @param word The word to be set.
     */
    public void setWord(String word) {
        this.word = word;
    }

    /***
     * Get the definition of the term.
     * @return The definition of the term.
     */
    public String getDefinition() {
        return definition;
    }

    /***
     * Set the definition of the term.
     * @param definition The definition of the term.
     */
    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
