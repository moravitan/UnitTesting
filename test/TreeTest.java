import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import system.Tree;

import static org.junit.Assert.*;

public class TreeTest {

    private Tree tree;


    @Before
    public void setUp() {
        tree = new Tree("root");

    }

    @Test
    public void initial() {
        assertEquals(0, tree.children.size());
        assertNull(tree.parent);
    }

    @Test
    public void getChildByName() {
        Tree firstSon = tree.GetChildByName("firstSon");
        assertEquals(firstSon,tree.children.get("firstSon"));
        assertEquals(firstSon.parent,tree);
        Tree existedNewTree = tree.GetChildByName("firstSon");
        assertEquals(firstSon,existedNewTree);
        Tree secondSon = new Tree("secondSon");
        tree.children.put("secondSon",secondSon);
        assertEquals(secondSon,tree.GetChildByName("secondSon"));
    }

    //*********************** START RED SUITS TEST ***********************


    /**
     * Assuming getPath should contain 'root'
     */
    @Test
    public void getPath(){
        try{
            Tree father = tree.GetChildByName("father");
            Tree son = father.GetChildByName("son");
            String[] treePath = son.getPath();
            String[] expectedPath = {"root","father","son"};
            assertEquals("root",treePath[0]);
            assertArrayEquals(expectedPath,treePath);
        } catch (Exception e){
            Assert.fail("expected arrays equals, got " + e.getMessage());
        }
    }

    //*********************** END RED SUITS TEST ***********************


}