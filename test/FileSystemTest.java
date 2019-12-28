import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import system.*;

import java.nio.file.DirectoryNotEmptyException;

import static org.junit.Assert.*;

public class FileSystemTest {

    private FileSystem fileSystem;

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

    //*********************** START RED SUITS TEST ***********************

    @Test
    public void creatingExistingDir() {
        try{
            String[] path = {"root","dir1"};
            fileSystem.dir(path);
            Tree dir = fileSystem.DirExists(path);
            fileSystem.dir(path);
            assertEquals(dir,fileSystem.DirExists(path));
        }
        catch (Exception e){
            Assert.fail("Expected nothing, got " + e.getMessage());
        }

    }

    //*********************** END RED SUITS TEST ***********************


    @Test (expected = BadFileNameException.class)
    public void creatingDirSameNameAsExistingFile() throws BadFileNameException, OutOfSpaceException {
        String[] path = {"root","dir1"};
        fileSystem.file(path, 1);
        fileSystem.dir(path);
    }

    @Test
    public void diskAllocSmallFile() throws OutOfSpaceException, BadFileNameException {
        String[] pathFile = {"root", "file1", "dir1"};
        fileSystem.file(pathFile, 1);
        String[][] path = new String[][]{{"root", "file1", "dir1"}, null, null, null, null, null, null, null, null, null};
        assertArrayEquals(path, fileSystem.disk());
    }


    @Test
    public void filesAllocatedWithGAP() throws OutOfSpaceException, BadFileNameException {
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
                {"root", "dir1", "file4"}, null, null, null, {"root", "dir1", "file3"}, {"root", "dir1", "file3"},
                {"root", "dir1", "file4"}};
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
    public void creatingFileWithEnoughSpace() throws BadFileNameException, OutOfSpaceException {
        String[] path = {"root", "file1"};
        fileSystem.file(path, 2);
        assertNotNull(fileSystem.FileExists(path));
        assertEquals(8, FileSystem.fileStorage.countFreeSpace());
        path = new String[]{"root", "dir1", "file1"};
        fileSystem.file(path,2);
        assertNotNull(fileSystem.DirExists(new String[]{"root","dir1"}));
    }

    @Test(expected = OutOfSpaceException.class)
    public void creatingFilesWithSpaceException() throws OutOfSpaceException, BadFileNameException {
        String[] path = {"root", "file1"};
        fileSystem.file(path, 2);
        path = new String[]{"root", "dir1", "file1"};
        fileSystem.file(path, 10);
    }

    @Test
    public void creatingFilesWithEqualSizeToCapacity() throws OutOfSpaceException, BadFileNameException {
        String[] path = {"root", "file1"};
        fileSystem.file(path, 2);
        path = new String[]{"root", "dir1", "file1"};
        fileSystem.file(path, 8);
        assertEquals(0, FileSystem.fileStorage.countFreeSpace());

    }

    @Test
    public void creatingTwoFileSameNameDiffSizeWithNotEnoughSpace() throws BadFileNameException, OutOfSpaceException {
        String[] pathFile = {"root", "dir1", "file1"};
        fileSystem.file(pathFile, 3);
        fileSystem.file(pathFile, 8);
        assertEquals(2,FileSystem.fileStorage.countFreeSpace());
    }

    @Test
    public void creatingTwoFileSameNameDiffSizeWithEnoughSpace() throws BadFileNameException, OutOfSpaceException {
        String[] pathFile = {"root", "dir1", "file1"};
        fileSystem.file(pathFile, 3);
        Leaf leaf = fileSystem.FileExists(pathFile);
        fileSystem.file(pathFile, 4);
        assertNotEquals(leaf,fileSystem.FileExists(pathFile));
    }

    //*********************** START RED SUITS TEST ***********************

    @Test(expected = OutOfSpaceException.class)
    public void creatingTwoFileSameNameDiffSizeWithSizeLargerThanInTheDisk() throws OutOfSpaceException, BadFileNameException {
        String[] pathFile = {"root", "dir1", "file1"};
        fileSystem.file(pathFile, 3);
        Leaf file = fileSystem.FileExists(pathFile);
        fileSystem.file(pathFile, 11);
        assertEquals(file,fileSystem.FileExists(pathFile));
    }

    //*********************** END RED SUITS TEST ***********************

    @Test(expected = BadFileNameException.class)
    public void creatingFileSameNameAsExistingDir() throws BadFileNameException, OutOfSpaceException {
        String[] path = {"root","dir1"};
        fileSystem.dir(path);
        assertNotNull(fileSystem.DirExists(path));
        fileSystem.file(path,1);
        assertNull(fileSystem.FileExists(path));
    }


    // trying to create a file with the same name of existing directory
    @Test(expected = BadFileNameException.class)
    public void BadFileNameExceptionExistingDir() throws BadFileNameException, OutOfSpaceException {
        String[] path = {"root","dir"};
        fileSystem.dir(path);
        String[] pathFile = {"root","dir"};
        fileSystem.file(pathFile, 3);
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
        assertArrayEquals(new String[]{"file1","file2"},dirPath);

    }

    @Test
    public void checkLsOnFile () throws BadFileNameException, OutOfSpaceException {
        String[] path = {"root","dir","file"};
        fileSystem.file(path,1);
        String[] dirPath = fileSystem.lsdir(path);
        assertNull(dirPath);
    }


    // Not supposed to do anything

    @Test
    public void removeNotExistingFile() {
        String[] path = {"file1"};
        fileSystem.rmfile(path);
    }

    @Test
    public void removeExistingFile() throws BadFileNameException, OutOfSpaceException {
        String[] path = {"root", "dir", "file"};
        fileSystem.file(path, 1);
        fileSystem.rmfile(path);
        assertEquals(FileSystem.fileStorage.countFreeSpace(), 10);

    }

    // Not supposed to do anything

    @Test
    public void removeNotExistingDir() throws DirectoryNotEmptyException {
        String[] path = {"dir"};
        fileSystem.rmdir(path);
    }

    @Test
    public void removeExistingDir() throws BadFileNameException, DirectoryNotEmptyException {
        String[] path = {"root", "dir"};
        fileSystem.dir(path);
        fileSystem.rmdir(path);
        assertNull(fileSystem.DirExists(path));
    }

    @Test(expected = DirectoryNotEmptyException.class)
    public void removeNotEmptyDir() throws BadFileNameException, OutOfSpaceException, DirectoryNotEmptyException {
        String[] pathDir = {"root", "dir"};
        fileSystem.dir(pathDir);
        String[] pathFile = {"root", "dir", "file"};
        fileSystem.file(pathFile, 2);
        fileSystem.rmdir(pathDir);
        assertEquals(8, FileSystem.fileStorage.countFreeSpace());
    }

}
