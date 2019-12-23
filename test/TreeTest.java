import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TreeTest {

    private Tree tree;


    @Before
    public void setUp() {
        tree = new Tree("tree");

    }

    @Test
    public void initial() {
        assertEquals(0, tree.children.size());
        assertEquals(0,tree.depth);
        assertNull(tree.parent);
    }

    @Test
    public void getChildByName() {
        Tree root = tree.GetChildByName("root");
        assertEquals(root,tree.children.get("root"));
        assertEquals(0,tree.depth);
        assertEquals(1,root.depth);
        assertEquals(root.parent,tree);
        Tree existedNewTree = tree.GetChildByName("root");
        assertEquals(root,existedNewTree);
    }

    @Test
    public void getPath(){
        Tree root = tree.GetChildByName("root");
        Tree son = root.GetChildByName("son");
        String[] treePath = son.getPath();
        String[] expectedPath = {"root","son"};
        assertArrayEquals(treePath, expectedPath);
    }

}