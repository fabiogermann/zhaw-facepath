package ch.zhaw.seps;

import java.util.List;

public class TreeNode<T> {
	public static class Node<T> {
        private T data;
        private String key;
        private TreeNode<T> parent;
        private List<TreeNode<T>> children;
        
        public void TreeNode( String key, T data) {
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
        
        public void getData() {
    		// TODO Auto-generated method stub
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
}
