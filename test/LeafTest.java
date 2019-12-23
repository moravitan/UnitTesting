import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class LeafTest {


    @Before
    public void setUp() {
        FileSystem.fileStorage = new SpaceStub(10);
    }


    @Test(expected = OutOfSpaceException.class)
    public void allocateOutOfSpace() throws OutOfSpaceException{
        Leaf leaf = new Leaf("leaf",12);
//        assertEquals("leaf",leaf.name);
        assertEquals(10, FileSystem.fileStorage.countFreeSpace());

    }

    @Test(expected = OutOfSpaceException.class)
    public void allocate() throws OutOfSpaceException {
        Leaf leaf = new Leaf("leaf",2);
//        assertEquals("leaf",leaf.name);
        assertEquals(8,FileSystem.fileStorage.countFreeSpace());
        Leaf leaf1 = new Leaf("leaf1",7);
        String[] path = {"leaf1"};
        assertEquals(1,FileSystem.fileStorage.countFreeSpace());
        Leaf leaf2 = new Leaf("leaf1",2);

    }
}
