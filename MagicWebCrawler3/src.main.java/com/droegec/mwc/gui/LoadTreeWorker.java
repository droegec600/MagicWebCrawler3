package com.droegec.mwc.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.droegec.mwc.model.WebTreeModel;

public class LoadTreeWorker extends SwingWorker<WebTreeModel, Object> {
	
	private File selectedFile;
	private MainWindow wnd;
	private String error = null;

	public LoadTreeWorker(MainWindow wnd, File selectedFile) {
		this.wnd = wnd;
		this.selectedFile = selectedFile;
	}

	@Override
	protected void done() {
		if (error != null)
			JOptionPane.showMessageDialog(wnd, error, "Fatal", JOptionPane.ERROR_MESSAGE);
		else {
			try {
				wnd.setDocument(get());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		wnd.guiContinue();
		super.done();
	}

	@Override
	protected WebTreeModel doInBackground() throws Exception {
		
		wnd.guiPause();
		WebTreeModel doc = null;
		
		try {
			
			FileInputStream fos = new FileInputStream(selectedFile.getCanonicalPath());
	        ObjectInputStream oos = new ObjectInputStream(fos);
	        doc = (WebTreeModel) oos.readObject();
	        oos.close();
	        
	        
		} catch (FileNotFoundException e1) {
			error  = "File not found looking for "+selectedFile.getAbsolutePath();
		} catch (IOException e1) {
			error  = "I/O Error reading "+selectedFile.getAbsolutePath();
		} catch (ClassNotFoundException e1) {
			error  = "Invalid file reading "+selectedFile.getAbsolutePath();
		}
		
		return doc;
	}

}
