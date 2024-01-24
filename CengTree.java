import java.util.ArrayList;

public class CengTree
{
    public CengTreeNode root;

    public CengTree(Integer order)
    {
        if (order < 1) {
            throw new IllegalStateException("Order must be greater than 0.");
        }
        CengTreeNode.order = order;
        this.root = new CengTreeNodeLeaf(null);
    }

    public void addBook(CengBook book)
    {
        this.addBook(book, this.root);
    }

    public void addBook(CengBook book, CengTreeNode cur) {
        if (cur instanceof CengTreeNodeLeaf) {
            CengTreeNodeLeaf t = (CengTreeNodeLeaf) cur;
            t.insertBook(book);
            if (t.bookCount() > 2 * CengTreeNode.order) {
                t.split(this);
            }
        } else if (cur instanceof CengTreeNodeInternal) {
            CengTreeNodeInternal t = (CengTreeNodeInternal) cur;
            if (book.getBookID() < t.keyAtIndex(0)) {
                this.addBook(book, t.childrenAtIndex(0));
                return;
            }
            for (int i = 0, n = t.keyCount(); i < n; ++i) {
                if (book.getBookID() >= t.keyAtIndex(i) && (book.getBookID() < t.keyAtIndex(i + 1) || i == n-1)) {
                    this.addBook(book, t.childrenAtIndex(i + 1));
                    return;
                }
            }
        } else {
            throw new IllegalStateException();
        }
    }

    public ArrayList<CengTreeNode> searchBook(Integer bookID)
    {
        ArrayList<CengTreeNode> res = new ArrayList<>();
        CengBook target = this.searchBook(bookID, this.root, res);
        if (target != null) {
            printPath(res, target);
            return res;
        }
        System.out.println("Could not find " + bookID + ".");
        return null;
    }

    public void printPath(ArrayList<CengTreeNode> res, CengBook target) {
        int level = 0;
        for (CengTreeNode cur: res) {
            String tab = "\t".repeat(level);
            if (cur instanceof CengTreeNodeInternal) {
                CengTreeNodeInternal t = (CengTreeNodeInternal) cur;
                System.out.println(tab + "<index>");
                for (int i = 0, n = t.keyCount(); i < n; ++i) {
                    System.out.println(tab + t.keyAtIndex(i));
                }
                System.out.println(tab + "</index>");
            } else if (cur instanceof CengTreeNodeLeaf) {
                System.out.print(tab + "<record>");
                System.out.print(target.getBookID() + "|");
                System.out.print(target.getBookTitle() + "|");
                System.out.print(target.getAuthor() + "|");
                System.out.print(target.getGenre());
                System.out.println("</record>");
            } else {
                throw new IllegalStateException();
            }
            ++level;
        }
    }

    public CengBook searchBook(Integer bookID, CengTreeNode cur, ArrayList<CengTreeNode> res) {
        res.add(cur);

        if (cur instanceof CengTreeNodeLeaf) {
            CengTreeNodeLeaf t = (CengTreeNodeLeaf) cur;
            return t.findByBookId(bookID);

        } else if (cur instanceof CengTreeNodeInternal) {
            CengTreeNodeInternal t = (CengTreeNodeInternal) cur;

            if (bookID < t.keyAtIndex(0)) {
                return searchBook(bookID, t.childrenAtIndex(0), res);
            }

            for (int i = 0, n = t.keyCount(); i < n; ++i) {
                if (bookID >= t.keyAtIndex(i) && (bookID < t.keyAtIndex(i + 1) || i == n-1)) {
                    return searchBook(bookID, t.childrenAtIndex(i + 1), res);
                }
            }

        } else {
            throw new IllegalStateException();
        }

        return null; // won't reach here
    }

    public void printTree()
    {
        printTree(this.root, 0);
    }

    public void printTree(CengTreeNode cur, int level) {
        String tab = "\t".repeat(level);
        if (cur instanceof CengTreeNodeLeaf) {
            CengTreeNodeLeaf t = (CengTreeNodeLeaf) cur;
            System.out.println(tab + "<data>");
            for (int i = 0, n = t.bookCount(); i < n; ++i) {
                CengBook book = t.bookAtIndex(i);
                System.out.print(tab);
                System.out.print("<record>");
                System.out.print(book.getBookID());
                System.out.print("|");
                System.out.print(book.getBookTitle());
                System.out.print("|");
                System.out.print(book.getAuthor());
                System.out.print("|");
                System.out.print(book.getGenre());
                System.out.println("</record>");
            }
            System.out.println(tab + "</data>");
        } else if (cur instanceof CengTreeNodeInternal) {
            CengTreeNodeInternal t = (CengTreeNodeInternal) cur;
            System.out.println(tab + "<index>");
            for (int i = 0, n = t.keyCount(); i < n; ++i) {
                System.out.println(tab + t.keyAtIndex(i));
            }
            System.out.println(tab + "</index>");
            for (int i = 0, n = t.keyCount() + 1; i < n; ++i) {
                printTree(t.childrenAtIndex(i), level + 1);
            }
        } else {
            throw new IllegalStateException();
        }
    }

}
