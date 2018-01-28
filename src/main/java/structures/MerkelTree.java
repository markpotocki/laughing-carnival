package structures;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


public final class MerkelTree<E extends Serializable> implements Serializable {

    public final int MAGIC_NUMBER = 0xcdaace99;
    public final int INT_BYTES = 4;
    public final int LONG_BYTES = 8;
    public final byte INTERNAL_NODE_SIG = 0x01;
    public final byte LEAF_NODE_SIG = 0x0;

    private Node root;
    private List<E> sigs;
    private int nnodes;
    private int depth;

    public MerkelTree(List<E> leafSignitures) {
        constructTree(leafSignitures);
    }
/*
    @Deprecated
    public byte[] serialize() {
        int magicHdrLength = INT_BYTES;
        int nnodesLength = INT_BYTES;
        int headerSize = magicHdrLength + nnodesLength;

        int typeByteSize = 1;
        int sigLength = INT_BYTES;

        int parentSigLength = LONG_BYTES;

        int leafSigSize = ByteBuffer.allocate(4).putInt(sigs.get(0)).array().length;

        int maxSigSize = leafSigSize;
        if(parentSigLength > maxSigSize) {
            maxSigSize = parentSigLength;
        }

        int spaceForNodes = (typeByteSize + sigLength + maxSigSize) * nnodes;

        int cap = headerSize + spaceForNodes;
        ByteBuffer buffer = ByteBuffer.allocate(cap);

        buffer.putInt(MAGIC_NUMBER).putInt(nnodes);
        serializeBreadthFirst(buffer);

        byte[] serializedTree = new byte[buffer.position()];
        buffer.rewind();
        buffer.get(serializedTree);
        return serializedTree;

    }

    @Deprecated
    private void serializeBreadthFirst(ByteBuffer buffer) {
        Queue<Node> foo = new ArrayDeque<Node>((nnodes / 2) -1);
        foo.add(root);

        while(!foo.isEmpty()) {
            Node nd = foo.remove();
            buffer.put(nd.type).putInt(nd.toBytes().length).put(nd.toBytes());

            if (nd.hasLeftChild())
                foo.add(nd.left);
            if (nd.hasRightChild())
                foo.add(nd.right);
        }
    }
*/

    public Integer getRoot() {
        return this.root.hashData.hashCode();
    }

    void constructTree(List<E> sigs) {
        if (sigs.size() < 2)
            throw new IllegalStateException("Need atleast 2 signitures.");

        this.sigs = sigs;
        this.nnodes = sigs.size();
        List<Node> parents = constructBottomLayer(sigs);
        nnodes += parents.size();
        this.depth = 1;

        while (parents.size() > 1) {
            parents = constructInternalLayer(parents);
            depth++;
            nnodes += parents.size();
        }

        root = parents.get(0);
    }

    private List<Node> constructInternalLayer(List<Node> children) {
        List<Node> parents = new ArrayList<Node>();

        for (int i = 0; i < children.size() -1; i += 2) {
            Node child1 = children.get(i);
            Node child2 = children.get(i+1);

            Node parent = constructInternalNode(child1, child2);
            parents.add(parent);
        }

        if (children.size() % 2 != 0) {
            Node child = children.get(children.size() - 1);
            Node parent = constructInternalNode(child, null);
            parents.add(parent);
        }

        return parents;
    }

    private List<Node> constructBottomLayer(List<E> sigs) {
        List<Node> parents = new ArrayList<Node>();

        for (int i = 0; i < sigs.size() -1; i += 2) {
            Node l = constructLeafNode(sigs.get(i));
            Node r = constructLeafNode(sigs.get(i + 1));

            Node parent = constructInternalNode(l, r);
            parents.add(parent);
        }

        // odd case
        if (sigs.size() % 2 != 0) {
            Node leaf = constructLeafNode(sigs.get(sigs.size() -1));
            Node parent = constructInternalNode(leaf, null);
            parents.add(parent);
        }

        return parents;
    }

    // constructors for internal and leaf nodes
    private Node constructLeafNode(E sig) {
        Node leaf= new Node(SerializationUtils.serialize(sig), LEAF_NODE_SIG);
        return leaf;
    }

    private Node constructInternalNode(Node lchild, Node rchild) {
        Node internal = new Node(INTERNAL_NODE_SIG, lchild, rchild);
        return internal;
    }

    static class Node implements Serializable{

        final byte[] hashData;
        final byte type;
        final Node left;
        final Node right;

        // constructor for internal nodes
        Node(byte type, Node...nodes) {
            if (nodes.length > 2) {
                throw new UnsupportedOperationException("Tried to construct a node with " + nodes.length + "child nodes. Only binary Merkel Trees are supported right now.");
            }
            else if (nodes[1] != null) {
                this.left = nodes[0];
                this.right = nodes[1];
                this.hashData = toBytes(right.hashData.hashCode() + left.hashData.hashCode());
            }
            else if (nodes[0] != null) {
                this.left = nodes[0];
                this.right = null;
                this.hashData = left.hashData;
            }
            else {
                throw new Error("This is not an internal node. The types will be incompatible.");
            }
            this.type = type;
        }

        // constructor for leaf nodes
        Node(byte[] hashData, byte type) {
            this.hashData = hashData;
            this.type = type;
            this.left = null;
            this.right = null;
        }

        boolean hasLeftChild() {
            return left != null;
        }

        boolean hasRightChild() {
            return right != null;
        }

        byte[] toBytes(int foobar) {
            return ByteBuffer.allocate(4).putInt(foobar).array();
        }

        @Override
        public String toString() {
            String leftType = "<null>";
            String rightType = "<null>";
            if (left != null) {
                leftType = String.valueOf(left.type);
            }
            if (right != null) {
                rightType = String.valueOf(right.type);
            }
            return String.format("MerkleTree.Node<type:%d, sig:%s, left (type): %s, right (type): %s>",
                    type, hashData, leftType, rightType);
        }


    }

}

