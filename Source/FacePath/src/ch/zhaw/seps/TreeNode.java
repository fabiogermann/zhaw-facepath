package ch.zhaw.seps;

import java.util.List;

import ch.zhaw.seps.fb.FacebookProfile;

public class TreeNode<T> {
    private T data;
    private String key;
    private TreeNode<T> parent;
    private List<TreeNode<T>> children;

	public TreeNode(String key, T data) {
    	this.data = data;
    	this.key = key;
    }
    
    public void addChild(TreeNode<T> node) {
		// TODO Auto-generated method stub
	}
    
    public void removeChild(TreeNode<T> node) {
		// TODO Auto-generated method stub
	}
    
    public void getChildByKey(String key) {
		// TODO Auto-generated method stub
	}
    
    public T getData() {
		return this.data;
	}
    
    public void setData() {
		// TODO Auto-generated method stub
	}
    
    public void setKey() {
		// TODO Auto-generated method stub
	}
    
    public void getKey() {
		// TODO Auto-generated method stub
	}
}
