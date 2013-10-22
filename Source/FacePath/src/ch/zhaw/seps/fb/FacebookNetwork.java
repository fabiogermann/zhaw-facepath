package ch.zhaw.seps.fb;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import ch.zhaw.seps.TreeNode;

public class FacebookNetwork {
	// Datastructure for the simulation of the facebook network
	
    private TreeNode<FacebookProfile> root;
    private TreeNode<FacebookProfile> currentRootChild;
    private Stack<TreeNode<FacebookProfile>> rootStack;
    private List<TreeNode<FacebookProfile>> rootChildren;
    private HashMap<String,FacebookProfile> rootDatacollection;
    
    private TreeNode<FacebookProfile> target;
    private TreeNode<FacebookProfile> currentTargetChild;
    private Stack<TreeNode<FacebookProfile>> targetStack;
    private List<TreeNode<FacebookProfile>> targetChildren;
    private HashMap<String,FacebookProfile> targetDatacollection;
    
    public void insertItem() {
		// TODO Auto-generated method stub
	}
    
    public void getItem() {
		// TODO Auto-generated method stub
	}

}
