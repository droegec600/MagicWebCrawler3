package com.droegec.mwc.gui.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.droegec.mwc.DRKTools;
import com.droegec.mwc.gui.MainWindow;
import com.droegec.mwc.gui.SaveTreeWorker;

public class TreeSave extends AbstractSpiderAction {
	private static final long serialVersionUID = 8993403401399572268L;

	public TreeSave(MainWindow wnd, String text) {
		super(wnd, text, new ImageIcon("/images/save_tree.png"));
		
		putValue(SHORT_DESCRIPTION, "Save tree to disk");
	}


	@Override
	public void actionPerformed(ActionEvent e) {

		JFileChooser chooser = new JFileChooser(DRKTools.prefs.get("tree_last_path", ""));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("DRKSpiderJava file", "drkspider");
	    chooser.setFileFilter(filter);
	    chooser.setApproveButtonText("Save");
	   
	    int returnVal = chooser.showSaveDialog(wnd);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {

	    	File selectedFile = chooser.getSelectedFile();
	    	
	    	// Si no tiene la extensión, se la agrego
			if (!selectedFile.getName().endsWith("."+filter.getExtensions()[0]))
				selectedFile = new File(selectedFile.getAbsolutePath()+"."+filter.getExtensions()[0]);
	    	
	    	// Confirm overwrite
	    	if (selectedFile.exists())
	    		if (JOptionPane.showConfirmDialog(wnd, "The file "+selectedFile.getName()+" already exists. Do you want to overwrite?", "Confirm overwrite", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION)
	    			return;

    		DRKTools.prefs.put("tree_last_path", selectedFile.getParent());

    		SaveTreeWorker worker = new SaveTreeWorker(wnd, selectedFile);
    		worker.execute();
	    }
	}

}
