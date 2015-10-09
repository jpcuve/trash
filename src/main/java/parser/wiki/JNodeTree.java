package parser.wiki;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class JNodeTree extends JTree {
    private final Node node;

    public class Wrapper{
        private final Node node;

        public Wrapper(Node node) {
            this.node = node;
        }

        public String toString() {
            return String.format("%s %s %s", node.getNodeType(), node.getNodeName(), node.getNodeValue() != null ? node.getNodeValue() : "");
        }
    }

    public JNodeTree(Node node) {
        this.node = node;
        DefaultMutableTreeNode tn = new DefaultMutableTreeNode(new Wrapper(node));
        recurseChildren(tn);
        setModel(new DefaultTreeModel(tn));
        for (int i = 0; i < getRowCount(); i++) expandRow(i);
    }

    private void recurseChildren(final DefaultMutableTreeNode tnParent){
        final Node node = ((Wrapper) tnParent.getUserObject()).node;
        final NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++){
            final DefaultMutableTreeNode tnChild = new DefaultMutableTreeNode(new Wrapper(childNodes.item(i)));
            tnParent.add(tnChild);
            recurseChildren(tnChild);
        }
    }

    public static void openFrame(final Node node){
        final JFrame fTree = new JFrame();
        fTree.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fTree.add(new JScrollPane(new JNodeTree(node)));
        fTree.pack();
        fTree.setVisible(true);
    }
}
