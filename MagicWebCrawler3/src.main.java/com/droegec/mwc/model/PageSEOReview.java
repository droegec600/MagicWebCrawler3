package com.droegec.mwc.model;

import java.io.Serializable;
import java.util.Vector;

import com.droegec.mwc.processor.HTMLParser;
import org.apache.commons.lang3.StringUtils;

public class PageSEOReview implements Serializable {
	private static final long serialVersionUID = -3867137172281784986L;
	
	private Vector<KeywordInformation> keywords = new Vector<KeywordInformation>();
	private String review;
	private int result;

	public PageSEOReview(WebTreeNode nodo) {
		this(nodo, null);
	}
	
	public PageSEOReview(WebTreeNode nodo, String html) {
		String text;
		if (html == null) {
			html = nodo.getSource(null);
			text = nodo.getTextContent();
		}
		else {
			text = HTMLParser.getTextFromHTML(html);;
		}
		
		StringBuilder review_builder = new StringBuilder();
		
		// Title
		if (nodo.getTitle() != null) {
			int r = phraseRelevance(nodo.getTitle(), text);
			review_builder.append(qualityForPercent(r));
			review_builder.append(": Title relevancy to content is "+r+"%\n");
			result = r;
		}
		else {
			review_builder.append("BAD: There is no title!\n");
			result = 0;
		}

		// Description
		if (nodo.getMeta("description") != null) {
			int r = phraseRelevance(nodo.getMeta("description").toString(), text);
			review_builder.append(qualityForPercent(r));
			review_builder.append(": Description relevancy to content is "+r+"%\n");
			result = (result + r) / 2;
		}
		else {
			review_builder.append("BAD: There is no description!\n");
			result /= 2;
		}
		
		review  = review_builder.toString();
		
		// Count keywords repetition in text
		int matches = 0;
		int words = StringUtils.countMatches(text, " ");
		if (nodo.getMeta("keywords") != null) {
			String[] keys = nodo.getMeta("keywords").toString().split("\\s*,\\s*");
			for (int i = 0; i < keys.length; i++) {
				
				// Salteamos las palabras menores a tres letras
				if (keys[i].length() < 3)
					continue;
				
				int c = 0;
				int index = -1;
				while((index = text.indexOf(keys[i], index+1)) != -1) ++c;
				matches += (c>0)?1:0;
				int ratio = (int)((double)c / words * 1000);
				keywords.add(new KeywordInformation(keys[i], c, ratio));
			}
			result = (int)((double)result + ((double)matches/keys.length*100)) / 2;
		}
	}
	
	public String getReview() {
		return review;
	}

	public int getResult() {
		return result;
	}

	/**
	 * Relevance of this phrase in text.
	 * Searches every word (from phrase) in text and return 100% is every word matches at least once
	 * 
	 * @param phrase one or more words
	 * @param text a large text string
	 * @return percent 0-100
	 */
	protected int phraseRelevance(String phrase, String text) {
		int matches = 0;
		String[] words = phrase.split("[\\W]+");
		
		if (words.length < 1)
			return 0;
		
		for (String word : words) {
			if (text.indexOf(word) != -1)
				++matches;
		}
		
		return (int)((double)matches/words.length * 100);
	}

	protected String qualityForPercent(int r) {
		if (r >= 95)
			return "EXCELENT";
		else if (r >= 80)
			return "GOOD";
		else if (r >= 50)
			return "REGULAR";
		else
			return "BAD";
	}
	
	public Vector<KeywordInformation> getKeywordInformation() {
		return keywords;
	}
	
	/**
	 * Clases auxiliares
	 */
	
	public class KeywordInformation implements Serializable {
		private static final long serialVersionUID = -3063685828301034261L;
		
		public String keyword;
		public int count;
		public int ratio;
		public KeywordInformation(String k, int c, int ratio) {
			this.keyword = k;
			this.count = c;
			this.ratio = ratio;
		}
	}

}
