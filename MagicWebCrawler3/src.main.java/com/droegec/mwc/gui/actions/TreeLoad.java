package com.droegec.mwc.gui.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.droegec.mwc.DRKTools;
import com.droegec.mwc.gui.LoadTreeWorker;
import com.droegec.mwc.gui.MainWindow;

public class TreeLoad extends AbstractSpiderAction {
	private static final long serialVersionUID = -5715647942510706693L;

	public TreeLoad(MainWindow wnd, String text) {
		super(wnd, text, new ImageIcon("/images/load_tree.png"));
		
		putValue(SHORT_DESCRIPTION, "Load tree from disk");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		JFileChooser chooser = new JFileChooser(DRKTools.prefs.get("tree_last_path", ""));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("DRKSpiderJava file", "drkspider");
	    chooser.setFileFilter(filter);
	    chooser.setApproveButtonText("Load");
	   
	    int returnVal = chooser.showOpenDialog(wnd);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {

	    	File selectedFile = chooser.getSelectedFile();
    		DRKTools.prefs.put("tree_last_path", selectedFile.getParent());
    		
    		LoadTreeWorker worker = new LoadTreeWorker(wnd, selectedFile);
    		worker.execute();
	    }
	}

}
