package com.droegec.mwc.model;

public interface WebTreeNodeListener {

	public void nodeAdded(WebTreeNode source, Object[] path, 
			int[] childIndices, 
			WebTreeNode[] children);
}
