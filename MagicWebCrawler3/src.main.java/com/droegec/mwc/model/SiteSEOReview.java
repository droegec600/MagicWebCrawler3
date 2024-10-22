package com.droegec.mwc.model;

public class SiteSEOReview {

	private int result;
	
	public int getResult() {
		return result;
	}

	public SiteSEOReview(PageSEOReview seoReview) {
		result = seoReview.getResult();
	}

	public void add(PageSEOReview seoReview) {
		result = (int)((double)(result + seoReview.getResult()) / 2);
	}

	public void add(SiteSEOReview deepSEOResult) {
		result = (int)((double)(result + deepSEOResult.getResult()) / 2);
	}

}
