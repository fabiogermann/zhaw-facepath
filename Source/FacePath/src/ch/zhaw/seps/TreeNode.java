package ch.zhaw.seps;

import java.util.List;

public class TreeNode<T> {
    private T data;
    private TreeNode<T> parent;
    private List<TreeNode<T>> children;
}