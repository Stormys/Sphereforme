package sphereforme.sphereforme;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void createnCard() {
        nCard ncard = new nCard();
        ncard.addAttr("name", "steven");
        ncard.addAttr("email", "smfields@umail.ucsb.edu");
        ncard.addAttr("home_phone", "714-642-4262");
        String correct = "#CARD#\n"
                + "name: steven\n"
                + "email: smfields@umail.ucsb.edu\n"
                + "home_phone: 714-642-4262\n"
                + "#CARD#";
        assertEquals(correct, ncard.toString());
    }

}