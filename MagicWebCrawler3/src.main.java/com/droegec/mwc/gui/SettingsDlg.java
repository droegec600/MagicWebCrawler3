package com.droegec.mwc.gui;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JTextField;
import javax.swing.JCheckBox;

import com.droegec.mwc.DRKTools;

public class SettingsDlg extends JDialog {
	
	private static final long serialVersionUID = -7491642650645262745L;
	private JTextField c_useragent;
	private JCheckBox c_codefolding;
	private JCheckBox c_follow_redirects;
	private JCheckBox c_seointree;
	private JTextField c_thread_count;

	public SettingsDlg(Frame owner) {
		super(owner);
		setTitle("Settings...");
		setModal(true);
		
		JPanel controls = new JPanel();
		getContentPane().add(controls, BorderLayout.CENTER);
		GridBagLayout gbl_controls = new GridBagLayout();
		controls.setLayout(gbl_controls);
		
		JLabel lblReportUserAgent = new JLabel("Report user agent:");
		GridBagConstraints gbc_lblReportUserAgent = new GridBagConstraints();
		gbc_lblReportUserAgent.anchor = GridBagConstraints.EAST;
		gbc_lblReportUserAgent.insets = new Insets(5, 5, 5, 5);
		gbc_lblReportUserAgent.gridx = 0;
		gbc_lblReportUserAgent.gridy = 0;
		controls.add(lblReportUserAgent, gbc_lblReportUserAgent);
		
		c_useragent = new JTextField();
		c_useragent.setToolTipText("Leave this field empty for reporting DRKSpiderJava user agent.");
		GridBagConstraints gbc_c_useragent = new GridBagConstraints();
		gbc_c_useragent.gridwidth = 2;
		gbc_c_useragent.weightx = 1.0;
		gbc_c_useragent.insets = new Insets(5, 5, 5, 5);
		gbc_c_useragent.fill = GridBagConstraints.HORIZONTAL;
		gbc_c_useragent.gridx = 1;
		gbc_c_useragent.gridy = 0;
		controls.add(c_useragent, gbc_c_useragent);
		c_useragent.setColumns(10);
		
		c_follow_redirects = new JCheckBox("Follow HTTP redirections");
		GridBagConstraints gbc_c_follow_redirects = new GridBagConstraints();
		gbc_c_follow_redirects.gridwidth = 2;
		gbc_c_follow_redirects.anchor = GridBagConstraints.WEST;
		gbc_c_follow_redirects.insets = new Insets(5, 5, 5, 5);
		gbc_c_follow_redirects.gridx = 1;
		gbc_c_follow_redirects.gridy = 1;
		controls.add(c_follow_redirects, gbc_c_follow_redirects);
		
		c_seointree = new JCheckBox("Show SEO score in tree");
		GridBagConstraints gbc_c_seointree = new GridBagConstraints();
		gbc_c_seointree.gridwidth = 2;
		gbc_c_seointree.anchor = GridBagConstraints.WEST;
		gbc_c_seointree.insets = new Insets(5, 5, 5, 5);
		gbc_c_seointree.gridx = 1;
		gbc_c_seointree.gridy = 2;
		controls.add(c_seointree, gbc_c_seointree);
		
		c_codefolding = new JCheckBox("Enable code folding in source text area");
		GridBagConstraints gbc_chckbxEnableCodeFolding = new GridBagConstraints();
		gbc_chckbxEnableCodeFolding.gridwidth = 2;
		gbc_chckbxEnableCodeFolding.insets = new Insets(5, 5, 5, 5);
		gbc_chckbxEnableCodeFolding.anchor = GridBagConstraints.WEST;
		gbc_chckbxEnableCodeFolding.gridx = 1;
		gbc_chckbxEnableCodeFolding.gridy = 3;
		controls.add(c_codefolding, gbc_chckbxEnableCodeFolding);
		
		
		JLabel lblMaximumThreadCount = new JLabel("Maximum thread count:");
		GridBagConstraints gbc_lblMaximumThreadCount = new GridBagConstraints();
		gbc_lblMaximumThreadCount.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblMaximumThreadCount.insets = new Insets(5, 5, 5, 5);
		gbc_lblMaximumThreadCount.gridx = 0;
		gbc_lblMaximumThreadCount.gridy = 4;
		controls.add(lblMaximumThreadCount, gbc_lblMaximumThreadCount);
		
		c_thread_count = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(5, 5, 5, 5);
		gbc_textField.anchor = GridBagConstraints.NORTHWEST;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 4;
		controls.add(c_thread_count, gbc_textField);
		c_thread_count.setColumns(4);
		
		JLabel lblForMatching = new JLabel("<html>1: for matching tree with site navigation.<br>\r\n2: for honouring HTTP protocol (recommended)<br>\r\n3 to 15: for speeding up crawling");
		GridBagConstraints gbc_lblForMatching = new GridBagConstraints();
		gbc_lblForMatching.anchor = GridBagConstraints.WEST;
		gbc_lblForMatching.insets = new Insets(5, 5, 5, 5);
		gbc_lblForMatching.gridx = 2;
		gbc_lblForMatching.gridy = 4;
		controls.add(lblForMatching, gbc_lblForMatching);
		
		JPanel buttons = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttons.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(buttons, BorderLayout.SOUTH);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				DRKTools.prefs.put("user_agent", c_useragent.getText());
				DRKTools.prefs.putBoolean("use_code_folding", c_codefolding.isSelected());
				DRKTools.prefs.putBoolean("follow_http_redirects", c_follow_redirects.isSelected());
				DRKTools.prefs.putBoolean("show_seo_in_tree", c_seointree.isSelected());
				int tc = Integer.valueOf(c_thread_count.getText());
				tc = (tc < 1 || tc > 15)?2:tc;
				DRKTools.prefs.putInt("thread_count", tc);
				
				SettingsDlg.this.dispatchEvent(new WindowEvent(SettingsDlg.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		buttons.add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SettingsDlg.this.dispatchEvent(new WindowEvent(SettingsDlg.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		buttons.add(btnCancel);
		
		c_follow_redirects.setSelected(DRKTools.prefs.getBoolean("follow_http_redirects", true));
		c_codefolding.setSelected(DRKTools.prefs.getBoolean("use_code_folding", false));
		c_seointree.setSelected(DRKTools.prefs.getBoolean("show_seo_in_tree", true));
		c_useragent.setText(DRKTools.prefs.get("user_agent", ""));
		c_thread_count.setText(String.valueOf(DRKTools.prefs.getInt("thread_count", 2)));

		pack();
		setLocation(DRKTools.getCenterLocation(this));
		setVisible(true);
	}
	
}
