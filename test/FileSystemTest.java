import org.junit.Before;
import org.junit.Test;

import java.nio.file.DirectoryNotEmptyException;

import static org.junit.Assert.*;

public class FileSystemTest {

    FileSystem fileSystem;

    @Before
    public void setUp() {
        fileSystem = new FileSystem(10);
    }


    @Test
    public void initial() {
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
        String[] path = {"root", "dir1"};
        fileSystem.dir(path);
        assertNotNull(fileSystem.DirExists(path));
    }

/*    @Test
    public void creatingExistingDir() throws BadFileNameException {
        String[] path = {"root","dir1"};
        fileSystem.dir(path);
        assertNotNull(fileSystem.DirExists(path));
        Tree dir = fileSystem.DirExists(path);
        fileSystem.dir(path);
        assertEquals(dir,fileSystem.DirExists(path));
    }*/

    @Test
    public void diskAllocSmallFile() throws OutOfSpaceException, BadFileNameException {
        String[][] path = {null, null, null, null, null, null, null, null, null, null};
        assertArrayEquals(path, fileSystem.disk());
        String[] pathFile = {"root", "file1", "dir1"};
        fileSystem.file(pathFile, 1);
        path = new String[][]{{"root", "file1", "dir1"}, null, null, null, null, null, null, null, null, null};
        assertArrayEquals(path, fileSystem.disk());
        fileSystem.rmfile(pathFile);
        path = new String[][]{null, null, null, null, null, null, null, null, null, null};
        assertArrayEquals(path, fileSystem.disk());
    }


    @Test
    public void diskWithTwoFiles() throws OutOfSpaceException, BadFileNameException {
        String[] pathFile3 = {"root", "dir1", "file1"};
        fileSystem.file(pathFile3, 3);
        String[] pathFile4 = {"root", "dir1", "file2"};
        fileSystem.file(pathFile4, 4);
        String[] pathFile2 = {"root", "dir1", "file3"};
        fileSystem.file(pathFile2, 2);
        fileSystem.rmfile(pathFile4);
        String[] pathFile = {"root", "dir1", "file4"};
        fileSystem.file(pathFile, 2);
        String[][] pathFromDisk = fileSystem.disk();
        String[][] path = new String[][]{{"root", "dir1", "file1"}, {"root", "dir1", "file1"}, {"root", "dir1", "file1"},
                {"root", "dir1", "file4"}, null, null, null, {"root", "dir1", "file3"}, {"root", "dir1", "file3"},{"root", "dir1", "file4"}};
        assertArrayEquals(path,pathFromDisk);
    }


    @Test(expected = BadFileNameException.class)
    public void fileThrowingBadNameFileException() throws BadFileNameException, OutOfSpaceException {
        String[] path = {"file1"};
        fileSystem.file(path, 2);
    }

    @Test(expected = OutOfSpaceException.class)
    public void fileThrowingOutOfSpaceException() throws BadFileNameException, OutOfSpaceException {
        String[] path = {"root", "file1"};
        fileSystem.file(path, 11);
    }

    @Test
    public void creatingFile() throws BadFileNameException, OutOfSpaceException {
        String[] path = {"root", "file1"};
        fileSystem.file(path, 2);
        assertNotNull(fileSystem.FileExists(path));
        path = new String[]{"root", "dir1", "file1"};
        fileSystem.file(path, 1);
        assertNotNull(fileSystem.FileExists(path));
        assertNotNull(fileSystem.FileExists(new String[]{"root", "file1"}));
    }

    @Test(expected = OutOfSpaceException.class)
    public void creatingFilesWithSpaceException() throws OutOfSpaceException, BadFileNameException {
        String[] path = {"root", "file1"};
        fileSystem.file(path, 2);
        assertNotNull(fileSystem.FileExists(path));
        path = new String[]{"root", "dir1", "file1"};
        fileSystem.file(path, 10);
    }

    @Test
    public void creatingFilesWithEqualSizeToCapacity() throws OutOfSpaceException, BadFileNameException {
        String[] path = {"root", "file1"};
        fileSystem.file(path, 2);
        assertNotNull(fileSystem.FileExists(path));
        path = new String[]{"root", "dir1", "file1"};
        fileSystem.file(path, 8);
        assertNotNull(fileSystem.FileExists(path));

    }

    @Test
    public void creatingTwoFileSameNameDiffSizeWithNotEnoughSpace() throws BadFileNameException, OutOfSpaceException {
        String[] pathFile1 = {"root", "dir1", "file1"};
        fileSystem.file(pathFile1, 3);
        assertNotNull(fileSystem.FileExists(pathFile1));
        String[] pathFile2 = {"root", "dir1", "file1"};
        fileSystem.file(pathFile2, 8);
        assertNotNull(fileSystem.FileExists(pathFile2));
//        assertNull(fileSystem.FileExists(pathFile1));
    }

    @Test
    public void creatingTwoFileSameNameDiffSizeWithEnoughSpace() throws BadFileNameException, OutOfSpaceException {
        String[] pathFile1 = {"root", "dir1", "file1"};
        fileSystem.file(pathFile1, 3);
        assertNotNull(fileSystem.FileExists(pathFile1));
        assertEquals(7, FileSystem.fileStorage.countFreeSpace());
        String[] pathFile2 = {"root", "dir1", "file1"};
        fileSystem.file(pathFile2, 4);
        assertNotNull(fileSystem.FileExists(pathFile2));
//        assertNull(fileSystem.FileExists(pathFile1));
    }


    // trying to create a file with the same name of existing directory
/*    @Test(expected = BadFileNameException.class)
    public void BadFileNameExceptionExistingDir() throws BadFileNameException, OutOfSpaceException {
        String[] path = {"root","dir"};
        fileSystem.dir(path);
        String[] pathFile = {"root","dir"};
        fileSystem.file(pathFile, 3);
    }*/


    @Test
    public void removeFileAndCreateWithEnoughSpace() throws OutOfSpaceException, BadFileNameException {
        String[] path = {"root", "file1"};
        fileSystem.file(path, 2);
        fileSystem.file(path, 3);
        assertNotNull(fileSystem.FileExists(path));
        assertEquals(7, FileSystem.fileStorage.countFreeSpace());

    }

    @Test
    public void checkIfIsDir() throws BadFileNameException, OutOfSpaceException {
        String[] path = {null};
        assertNull(fileSystem.lsdir(path));
        path = new String[]{"root", "dir"};
        fileSystem.dir(path);
        String[] dirPath = fileSystem.lsdir(path);
        assertEquals(0, dirPath.length);
        assertNotNull(fileSystem.lsdir(path));
        path = new String[]{"root", "dir", "file"};
        fileSystem.file(path, 1);
        dirPath = fileSystem.lsdir(new String[]{"root", "dir"});
        assertEquals(1, dirPath.length);
    }

    @Test
    public void checklsSort() throws OutOfSpaceException, BadFileNameException {
        String[] path = {"root", "dir", "file2"};
        fileSystem.file(path, 1);
        path = new String[]{"root", "dir", "file1"};
        fileSystem.file(path, 1);
        String[] dirPath = fileSystem.lsdir(new String[]{"root","dir"});
        assertEquals("file1",dirPath[0]);
        assertEquals("file2",dirPath[1]);

    }

/*    @Test
    public void checkLsOnFile () throws BadFileNameException, OutOfSpaceException {
        String[] path = {"root","dir","file"};
        fileSystem.file(path,1);
        String[] dirPath = fileSystem.lsdir(path);
        assertNull(dirPath);
    }*/

    @Test
    public void removeNotExistingFile() {
        String[] path = {"file1"};
        fileSystem.rmfile(path);
    }

    @Test
    public void removeExistingFile() throws BadFileNameException, OutOfSpaceException {
        String[] path = {"root", "dir", "file"};
        fileSystem.file(path, 1);
        assertNotNull(fileSystem.FileExists(path));
        fileSystem.rmfile(path);
        assertNull(fileSystem.FileExists(path));
        assertEquals(FileSystem.fileStorage.countFreeSpace(), 10);

    }

    @Test
    public void removeNotExistingDir() throws DirectoryNotEmptyException {
        String[] path = {"dir"};
        fileSystem.rmdir(path);
    }

    @Test
    public void removeExistingDir() throws BadFileNameException, DirectoryNotEmptyException {
        String[] path = {"root", "dir"};
        fileSystem.dir(path);
        assertNotNull(fileSystem.DirExists(path));
        fileSystem.rmdir(path);
        assertNull(fileSystem.DirExists(path));
    }

    @Test(expected = DirectoryNotEmptyException.class)
    public void removeNotEmptyDir() throws BadFileNameException, OutOfSpaceException, DirectoryNotEmptyException {
        String[] pathDir = {"root", "dir"};
        fileSystem.dir(pathDir);
        assertNotNull(fileSystem.DirExists(pathDir));
        String[] pathFile = {"root", "dir", "file"};
        fileSystem.file(pathFile, 2);
        assertNotNull(fileSystem.FileExists(pathFile));
        fileSystem.rmdir(pathDir);
        assertEquals(8, FileSystem.fileStorage.countFreeSpace());
    }


   @Test
    public void fileExists() throws BadFileNameException, OutOfSpaceException {
        String[] path = {"root", "dir", "file"};
        fileSystem.file(path, 2);
        assertNotNull(fileSystem.FileExists(path));
        fileSystem.rmfile(path);
        assertNull(fileSystem.FileExists(path));
    }

    @Test
    public void fileNotExists() {
        String[] path = {"root", "dir"};
        assertNull(fileSystem.FileExists(path));
    }

    @Test
    public void DirExists() throws BadFileNameException, DirectoryNotEmptyException {
        String[] path = {"root", "dir1"};
        fileSystem.dir(path);
        assertNotNull(fileSystem.DirExists(path));
        fileSystem.rmdir(path);
        assertNull(fileSystem.DirExists(path));
    }

    @Test
    public void DirNotExists() {
        String[] path = {"root"};
        assertNull(fileSystem.DirExists(path));
    }

}
