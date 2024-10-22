package com.droegec.mwc.model.helpers;

import java.io.Serializable;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SerializableCookieStore implements CookieStore, Serializable {
	private static final long serialVersionUID = -1655756528985025966L;
	
	private Map<URI, Set<HttpCookie>> data = new HashMap<URI, Set<HttpCookie>>();
	private Set<HttpCookie> orphan = new HashSet<HttpCookie>();

	@Override
	public void add(URI uri, HttpCookie cookie) {
		purge();
		if (uri == null)
			orphan.add(cookie);
		else {
			if (!data.containsKey(uri))
				data.put(uri, new HashSet<HttpCookie>());
			data.get(uri).add(cookie);
		}
	}

	@Override
	public List<HttpCookie> get(URI uri) {
		purge();
		if (uri == null)
			throw new NullPointerException();
		
		List<HttpCookie> cookies = (data.containsKey(uri)?(new LinkedList<HttpCookie>(data.get(uri))):new LinkedList<HttpCookie>());
		return Collections.unmodifiableList(cookies);
	}

	@Override
	public List<HttpCookie> getCookies() {
		purge();
		Set<HttpCookie> allCookies = new HashSet<HttpCookie>(orphan);
		for (URI uri : data.keySet()) {
			allCookies.addAll(data.get(uri));
		}
		return Collections.unmodifiableList(new LinkedList<HttpCookie>(allCookies));
	}

	@Override
	public List<URI> getURIs() {
		purge();
		return Collections.unmodifiableList(new LinkedList<URI>(data.keySet()));
	}

	@Override
	public boolean remove(URI uri, HttpCookie cookie) {
		purge();
		if (uri == null)
			return orphan.remove(cookie);
		else {
			if (!data.containsKey(uri))
				return false;
			return data.get(uri).remove(cookie);
		}
	}

	@Override
	public boolean removeAll() {
		boolean change = !orphan.isEmpty() || !data.isEmpty();
		orphan =  new HashSet<HttpCookie>();
		data = new HashMap<URI, Set<HttpCookie>>();
		return change;
	}

	/**
	 * Remove expired cookies
	 */
	private void purge() {
		List<HttpCookie> toRemove = new LinkedList<HttpCookie>();
		for (HttpCookie httpCookie : orphan) {
			if (httpCookie.hasExpired())
				toRemove.add(httpCookie);
		}
		orphan.removeAll(toRemove);
		
		for (URI uri : data.keySet()) {
			toRemove.clear();
			for (HttpCookie httpCookie : data.get(uri)) {
				if (httpCookie.hasExpired())
					toRemove.add(httpCookie);
			}
			data.get(uri).removeAll(toRemove);
		}		
	}

}
