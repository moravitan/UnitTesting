
public class SpaceStub extends Space{

    /**
     * Ctor - create \c size blank filesystem blocks.
     *
     * @param size
     */

    SpaceStub(int size) {
        super(size);
    }

    @Override
    public void Alloc(int size, Leaf file) throws OutOfSpaceException {

        if(countFreeSpace() < size){
            throw new OutOfSpaceException();
        }

        super.Alloc(size,file);



    }
}
