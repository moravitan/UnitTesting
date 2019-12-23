import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class LeafTest {

     Leaf leaf;
     FileSystem fileSystem;

    @Before
    public void setUp() {
        fileSystem = new FileSystem(10);
    }


    @Test(expected = NullPointerException.class)
    public void allocateOutOfSpace() throws OutOfSpaceException{
        leaf = new Leaf("leaf",12);
        assertEquals("leaf",leaf.name);
        assertEquals(0,FileSystem.fileStorage.countFreeSpace());

    }

    @Test
    public void allocate() throws OutOfSpaceException{
        leaf = new Leaf("leaf",2);
        assertEquals("leaf",leaf.name);
        assertEquals(8,FileSystem.fileStorage.countFreeSpace());
        Leaf leaf1 = new Leaf("leaf1",7);
        String[] path = {"leaf1"};
        assertEquals(1,FileSystem.fileStorage.countFreeSpace());

    }
}
