package com.droegec.mwc.model.exporters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.droegec.mwc.model.DRKLink;
import com.droegec.mwc.model.WebTreeNode;

public abstract class Exporter {
	protected final WebTreeNode root;
	protected final List<DRKLink> linkList;
	protected final List<WebTreeNode> nodeList;
	
	public Exporter(WebTreeNode root) {
		this.root = root;
		this.linkList = null;
		this.nodeList = null;
	}
	public Exporter(List<DRKLink> linkList, List<WebTreeNode> nodeList) {
		this.root = null;
		this.linkList = linkList;
		this.nodeList = nodeList;
	}
	
	public void exportSitemap(String filename) throws IOException {
		File f = new File(filename);
		f.createNewFile();
		BufferedWriter buffer = new BufferedWriter(new FileWriter(f));
		exportNodeSitemap(buffer);
		buffer.close();
	}
	
	public void exportLinks(String filename) throws IOException {
		File f = new File(filename);
		f.createNewFile();
		BufferedWriter buffer = new BufferedWriter(new FileWriter(f));
		exportNodeLinks(buffer);
		buffer.close();
	}
	
	public void exportLinksFirstLevel(String filename) throws IOException {
		File f = new File(filename);
		f.createNewFile();
		BufferedWriter buffer = new BufferedWriter(new FileWriter(f));
		exportLinksFirstLevel(buffer);
		buffer.close();
	}
	
	public void exportErrors(String filename) throws IOException {
		File f = new File(filename);
		f.createNewFile();
		BufferedWriter buffer = new BufferedWriter(new FileWriter(f));
		exportNodeErrors(buffer);
		buffer.close();
	}
	
	public void exportNodesFirstLevel(String filename) throws IOException {
		File f = new File(filename);
		f.createNewFile();
		BufferedWriter buffer = new BufferedWriter(new FileWriter(f));
		exportNodesFirstLevel(buffer);
		buffer.close();
	}
	
	abstract public void exportNodeSitemap(BufferedWriter buffer) throws IOException;
	abstract public void exportNodeLinks(BufferedWriter buffer) throws IOException;
	abstract public void exportLinksFirstLevel(BufferedWriter buffer) throws IOException;
	abstract public void exportNodeErrors(BufferedWriter buffer) throws IOException;
	abstract public void exportNodesFirstLevel(BufferedWriter buffer) throws IOException;
}
