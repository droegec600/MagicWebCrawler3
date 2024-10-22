package com.droegec.mwc.model;

import java.io.Serializable;

public class MetaTag implements Serializable {
	private static final long serialVersionUID = 1608270244596089167L;
	
	private String name; 
	private String content; 
	private int start; 
	private int end; 

	public MetaTag(String name, String content, int start, int end) {
		this.name = name;
		this.content = content;
		this.start = start;
		this.end = end;
	}
	

	@Override
	public String toString() {
		return content;
	}


	public String getName() {
		return name;
	}


	public String getContent() {
		return content;
	}


	public int getStart() {
		return start;
	}


	public int getEnd() {
		return end;
	}

}
