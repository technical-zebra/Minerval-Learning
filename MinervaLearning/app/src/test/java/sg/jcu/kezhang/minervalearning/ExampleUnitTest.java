package sg.jcu.kezhang.minervalearning;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void testTerm(){

        Term term = new Term("Happy", "Feeling or showing pleasure or contentment.");
        assertEquals("Happy", term.getWord());
        assertEquals("Feeling or showing pleasure or contentment.", term.getDefinition());

        term.setWord("Sad");
        term.setDefinition("Feeling or showing sorrow; unhappy.");
        assertEquals("Sad", term.getWord());
        assertEquals("Feeling or showing sorrow; unhappy.", term.getDefinition());
    }



}