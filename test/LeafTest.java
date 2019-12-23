import org.junit.Before;
import org.junit.Test;

public class LeafTest {

    Leaf leaf;

    @Before
    public void setUp() throws OutOfSpaceException {
        FileSystem fs = new FileSystem(10);
        leaf = new Leaf("leaf",10);
    }

    @Test
    public void d() throws OutOfSpaceException {
        FileSystem fs = new FileSystem(10);
        leaf = new Leaf("leaf",10);

    }


}
