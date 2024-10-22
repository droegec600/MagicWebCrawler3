package com.droegec.mwc.gui.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.droegec.mwc.DRKTools;
import com.droegec.mwc.gui.ExportDlg;
import com.droegec.mwc.gui.MainWindow;
import com.droegec.mwc.model.exporters.CSVExporter;
import com.droegec.mwc.model.exporters.Exporter;
import com.droegec.mwc.model.exporters.PlainTextExporter;
import com.droegec.mwc.model.exporters.XMLExporter;

public class ExportTree extends AbstractSpiderAction {
	private static final long serialVersionUID = -2352506309193888584L;
	public ExportTree(MainWindow wnd, String text) {
		super(wnd, text);
	}

	public void actionPerformed(ActionEvent e) {
		if (wnd.isThereSelectedNode()) {
			

			ExportDlg dlg = new ExportDlg(wnd);
			
			if (!dlg.ok)
				return;
			
			JFileChooser chooser = new JFileChooser(DRKTools.prefs.get("export_last_path", ""));
			FileNameExtensionFilter filter;
			Exporter exporter;
			switch(dlg.getFormat()) {
			default:
			case 0: {
		    	filter = new FileNameExtensionFilter("CSV file", "csv");
		    	exporter = new CSVExporter(wnd.getSelectedNode());
			}
			break;
			case 1: {
		    	filter = new FileNameExtensionFilter("XML file", "xml");
		    	exporter = new XMLExporter(wnd.getSelectedNode());
			}
			break;
			case 2: {
		    	filter = new FileNameExtensionFilter("Plain text file", "txt");
		    	exporter = new PlainTextExporter(wnd.getSelectedNode());
			}
			break;
			}
		    chooser.setFileFilter(filter);
		    chooser.setApproveButtonText("Export");
		   
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
		    		
		    	try {
		    		switch(dlg.getData()) {
		    		default:
		    		case 0:
						exporter.exportSitemap(selectedFile.getAbsolutePath());
						break;
		    		case 1:
						exporter.exportLinks(selectedFile.getAbsolutePath());
						break;
		    		case 2:
						exporter.exportErrors(selectedFile.getAbsolutePath());
						break;
		    		}
		    		
		    		// Save defaults
		    		DRKTools.prefs.put("export_last_path", selectedFile.getParent());
		    		
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(wnd, e1.getLocalizedMessage(), "Error during export", JOptionPane.ERROR_MESSAGE);
				}
		    }
			
		}
	}


}
