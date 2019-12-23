
public class Test {

    public static void main(String[] args) throws BadFileNameException, OutOfSpaceException {
/*        system.Tree t = new system.Tree("root");
        t.GetChildByName("mor");
        t.GetChildByName("itay");
        String [] path = t.getPath();
        for (String str:path) {
            System.out.println(str);
        }*/

        FileSystem fs = new FileSystem(5);
        String[] path = {"root","mor"};
        fs.dir(path);
        String[] path1 = {"root","mor","file1"};
        fs.file(path1,2);
        String[][] disk = fs.disk();
        for (int i = 0; i < disk.length; i++) {
            for (int j = 0; disk[i] != null && j < disk[i].length; j++) {
                System.out.println(disk[i][j]);
            }
        }
        System.out.println(fs.DirExists(path));

    }
}
