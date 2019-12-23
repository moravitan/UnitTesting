import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileSystemTest {

    FileSystem fileSystem;

    @Before
    public void setUp() {
        fileSystem = new FileSystem(10);
//        FileSystem.fileStorage
    }


    @Test
    public void initial(){
        String[] root = {"root"};
        assertNull(fileSystem.DirExists(root));
    }

    @Test(expected = BadFileNameException.class)
    public void dirThrowingException() throws BadFileNameException {
        String[] path = {"dir1"};
        fileSystem.dir(path);
    }

    @Test
    public void creatingDir() throws BadFileNameException {
        String[] path = {"root","dir1"};
        fileSystem.dir(path);
        assertNotNull(fileSystem.DirExists(path));
    }

    @Test(expected = BadFileNameException.class)
    public void fileThrowingBadNameFileException () throws BadFileNameException, OutOfSpaceException {
        String[] path = {"file1"};
        fileSystem.file(path, 2);
    }

    @Test(expected = OutOfSpaceException.class)
    public void fileThrowingOutOfSpaceException () throws BadFileNameException, OutOfSpaceException {
        String[] path = {"root","file1"};
        fileSystem.file(path, 11);
    }

    @Test
    public void creatingFile () throws BadFileNameException, OutOfSpaceException {
        String[] path = {"root","file1"};
        fileSystem.file(path, 2);
        assertNotNull(fileSystem.FileExists(path));
    }

    @Test(expected = OutOfSpaceException.class)
    public void creatingFiles () throws BadFileNameException, OutOfSpaceException {
        String[] path = {"root","file1","dir1"};
        fileSystem.file(path,1);
        assertNotNull(fileSystem.FileExists(path));
    }
}
