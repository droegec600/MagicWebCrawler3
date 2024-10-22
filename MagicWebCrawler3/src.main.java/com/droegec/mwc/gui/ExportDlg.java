package com.droegec.mwc.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.droegec.mwc.DRKTools;

public class ExportDlg extends JDialog {
	private static final long serialVersionUID = 3453886452239644094L;
	private JComboBox<String> data;
	private JComboBox<String> format;
	public boolean ok = false;
	private String[] data_types;
	private String[] format_types;
	
	public ExportDlg(Frame frame) {
		super(frame);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Export tree...");
		setModal(true);
		
		JPanel controls = new JPanel();
		getContentPane().add(controls, BorderLayout.CENTER);
		GridBagLayout gbl_controls = new GridBagLayout();
		controls.setLayout(gbl_controls);
		
		JLabel lblExport = new JLabel("Export:");
		GridBagConstraints gbc_lblExport = new GridBagConstraints();
		gbc_lblExport.anchor = GridBagConstraints.EAST;
		gbc_lblExport.insets = new Insets(5, 5, 5, 5);
		gbc_lblExport.gridx = 0;
		gbc_lblExport.gridy = 0;
		controls.add(lblExport, gbc_lblExport);
		
		data_types = new String[] {"Sitemap", "Links", "Errors"};
		data = new JComboBox<String>();
		data.setModel(new DefaultComboBoxModel<String>(data_types));
		data.setSelectedItem(DRKTools.prefs.get("export_last_data_type", data_types[0]));
		GridBagConstraints gbc_data = new GridBagConstraints();
		gbc_data.insets = new Insets(5, 5, 5, 5);
		gbc_data.fill = GridBagConstraints.HORIZONTAL;
		gbc_data.gridx = 1;
		gbc_data.gridy = 0;
		controls.add(data, gbc_data);
		
		JLabel lblOutputFormat = new JLabel("Output format:");
		GridBagConstraints gbc_lblOutputFormat = new GridBagConstraints();
		gbc_lblOutputFormat.anchor = GridBagConstraints.EAST;
		gbc_lblOutputFormat.insets = new Insets(5, 5, 5, 5);
		gbc_lblOutputFormat.gridx = 0;
		gbc_lblOutputFormat.gridy = 1;
		controls.add(lblOutputFormat, gbc_lblOutputFormat);
		
		format_types = new String[] {"Comma-separated values", "XML", "Plain text"};
		format = new JComboBox<String>();
		format.setModel(new DefaultComboBoxModel<String>(format_types));
		format.setSelectedItem(DRKTools.prefs.get("export_last_file_type", format_types[0]));
		GridBagConstraints gbc_format = new GridBagConstraints();
		gbc_format.insets = new Insets(5, 5, 5, 5);
		gbc_format.fill = GridBagConstraints.HORIZONTAL;
		gbc_format.gridx = 1;
		gbc_format.gridy = 1;
		controls.add(format, gbc_format);
		
		JPanel buttons = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttons.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(buttons, BorderLayout.SOUTH);
		
		JButton btnExport = new JButton("Export");
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if (getFormat() == 1 && getData() != 0) {
					JOptionPane.showMessageDialog(ExportDlg.this, "XML output format is available for Sitemap data only", "Can't export", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
	    		DRKTools.prefs.put("export_last_data_type", (String)data.getSelectedItem());
	    		DRKTools.prefs.put("export_last_file_type", (String)format.getSelectedItem());
				ok = true;
				ExportDlg.this.dispatchEvent(new WindowEvent(ExportDlg.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		buttons.add(btnExport);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ExportDlg.this.dispatchEvent(new WindowEvent(ExportDlg.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		buttons.add(btnCancel);
		
		pack();
		setLocation(DRKTools.getCenterLocation(this));
		setVisible(true);
	}
	
	public int getData() {
		return data.getSelectedIndex();
	}
	public int getFormat() {
		return format.getSelectedIndex();
	}
}
