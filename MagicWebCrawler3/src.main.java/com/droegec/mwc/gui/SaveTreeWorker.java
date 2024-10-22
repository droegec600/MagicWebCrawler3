package com.droegec.mwc.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.event.TreeModelListener;

import com.droegec.mwc.model.WebTreeModel;

public class SaveTreeWorker extends SwingWorker<Object, Object> {

	private File selectedFile;
	private MainWindow wnd;
	private String error = null;
	private WebTreeModel document;

	public SaveTreeWorker(MainWindow wnd, File selectedFile) {
		this.wnd = wnd;
		this.selectedFile = selectedFile;
        this.document = wnd.getDocument();
	}

	@Override
	protected void done() {
		if (error != null)
			JOptionPane.showMessageDialog(wnd, error, "Fatal", JOptionPane.ERROR_MESSAGE);

		wnd.guiContinue();
		super.done();
	}

	@Override
	protected Object doInBackground() throws Exception {
		try {
			
			FileOutputStream fos = new FileOutputStream(selectedFile.getCanonicalPath());
	        ObjectOutputStream oos = new ObjectOutputStream(fos);

			TreeModelListener[] all = document.prepareForSerialization();
	        oos.writeObject(document);
	        document.recoverAfterSerialization(all);

	        oos.close();
	        
		} catch (FileNotFoundException e1) {
			error = "Can't create file "+selectedFile.getAbsolutePath();
		} catch (IOException e1) {
			error = "I/O Error writing "+selectedFile.getAbsolutePath();
		}
		return null;
	}

}
