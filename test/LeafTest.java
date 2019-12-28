import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import system.*;

import static org.junit.Assert.assertEquals;


public class LeafTest {


    @Before
    public void setUp() {
        FileSystem.fileStorage = new Space(10);
    }

    //*********************** START RED SUITS TEST ***********************


    @Test(expected = OutOfSpaceException.class)
    public void allocateOutOfSpace() throws OutOfSpaceException{
        try{
            Leaf leaf = new Leaf("leaf",12);
            assertEquals(10, FileSystem.fileStorage.countFreeSpace());
        }
        catch (Exception e){
            Assert.fail("expected OutOfSpaceException got " + e.getMessage());
        }


    }

    //*********************** END RED SUITS TEST ***********************



    @Test
    public void allocate() throws OutOfSpaceException {
        Leaf leaf = new Leaf("leaf",2);
        assertEquals(8,FileSystem.fileStorage.countFreeSpace());
        Leaf leaf1 = new Leaf("leaf1",7);
    }


}
