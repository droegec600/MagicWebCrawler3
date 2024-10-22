package com.droegec.mwc.model;

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DRKLink implements Serializable{
	private static final long serialVersionUID = 4970763789809914397L;
	
	final private URL url;
	final private URL parent;
	final private boolean nofollow;
	final private WebTreeNode parentNode;
	final private int start;
	final private int end;
	final private String anchor;
	final private String tag;
	final private Map<String, String> attributes;
	private WebTreeNode node = null;
	private int depth;

	public DRKLink(String tag, URL url, WebTreeNode parentNode, int start, int end) {
		this(tag, "", url, null, parentNode, start, end);
	}
	
	public DRKLink(String tag, String anchor, URL url, Map<String, String> attributes, WebTreeNode parentNode, int start, int end) {
		this.anchor = anchor;
		this.tag = tag;
		this.url = url;
		this.parent = parentNode.getURL();
		this.attributes = (attributes != null)?attributes:new HashMap<String, String>();
		this.parentNode = parentNode;
		this.start = start;
		this.end = end;
		this.nofollow = (this.attributes.get("rel") != null && "nofollow".equals(this.attributes.get("rel").toLowerCase()));
	}

	public WebTreeNode getNode() {
		return node;
	}
	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public void setNode(WebTreeNode node) {
		this.node = node;
		this.node.addSourceLink(this);
	}

	public WebTreeNode getParentNode() {
		return parentNode;
	}

	public boolean isExternal() {
		return !parent.getHost().equals(url.getHost());
	}
	public boolean isNoFollow() {
		return nofollow;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DRKLink) {
			DRKLink olink = (DRKLink) obj;
			return this.url.equals(olink.url);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.url.hashCode();
	}

	@Override
	public String toString() {
		return this.url.toString()+" ("+anchor+")";
	}
	
	public URL getURL() {
		return url;
	}
	
	public Map<String, String> getAttributes() {
		return attributes;
	}

	public String getTag() {
		return tag;
	}

	public String getAnchor() {
		return anchor;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public int getDepth() {
		return depth;
	}
}
