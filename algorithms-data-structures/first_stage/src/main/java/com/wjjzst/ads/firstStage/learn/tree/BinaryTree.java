package com.wjjzst.ads.firstStage.learn.tree;


import java.util.LinkedList;
import java.util.Queue;

import com.wjjzst.ads.firstStage.learn.common.printer.BinaryTreeInfo;
import com.wjjzst.ads.firstStage.learn.stack.Stack;

/**
 * @Author: Wjj
 * @Date: 2019/5/6 12:17
 */
public class BinaryTree<E> implements BinaryTreeInfo {
    protected int size;
    protected Node<E> root;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = null;
        size = 0;
    }


    /**
     * 前序遍历
     */
    public void preorderTraversal() {
        preorderTraversal(root);
    }


    private void preorderTraversal(Node<E> node) {
        if (node == null) {
            return;
        }
        System.out.println(node.element);
        preorderTraversal(node.left);
        preorderTraversal(node.right);
    }


    //非递归方式
    public void preorder(Visitor<E> visitor) {
        if (visitor == null || root == null) {
            return;
        }
        Node<E> node = root;
        Stack<Node<E>> stack = new Stack<>(); // 装所有的右子节点
        while (true) {
            if (node != null) {

                // 访问node节点
                if (visitor.visit(node.element)) {
                    return;
                }
                // 将右字节点入栈
                if (node.right != null) {
                    stack.push(node.right);
                }
                // 往左走
                node = node.left;
                //如果 左节点为空了 且stack 都空了 那么直接停止遍历
            } else if (stack.isEmpty()) {
                return;
                //如果 左节点为空了 且stack 不为空 那么可以将栈顶的node.right 接着走之前的逻辑
            } else {
                node = stack.pop();
            }
        }

    }

    //非递归方式2
    public void preorder2(Visitor<E> visitor) {
        if (visitor == null || root == null) {
            return;
        }
        Stack<Node<E>> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node<E> node = stack.pop();
            // 父节点弹出的时候就访问了
            if (visitor.visit(node.element)) {
                return;
            }
            // 压进去先右后左 出队就是先做后右 前序遍历是先右后左
            if (node.right != null) {
                stack.push(node.right);
            }
            if (node.left != null) {
                stack.push(node.left);
            }

        }

    }

    //递归方式
    public void preorder1(Visitor<E> visitor) {
        if (visitor == null) {
            return;
        }
        preorder(root, visitor);

    }

    private void preorder(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor.stop) {
            return;
        }
        if (visitor.stop) {
            return;
        }
        visitor.stop = visitor.visit(node.element);
        preorder(node.left, visitor);
        preorder(node.right, visitor);
    }

    /**
     * 中序遍历
     */
    public void inorderTraversal() {
        inorderTraversal(root);
    }


    private void inorderTraversal(Node<E> node) {
        if (node == null) {
            return;
        }
        inorderTraversal(node.left);
        System.out.println(node.element);
        inorderTraversal(node.right);
    }

    //非递归方式
    public void inorder(Visitor<E> visitor) {
        if (visitor == null || root == null) {
            return;
        }
        Node<E> node = root;
        Stack<Node<E>> stack = new Stack<>(); // 装所有的左子节点
        while (true) {
            if (node != null) {
                stack.push(node);
                node = node.left;
            } else if (stack.isEmpty()) {
                return;
            } else {
                node = stack.pop();
                //访问node节点
                if (visitor.visit(node.element)) {
                    return;
                }
                // 让右节点进行中序遍历
                node = node.right;
            }
        }
    }


    //递归方式
    public void inorder1(Visitor<E> visitor) {
        if (visitor == null) {
            return;
        }
        inorder(root, visitor);
    }


    private void inorder(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor.stop) {
            return;
        }
        inorder(node.left, visitor);
        if (visitor.stop) {
            return;
        }
        visitor.stop = visitor.visit(node.element);
        inorder(node.right, visitor);
    }

    /**
     * 后序遍历
     */
    public void postorderTraversal() {
        postorderTraversal(root);
    }


    private void postorderTraversal(Node<E> node) {
        if (node == null) {
            return;
        }
        postorderTraversal(node.left);
        postorderTraversal(node.right);
        System.out.println(node.element);
    }

    //如果上次弹出的是当前弹出的子节点 该节点虽然有子节点但是不入栈
    public void postorder(Visitor<E> visitor) {
        if (visitor == null || root == null) {
            return;
        }
        // 用来记录上一次弹出访问的节点
        Node<E> prev = null;
        Stack<Node<E>> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node<E> top = stack.peek();
            if (top.isLeaf() || (prev != null && prev.parent == top)) {
                prev = stack.pop();
                //访问node节点
                if (visitor.visit(prev.element)) {
                    return;
                }
            } else {
                if (top.right != null) {
                    stack.push(top.right);
                }
                if (top.left != null) {
                    stack.push(top.left);
                }
            }

        }
    }

    public void postorder1(Visitor<E> visitor) {
        if (visitor == null) {
            return;
        }
        postorder(root, visitor);
    }


    private void postorder(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor.stop) {
            return;
        }
        postorder(node.left, visitor);
        postorder(node.right, visitor);
        if (visitor.stop) {
            return;
        }
        visitor.stop = visitor.visit(node.element);
    }

    public void levelOrderTraversal() {
        /*if (root == null) {
            return;
        }
        BaseQueue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            System.out.println(node.element);
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }*/
        levelOrder(new Visitor<E>() {
            @Override
            public boolean visit(E element) {
                System.out.println(element);
                return false;
            }
        });

    }

    public void levelOrder(Visitor<E> visitor) {
        if (root == null) {
            return;
        }
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            if (visitor.visit(node.element)) {
                return;
            }
            //层序遍历是先左入队再右入队
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
    }

    // 找前驱节点
    protected Node<E> predesessor(Node<E> node) {
        if (node == null) {
            return null;
        }
        // 前驱节点在左子树当中(left.right.right.right.right...)
        Node<E> p = node.left;
        if (p != null) {
            while (p.right != null) {
                p = p.right;
            }
            return p;
        }
        // 从父节点,祖父节点中寻找前驱节点
        while (node.parent != null && node == node.parent.left) {
            node = node.parent;
        }
        // node = node.parent.right 跳出来
        return node.parent;
    }

    // 找后驱节点
    protected Node<E> successor(Node<E> node) {
        if (node == null) {
            return null;
        }
        // 后驱节点在左子树当中(left.right.right.right.right...)
        Node<E> p = node.right;
        if (p != null) {
            while (p.left != null) {
                p = p.left;
            }
            return p;
        }
        // 从父节点,祖父节点中寻找后驱节点
        while (node.parent != null && node == node.parent.right) {
            node = node.parent;
        }
        // node = node.parent.left 跳出来
        return node.parent;
    }

    /**
     * 树的高度
     *
     * @return
     */

    public int height() {
        if (root == null) {
            return 0;
        }
        int height = 0;
        //存储着每一层的元素数量
        int levelSize = 1; //默认第一层有1个 跟不为空
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            levelSize--;
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
        if (levelSize == 0) {
            levelSize = queue.size();
            height++;
        }
        return height2(root);
    }

    /**
     * 任何一个节点的高度
     *
     * @param node
     * @return
     */
    public int height2(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(height2(node.left), height2(node.right));
    }

    public boolean isComplete() {
        if (root == null) {
            return false;
        }
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        boolean leaf = false; //要求后面节点是叶子的标志
        // 保证层序节点所有的都能遍历到
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            if (leaf && !node.isLeaf()) {
                return false;
            }
            if (node.left != null) {
                queue.offer(node.left);
            } else if (node.right != null) { //左空右不空
                // node.left == null && node.right != null;
                return false;
            }

            if (node.right != null) {
                queue.offer(node.right);
            } else {
                // node.left != null && node.right == null;
                // node.left == null && node.right == null;
                leaf = true;
            }
        }
        return true;
    }

    public boolean isComplete1() {
        if (root == null) {
            return false;
        }
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        boolean leaf = false; //要求后面节点是叶子的标志
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            if (leaf && !node.isLeaf()) {
                return false;
            }
            if (node.hasTwoChildren()) {
                queue.offer(node.left);
                queue.offer(node.right);
            } else if (node.left == null && node.right != null) {
                return false;
            } else {  //后面的节点都应该全是叶子节点 如果不是叶子节点则返回false
                leaf = true;
                if (node.left != null) {
                    queue.offer(node.left);
                }
            }
        }
        return true;
    }

    @Override
    public Object root() {
        return root;
    }

    @Override
    public Object left(Object node) {
        return ((Node<E>) node).left;
    }

    @Override
    public Object right(Object node) {
        return ((Node<E>) node).right;
    }

    @Override
    public Object string(Object node) {
        StringBuilder sb = new StringBuilder();
        sb.append(((Node<E>) node).element).append("_p(");
        if (((Node<E>) node).parent == null) {
            sb.append("null");
        } else {
            sb.append(((Node<E>) node).parent.element);
        }
        sb.append(")");
        return sb.toString();
    }


    public static abstract class Visitor<E> {
        boolean stop;

        protected abstract boolean visit(E element);
    }

    protected Node<E> createNode(E element, Node<E> parent) {
        return new Node<>(element, parent);
    }

    protected static class Node<E> {
        protected E element;
        protected Node<E> left;
        protected Node<E> right;
        protected Node<E> parent;

        public Node(E element, Node<E> parent) {
            this.element = element;
            this.parent = parent;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public boolean hasTwoChildren() {
            return left != null && right != null;
        }

        //判断自己是不是父节点的左子树
        public boolean isLeftChild() {
            return parent != null && this == parent.left;
        }

        //判断自己是不是父节点的左子树
        public boolean isRightChild() {
            return parent != null && this == parent.right;
        }

        // 获取兄弟节点
        public Node<E> sibling() {
            if (isLeftChild()) {
                return parent.right;
            } else if (isRightChild()) {
                return parent.left;
            } else {
                return null;
            }
        }
    }

}
