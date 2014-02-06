package sturesy.test.core.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import sturesy.core.ui.HTMLStripper;

public class TestHTMLStripper
{

    @Test
    public void test()
    {

        String htmlstring = "<html><body>some text</br>some more text <p>some text<br>and stuff</p></body></html>";

        String expected = "some text some more text some text and stuff";

        assertEquals(expected, HTMLStripper.stripHTML(htmlstring));
    }

    @Test
    public void test2()
    {

        String htmlstring = "<html><body>a < b > c</p></body></html>";

        String expected = "a < b > c";

        assertEquals(expected, HTMLStripper.stripHTML2(htmlstring));
    }

    @Test
    public void test3()
    {
        String smallerquals = "<html>a <= b</html>";

        assertEquals("a <= b", HTMLStripper.stripHTML2(smallerquals));

        String greaterEquals = "<html>a >= b</html>";

        assertEquals("a >= b", HTMLStripper.stripHTML2(greaterEquals));

        String wontWork = "<html>a<=b>=c</html>";

        assertFalse("a<=b>=c".equals(HTMLStripper.stripHTML2(wontWork)));

        String triple = "<html>a <= b >= c</html>";

        assertEquals("a <= b >= c", HTMLStripper.stripHTML2(triple));

    }
}
