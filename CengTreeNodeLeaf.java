import java.util.ArrayList;

public class CengTreeNodeLeaf extends CengTreeNode
{
    private ArrayList<CengBook> books;

    public CengTreeNodeLeaf(CengTreeNode parent)
    {
        super(parent);
        this.books = new ArrayList<>();
        this.type = CengNodeType.Leaf;
    }

    // GUI Methods - Do not modify
    public int bookCount()
    {
        return books.size();
    }
    public Integer bookKeyAtIndex(Integer index)
    {
        if(index >= this.bookCount()) {
            return -1;
        } else {
            CengBook book = this.books.get(index);

            return book.getBookID();
        }
    }

    public CengBook bookAtIndex(Integer index) {
        if(index >= this.bookCount()) {
            return null;
        }
        return this.books.get(index);
    }

    public void insertBook(CengBook book) {
        books.add(book);
        int n = this.bookCount();
        for (int i = n-1; i > 0; --i) {
            if (this.bookKeyAtIndex(i) < this.bookKeyAtIndex(i - 1)) {
                CengBook temp = books.get(i - 1);
                books.set(i - 1, books.get(i));
                books.set(i, temp);
            }
        }
    }

    public CengBook findByBookId(Integer bookID) {
        for (int i = 0, n = this.bookCount(); i < n; ++i) {
            if (this.bookKeyAtIndex(i).equals(bookID)) {
                return bookAtIndex(i);
            }
        }
        return null;
    }

    public void split(CengTree tree) {
        CengTreeNodeLeaf l2 = new CengTreeNodeLeaf(this.getParent());
        int n = this.bookCount();
        for (int i = n / 2; i < n; ++i) {
            l2.insertBook(this.bookAtIndex(i));
        }
        this.books.subList(n / 2, n).clear();

        if (this.getParent() == null) {
            CengTreeNodeInternal newRoot = new CengTreeNodeInternal(null);

            newRoot.insertKey(l2.bookKeyAtIndex(0));
            newRoot.insertChild(this);
            newRoot.insertChild(l2);

            tree.root = newRoot;

            this.setParent(newRoot);
            l2.setParent(newRoot);

        } else {
            CengTreeNodeInternal parent = (CengTreeNodeInternal) this.getParent();

            parent.insertKey(l2.bookKeyAtIndex(0));
            parent.insertChildNextTo(l2, this);

            if (parent.keyCount() > 2 * CengTreeNode.order) {
                parent.split(tree);
            }
        }
    }

}
