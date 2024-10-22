package com.droegec.mwc.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.droegec.mwc.exceptions.RobotsNotFoundException;
import com.droegec.mwc.gui.MainWindow;
import com.droegec.mwc.processor.Parser;

public class Robots implements Serializable {
	private static final long serialVersionUID = -8930313137141030899L;
	
	private List<String> disallow = new LinkedList<String>();
	private List<String> allow = new LinkedList<String>();

	public Robots(URL site_base) throws IOException, RobotsNotFoundException {

		URL robots = new URL(site_base.getProtocol(), site_base.getHost(), site_base.getPort(), "/robots.txt");

		HttpURLConnection conn = (HttpURLConnection)robots.openConnection();
		conn.setRequestProperty("User-Agent", MainWindow.getUserAgent());
		
		conn.connect();
		
		if (conn.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
			throw new RobotsNotFoundException();
		}
				
		String contentType = conn.getContentType();
		
		if (contentType == null)
			throw new IOException("Server response without Content-Type field");
		
		if (contentType.startsWith("text/plain")) {
			String encoding = "utf-8";
			if (contentType.contains("charset="))
				encoding = Parser.getCharsetFromContentType(contentType);
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));

			String line = null;
			boolean load_rules = false, rules_started = false;
			boolean reprocess_this_line = false;
			while (reprocess_this_line || (line = in.readLine()) != null) {

				reprocess_this_line = false;

				// Eliminamos comentarios
				line = line.replaceFirst("[\\s]+#.*$", "");
				if (line.equals(""))
					continue;

				if (!rules_started) {
					// Es un bloque User-agent:
					if (line.toLowerCase().startsWith("user-agent:")) {
						// User-agent:
						load_rules |= line.toLowerCase().matches("user\\-agent\\:[\\s]+\\*");
					} else {
						rules_started = true;
						reprocess_this_line = true;
					}
				} else {
					// Es un bloque de reglas
					if (load_rules) {
						if (line.toLowerCase().startsWith("disallow:")) {
							// Disallow:
							disallow.add(line.substring(9).trim());
						} else if (line.toLowerCase().startsWith("allow:")) {
							// Allow:
							allow.add(line.substring(6).trim());
						} else {
							// Terminó un bloque de relgas
							rules_started = false;
							reprocess_this_line = true;
							load_rules = false;
						}
					}
				}
			}
			
			/* Las convertimos en inmutables para evitar problemas de concurrencia
			 * en la lectura
			 */
			allow = Collections.unmodifiableList(allow);
			disallow = Collections.unmodifiableList(disallow);
		}
	}

	public boolean canAccess(URL url) {
		String path = url.getPath();
		for (String block : disallow) {
			if (path.startsWith(block)) {
				for (String pass : allow) {
					if (path.startsWith(pass))
						return true;
				}
				return false;
			}
		}
		
		// Si no coincide con algún disallow, puede pasar 
		return true;
	}
}
