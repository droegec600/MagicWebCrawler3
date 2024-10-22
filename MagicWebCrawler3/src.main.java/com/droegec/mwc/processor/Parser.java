package com.droegec.mwc.processor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.droegec.mwc.DRKTools;
import com.droegec.mwc.ProgressListener;
import com.droegec.mwc.gui.MainWindow;
import com.droegec.mwc.model.WebTreeModel;
import com.droegec.mwc.model.WebTreeNode;

public abstract class Parser  implements Serializable {
	private static final long serialVersionUID = 4201834881149854020L;
	
	final protected WebTreeNode nodo;
	final protected WebTreeModel model;
        //static protected URL base;

 	public Parser(WebTreeNode nodo, WebTreeModel model) {
		this.nodo = nodo;
		this.model = model;
	}
	
	/**
	 * Ejecuta el parseo del nodo.
	 */
	public abstract void parse();
	
	/**
	 * Busca URLs en el contenido y las almacena en el nodo.
	 * Por defecto este método no hace nada, ya que no todos
	 * los contenidos contienen necesariamente URLs. 
	 */
	protected void findURLs() {
	}
	
	/**
	 * Compact path replacing // for /
	 * @param path
	 * @return compacted path
	 */
	static public String compactPath(String path) {
		return path.replaceAll("[^\\/]+\\/\\.\\.\\/", "");
	}

	/**
	 * Get charset name from content type string
	 * @param contentType string
	 * @return charset name
	 */
	static public String getCharsetFromContentType(String contentType) {
		String charset = contentType.substring(contentType.indexOf("charset=") + 8).replaceAll("[\\\"\\\']+", "");
		if ("korean".equalsIgnoreCase(charset))
			charset = "EUC_KR";
		return charset;
	}

	
	/**
	 * Devuelve el parser adecuado para el nodo.
	 * @param nodo
	 * @param model 
	 * @return parser adecuado, y por defecto NullParser()
	 */
	static public Parser getParserForNode(WebTreeNode nodo, WebTreeModel model) {
		
		Parser accParser = new NullParser(nodo, model);
		
		URL base = nodo.getURL();

		HttpURLConnection conn;
		try {
			conn = (HttpURLConnection)base.openConnection();
			conn.setRequestProperty("User-Agent", MainWindow.getUserAgent());
			conn.setInstanceFollowRedirects(DRKTools.prefs.getBoolean("follow_http_redirects", true));
			
			conn.connect();
			
			nodo.setContentType(conn.getContentType());
			nodo.setSize(conn.getContentLength());
			if (conn.getHeaderField("Last-Modified") != null)
				nodo.setLastModification(conn.getHeaderFieldDate("Last-Modified", 0));
			
			accParser = getParserForContentType(nodo, model);
			
		} catch (IOException e) {
			// Nada, si hay una excepción utilizo un parser nulo
			nodo.setStatus(400);
			nodo.setContentType("[error]");
		} catch (Exception e) {
			nodo.setStatus(-1);
			nodo.setContentType("[error]");
		}

		return accParser;
	}

	public static Parser getParserForContentType(WebTreeNode nodo, WebTreeModel model) {
		
		System.out.println ("node: " + nodo.toString());
		
		if (nodo.getContentType() != null) {
			if (nodo.getContentType().startsWith("text/html"))
				return new HTMLParser(nodo, model);
			else if (nodo.getContentType().startsWith("text/javascript"))
				return new JavascriptParser(nodo, model);
			else if (nodo.getContentType().startsWith("text/css")){
				return new StyleSheetParser(nodo, model);
			}	
		}
		return new NullParser(nodo, model);
	}

	/**
	 * Obtiene el texto fuente del nodo correspondiente.
	 * @return texto fuente del nodo, o null si no corresponde
	 */
	public String getSource(ProgressListener progress) {
		return null;
	}

	protected String nodeContent(ProgressListener progress) {
		
		String source = "";
		
		if (nodo.getContent() == null) {
	
			if (progress != null) {
				if (nodo.getSize() > 0)
					progress.setProgress(0, "Downloading content");
				else
					progress.setUnknownProgress(true, "Downloading content");
			}
			
			StringBuilder contentBuilder = new StringBuilder();
	
			URL base = nodo.getURL();
			
			try {
				
				// Si getParserMIMEType() es null, puede ser link a cualquier cosa
				
				if (getParserMIMEType() == null) {
					// Tipo desconocido
					if (base.getProtocol() != null && base.getProtocol().startsWith("http")) {
						HttpURLConnection conn = (HttpURLConnection)base.openConnection();
						conn.setRequestProperty("User-Agent", MainWindow.getUserAgent());
						conn.setInstanceFollowRedirects(DRKTools.prefs.getBoolean("follow_http_redirects", true));
						
						nodo.setContentType(conn.getContentType());
						nodo.setStatus(conn.getResponseCode());
					}
					else {
						URLConnection conn = base.openConnection();
						nodo.setContentType(conn.getContentType());
					}					
				}
				else {
					// Tipo conocido
					HttpURLConnection conn = (HttpURLConnection)base.openConnection();
					conn.setRequestProperty("User-Agent", MainWindow.getUserAgent());
					conn.setInstanceFollowRedirects(DRKTools.prefs.getBoolean("follow_http_redirects", true));
					
					String contentType = conn.getContentType();
                                        if (contentType == null) return null;
					if (contentType.matches(getParserMIMEType())) {
						String encoding = "utf-8";
						if (contentType.contains("charset="))
							encoding = Parser.getCharsetFromContentType(contentType);
						BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
						
						nodo.setStatus(conn.getResponseCode());
						
						if (nodo.getStatus() >= 300 && nodo.getStatus() < 400) {
							// Redirect
							nodo.setErrorInformation("Redirection -> "+conn.getHeaderField("Location"));
						}
						
						String line = null;
						while((line = in.readLine()) != null) {
							contentBuilder.append(line + "\n");
		
							if ((progress != null) && (nodo.getSize() > 0))
								progress.setProgress((float)contentBuilder.length()/(float)nodo.getSize(), "Downloading content for "+this.nodo.getURL().toString());
						}
		
						nodo.setSize(contentBuilder.length());
						
						source = contentBuilder.toString();
						
						if (model.isContentInMemory())
							nodo.setContent(contentBuilder.toString());
	
						finishedGetContent(source, true);
					}
				}				
				
			} catch (FileNotFoundException e) {
				nodo.setStatus(404);
				nodo.setErrorInformation("Not found");
			} 
			catch (IOException e) {
				nodo.setStatus(400);
				nodo.setErrorInformation(e.getLocalizedMessage());
			}
			
			if (progress != null) {
				progress.setProgress(0, "");
				progress.setUnknownProgress(false, "");
			}
	
		}
		else
			return nodo.getContent();
	
		return source;
	}
	
	/**
	 * MIME types for this parser
	 * @return a regular expression for matching mime types
	 */
	abstract public String getParserMIMEType();
	abstract public void finishedGetContent(String content, boolean ok);

}
