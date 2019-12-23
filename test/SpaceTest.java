import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SpaceTest {

    private Space space;

    @Before
    public void setUp() {
        space = new Space(10);
    }

    @Test
    public void initial() {
        assertEquals(10, space.countFreeSpace());
        assertEquals(10, space.getAlloc().length);
    }

}
