package com.droegec.mwc.model;

public class NodeTextLocation {

	final public WebTreeNode node;
	final public int start;
	final public int end;
	
	public NodeTextLocation(WebTreeNode node, int start, int end) {
		this.node = node;
		this.start = start;
		this.end = end;
	}

	@Override
	public String toString() {
		return node.getURL().toExternalForm()+" at position "+start+" ("+(end-start)+" characters)";
	}
}
