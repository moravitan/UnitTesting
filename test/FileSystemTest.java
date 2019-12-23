import org.junit.Before;
import org.junit.Test;

import java.nio.file.DirectoryNotEmptyException;

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

    @Test
    public void diskAlloc() throws OutOfSpaceException, BadFileNameException {
        String[][] path = {null,null,null,null,null,null,null,null,null,null};
        assertArrayEquals(path, fileSystem.disk());
        String[] pathFile = {"root","file1","dir1"};
        fileSystem.file(pathFile, 1);
        path = new String[][]{{"root", "file1", "dir1"}, null, null, null, null, null, null, null, null, null};
        assertArrayEquals(path, fileSystem.disk());
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
        path = new String[]{"root","dir1","file1"};
        fileSystem.file(path,1);
        assertNotNull(fileSystem.FileExists(path));
    }

    @Test
    public void removeFileAndCreate () throws BadFileNameException, OutOfSpaceException {
        String[] pathFile1 = {"root","dir1","file1"};
        fileSystem.file(pathFile1, 3);
        assertNotNull(fileSystem.FileExists(pathFile1));
        assertEquals(7, FileSystem.fileStorage.countFreeSpace());
        String[] pathFile2 = {"root","dir1","file1"};
        fileSystem.file(pathFile2, 8);
        assertNotNull(fileSystem.FileExists(pathFile2));
        assertEquals(2, FileSystem.fileStorage.countFreeSpace());
    }

//    We need to check the casting

//    @Test(expected = BadFileNameException.class)
//    public void BadFileNameExceptionExistingDir() throws BadFileNameException, OutOfSpaceException {
//        String[] path = {"root","dir"};
//        fileSystem.dir(path);
//        String[] pathFile = {"root","dir"};
//        fileSystem.file(pathFile, 3);
//    }

    @Test
    public void checkIfIsDir () throws BadFileNameException, OutOfSpaceException {
        String[] path = {null};
        assertNull(fileSystem.lsdir(path));
        System.out.println("dir doesn't exist");
        path = new String[]  {"root","dir"};
        fileSystem.dir(path);
//        check this line
//        String[] dirPath = fileSystem.lsdir(path);
//        assertEquals(2, dirPath.length);
        assertNotNull(fileSystem.lsdir(path));
        System.out.println("dir exist");
    }

    @Test
    public void removeNotExistingFile () throws BadFileNameException, OutOfSpaceException {
        String[] path = {"file1"};
        fileSystem.rmfile(path);
    }

    @Test
    public void removeExistingFile () throws BadFileNameException, OutOfSpaceException {
        String[] path = {"root","dir","file"};
        fileSystem.file(path,1);
        assertNotNull(fileSystem.FileExists(path));
        fileSystem.rmfile(path);
        assertNull(fileSystem.FileExists(path));
    }

    @Test
    public void removeNotExistingDir () throws  DirectoryNotEmptyException {
        String[] path = {"dir"};
        fileSystem.rmdir(path);
    }

    @Test
    public void removeExistingDir () throws BadFileNameException, DirectoryNotEmptyException {
        String[] path = {"root","dir"};
        fileSystem.dir(path);
        assertNotNull(fileSystem.DirExists(path));
        fileSystem.rmdir(path);
        assertNull(fileSystem.DirExists(path));
    }

    @Test(expected = DirectoryNotEmptyException.class)
    public void removeNotEmptyDir () throws BadFileNameException,  OutOfSpaceException, DirectoryNotEmptyException {
        String[] pathDir = {"root","dir"};
        fileSystem.dir(pathDir);
        assertNotNull(fileSystem.DirExists(pathDir));
        String[] pathFile = {"root","dir", "file"};
        fileSystem.file(pathFile, 2);
        fileSystem.rmdir(pathDir);
    }

    @Test
    public void fileExists () throws BadFileNameException, OutOfSpaceException {
        String[] path = {"root","dir","file"};
        fileSystem.file(path, 2);
        assertNotNull(fileSystem.FileExists(path));
        fileSystem.rmfile(path);
        assertNull(fileSystem.FileExists(path));
    }
    @Test
    public void fileNotExists () throws BadFileNameException, OutOfSpaceException {
        String[] path = {"root","dir"};
        assertNull(fileSystem.FileExists(path));
    }

    @Test
    public void DirExists () throws BadFileNameException, OutOfSpaceException, DirectoryNotEmptyException {
        String[] path = {"root","dir1"};
        fileSystem.dir(path);
        assertNotNull(fileSystem.DirExists(path));
        fileSystem.rmdir(path);
        assertNull(fileSystem.DirExists(path));
    }
    @Test
    public void DirNotExists () throws BadFileNameException, OutOfSpaceException {
        String[] path = {"root"};
        assertNull(fileSystem.DirExists(path));
    }

}
