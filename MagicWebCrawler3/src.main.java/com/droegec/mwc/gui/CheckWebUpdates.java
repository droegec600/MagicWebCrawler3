package com.droegec.mwc.gui;

import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.droegec.mwc.DRKTools;
import com.droegec.mwc.processor.Parser;

public class CheckWebUpdates extends SwingWorker<List<String>, Void> {
	
	final private MainWindow main;
	
	public CheckWebUpdates(MainWindow main) {
		super();
		this.main = main;
	}

	@Override
	protected List<String> doInBackground() throws Exception {
		
		List<String> list =  new LinkedList<String>();
		
		URL url = new URL(MainWindow.UPDATE_URL);
		
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestProperty("User-Agent", MainWindow.getUserAgent());
		
		String contentType = conn.getContentType();
		if (contentType.startsWith("text/plain")) {
			String encoding = "utf-8";
			if (contentType.contains("charset="))
				encoding = Parser.getCharsetFromContentType(contentType);
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
			
			if (conn.getResponseCode() >= 400)
				return null;
			
			String line = null;
			while((line = in.readLine()) != null) {
				list.add(line);
			}
		}
	
		return list;
	}

	@Override
	protected void done() {
		List<String> list = null;
		try {
			list = get();
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (list != null && !list.isEmpty() && MainWindow.APP_NAME_AND_VERSION.compareTo(list.get(0)) < 0) {
			String title = "New "+list.get(0)+" available...";
			StringBuilder news = new StringBuilder("What's new:\n\n");
			for (int i = 1; i < list.size(); i++) {
				news.append(list.get(i) + "\n");
			}
			news.append("\nDo you want to open the download page?");
			if (JOptionPane.showConfirmDialog(main, news.toString(), title, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION) {
				DRKTools.showInBrowser(MainWindow.DOWNLOAD_URL, null);
			}
		}
		
		super.done();
	}

}
