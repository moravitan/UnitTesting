import org.junit.Test;
import system.Space;

import static org.junit.Assert.assertEquals;

public class SpaceTest {

    private Space space;


    @Test
    public void initialWithPositiveSize() {
        space = new Space(10);
        assertEquals(10, space.countFreeSpace());
        assertEquals(10, space.getAlloc().length);
    }


    @Test(expected = NegativeArraySizeException.class)
    public void initialWithNegativeSize() {
        space = new Space(-1);
    }

}
