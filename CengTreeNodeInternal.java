import java.util.ArrayList;

public class CengTreeNodeInternal extends CengTreeNode
{
    private ArrayList<Integer> keys; // n keys
    private ArrayList<CengTreeNode> children; // n+1 pointers

    public CengTreeNodeInternal(CengTreeNode parent)
    {
        super(parent);
        this.keys = new ArrayList<>();
        this.children = new ArrayList<>();
        this.type = CengNodeType.Internal;
    }

    // GUI Methods - Do not modify
    public ArrayList<CengTreeNode> getAllChildren()
    {
        return this.children;
    }
    public Integer keyCount()
    {
        return this.keys.size();
    }
    public Integer keyAtIndex(Integer index)
    {
        if(index >= this.keyCount() || index < 0)
        {
            return -1;
        }
        else
        {
            return this.keys.get(index);
        }
    }

    public Integer childrenCount() {
        return this.children.size();
    }

    public CengTreeNode childrenAtIndex(Integer index) {
        if (index >= this.childrenCount() || index < 0) {
            return null;
        }
        return this.children.get(index);
    }

    public void insertKey(Integer key) {
        this.keys.add(key);
        int n = this.keyCount();
        for (int i = n-1; i > 0; --i) {
            if (this.keyAtIndex(i) < this.keyAtIndex(i - 1)) {
                Integer temp = keys.get(i - 1);
                keys.set(i - 1, keys.get(i));
                keys.set(i, temp);
            }
        }
    }

    public void insertChild(CengTreeNode node) {
        this.children.add(node);
    }

    public void insertChildNextTo(CengTreeNode node, CengTreeNode nxt) {
        int index = 1;
        for (CengTreeNode p: this.getAllChildren()) {
            if (p.equals(nxt)) {
                break;
            }
            ++index;
        }
        if (index > this.childrenCount()) {
            this.children.add(node);
            return;
        }
        this.children.add(index, node);
    }

    public void split(CengTree tree) {
        CengTreeNodeInternal l2 = new CengTreeNodeInternal(this.getParent());
        int n = this.keyCount();
        Integer mid = this.keyAtIndex(n / 2);
        for (int i = n / 2 + 1; i < n; ++i) {
            l2.insertKey(this.keyAtIndex(i));
        }
        this.keys.subList(n / 2, n).clear();

        int c = this.childrenCount();
        for (int i = c / 2; i < c; ++i) {
            l2.insertChild(this.childrenAtIndex(i));
            this.childrenAtIndex(i).setParent(l2);
        }
        this.children.subList(c / 2, c).clear();

        if (this.getParent() == null) {
            CengTreeNodeInternal newRoot = new CengTreeNodeInternal(null);

            newRoot.insertKey(mid);
            newRoot.insertChild(this);
            newRoot.insertChild(l2);

            tree.root = newRoot;

            this.setParent(newRoot);
            l2.setParent(newRoot);

        } else {
            CengTreeNodeInternal parent = (CengTreeNodeInternal) this.getParent();

            parent.insertKey(mid);
            parent.insertChildNextTo(l2, this);

            if (parent.keyCount() > 2 * CengTreeNode.order) {
                parent.split(tree);
            }
        }
    }
}
