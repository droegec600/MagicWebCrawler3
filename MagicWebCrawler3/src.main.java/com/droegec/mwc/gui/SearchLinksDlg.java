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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.DimensionUIResource;

import com.droegec.mwc.DRKTools;

public class SearchLinksDlg extends JDialog {

	private static final long serialVersionUID = -5586450637174683111L;
	private JTextField c_tag;
	private JTextField c_url;
	private JTextField c_anchor;
	private JCheckBox c_url_regex;
	private JCheckBox c_anchor_regex;
	private JComboBox<String> c_error;
	private JComboBox<String> c_nofollow;
	private JComboBox<String> c_external;

	public String tag;
	public String url;
	public String anchor;
	public boolean url_regex;
	public boolean anchor_regex;
	public Boolean error = null;
	public Boolean nofollow = null;
	public Boolean external = null;
	public boolean ok = false;
	
	public SearchLinksDlg(Frame owner) {
		super(owner);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Search links...");
		setModal(true);
		setMinimumSize(new DimensionUIResource(360, 220));
		
		JPanel controls = new JPanel();
		getContentPane().add(controls, BorderLayout.CENTER);
		GridBagLayout gbl_controls = new GridBagLayout();
		gbl_controls.columnWidths = new int[]{0, 0, 0, 0};
		gbl_controls.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_controls.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_controls.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		controls.setLayout(gbl_controls);
		
		JLabel lblTag = new JLabel("Tag:");
		GridBagConstraints gbc_lblTag = new GridBagConstraints();
		gbc_lblTag.insets = new Insets(5, 5, 5, 5);
		gbc_lblTag.anchor = GridBagConstraints.EAST;
		gbc_lblTag.gridx = 0;
		gbc_lblTag.gridy = 0;
		controls.add(lblTag, gbc_lblTag);
		
		c_tag = new JTextField();
		GridBagConstraints gbc_tag = new GridBagConstraints();
		gbc_tag.gridwidth = 2;
		gbc_tag.insets = new Insets(5, 5, 5, 0);
		gbc_tag.fill = GridBagConstraints.HORIZONTAL;
		gbc_tag.gridx = 1;
		gbc_tag.gridy = 0;
		controls.add(c_tag, gbc_tag);
		c_tag.setColumns(10);
		
		JLabel lblUrl = new JLabel("URL:");
		GridBagConstraints gbc_lblUrl = new GridBagConstraints();
		gbc_lblUrl.anchor = GridBagConstraints.EAST;
		gbc_lblUrl.insets = new Insets(5, 5, 5, 5);
		gbc_lblUrl.gridx = 0;
		gbc_lblUrl.gridy = 1;
		controls.add(lblUrl, gbc_lblUrl);
		
		c_url = new JTextField();
		GridBagConstraints gbc_url = new GridBagConstraints();
		gbc_url.fill = GridBagConstraints.HORIZONTAL;
		gbc_url.insets = new Insets(5, 5, 5, 5);
		gbc_url.gridx = 1;
		gbc_url.gridy = 1;
		controls.add(c_url, gbc_url);
		c_url.setColumns(10);
		
		c_url_regex = new JCheckBox("RegEx");
		GridBagConstraints gbc_url_regex = new GridBagConstraints();
		gbc_url_regex.insets = new Insets(5, 5, 5, 0);
		gbc_url_regex.gridx = 2;
		gbc_url_regex.gridy = 1;
		controls.add(c_url_regex, gbc_url_regex);
		
		JLabel lblAchor = new JLabel("Achor:");
		GridBagConstraints gbc_lblAchor = new GridBagConstraints();
		gbc_lblAchor.anchor = GridBagConstraints.EAST;
		gbc_lblAchor.insets = new Insets(5, 5, 5, 5);
		gbc_lblAchor.gridx = 0;
		gbc_lblAchor.gridy = 2;
		controls.add(lblAchor, gbc_lblAchor);
		
		c_anchor = new JTextField();
		GridBagConstraints gbc_anchor = new GridBagConstraints();
		gbc_anchor.insets = new Insets(5, 5, 5, 5);
		gbc_anchor.fill = GridBagConstraints.HORIZONTAL;
		gbc_anchor.gridx = 1;
		gbc_anchor.gridy = 2;
		controls.add(c_anchor, gbc_anchor);
		c_anchor.setColumns(10);
		
		c_anchor_regex = new JCheckBox("RegEx");
		GridBagConstraints gbc_anchor_regex = new GridBagConstraints();
		gbc_anchor_regex.insets = new Insets(5, 5, 5, 0);
		gbc_anchor_regex.gridx = 2;
		gbc_anchor_regex.gridy = 2;
		controls.add(c_anchor_regex, gbc_anchor_regex);
		
		JLabel lblError = new JLabel("Error:");
		GridBagConstraints gbc_lblError = new GridBagConstraints();
		gbc_lblError.anchor = GridBagConstraints.EAST;
		gbc_lblError.insets = new Insets(5, 5, 5, 5);
		gbc_lblError.gridx = 0;
		gbc_lblError.gridy = 3;
		controls.add(lblError, gbc_lblError);
		
		c_error = new JComboBox<String>();
		c_error.setModel(new DefaultComboBoxModel<String>(new String[] {"", "Yes", "No"}));
		c_error.setMaximumRowCount(3);
		GridBagConstraints gbc_error = new GridBagConstraints();
		gbc_error.anchor = GridBagConstraints.WEST;
		gbc_error.insets = new Insets(5, 5, 5, 5);
		gbc_error.gridx = 1;
		gbc_error.gridy = 3;
		controls.add(c_error, gbc_error);
		
		JLabel lblNofollow = new JLabel("nofollow:");
		GridBagConstraints gbc_lblNofollow = new GridBagConstraints();
		gbc_lblNofollow.anchor = GridBagConstraints.EAST;
		gbc_lblNofollow.insets = new Insets(5, 5, 5, 5);
		gbc_lblNofollow.gridx = 0;
		gbc_lblNofollow.gridy = 4;
		controls.add(lblNofollow, gbc_lblNofollow);
		
		c_nofollow = new JComboBox<String>();
		c_nofollow.setModel(new DefaultComboBoxModel<String>(new String[] {"", "Yes", "No"}));
		GridBagConstraints gbc_c_nofollow = new GridBagConstraints();
		gbc_c_nofollow.anchor = GridBagConstraints.WEST;
		gbc_c_nofollow.insets = new Insets(5, 5, 5, 5);
		gbc_c_nofollow.gridx = 1;
		gbc_c_nofollow.gridy = 4;
		controls.add(c_nofollow, gbc_c_nofollow);
		
		JLabel lblExternal = new JLabel("External:");
		GridBagConstraints gbc_lblExternal = new GridBagConstraints();
		gbc_lblExternal.anchor = GridBagConstraints.EAST;
		gbc_lblExternal.insets = new Insets(5, 5, 0, 5);
		gbc_lblExternal.gridx = 0;
		gbc_lblExternal.gridy = 5;
		controls.add(lblExternal, gbc_lblExternal);
		
		c_external = new JComboBox<String>();
		c_external.setModel(new DefaultComboBoxModel<String>(new String[] {"", "Yes", "No"}));
		GridBagConstraints gbc_c_external = new GridBagConstraints();
		gbc_c_external.anchor = GridBagConstraints.WEST;
		gbc_c_external.insets = new Insets(5, 5, 5, 5);
		gbc_c_external.gridx = 1;
		gbc_c_external.gridy = 5;
		controls.add(c_external, gbc_c_external);
		
		JPanel buttons = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttons.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(buttons, BorderLayout.SOUTH);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onOk();
			}
		});
		buttons.add(btnSearch);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SearchLinksDlg.this.dispatchEvent(new WindowEvent(SearchLinksDlg.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		buttons.add(btnCancel);
		
		prepareDgl();
		pack();
		setLocation(DRKTools.getCenterLocation(this));
		setVisible(true);
	}

	protected void prepareDgl() {
		c_tag.setText(DRKTools.prefs.get("search_links_tag", ""));
		c_url.setText(DRKTools.prefs.get("search_links_url", ""));
		c_anchor.setText(DRKTools.prefs.get("search_links_anchor", ""));
		c_url_regex.setSelected( DRKTools.prefs.getBoolean("search_links_url_regex", false));
		c_anchor_regex.setSelected(DRKTools.prefs.getBoolean("search_links_anchor_regex", false));
		c_error.setSelectedIndex(DRKTools.prefs.getInt("search_links_error", 0));
		c_nofollow.setSelectedIndex(DRKTools.prefs.getInt("search_links_nofollow", 0));
		c_external.setSelectedIndex(DRKTools.prefs.getInt("search_links_external", 0));
	}
	
	public void onOk() {
		ok = true;
		tag = c_tag.getText();
		url = c_url.getText();
		anchor = c_anchor.getText();
		url_regex = c_url_regex.isSelected();
		anchor_regex = c_anchor_regex.isSelected();
		if (!"".equals(c_error.getSelectedItem()))
			error = "Yes".equals(c_error.getSelectedItem());
		else
			error = null;
		if (!"".equals(c_nofollow.getSelectedItem()))
			nofollow = "Yes".equals(c_nofollow.getSelectedItem());
		else
			nofollow = null;
		if (!"".equals(c_external.getSelectedItem()))
			external = "Yes".equals(c_external.getSelectedItem());
		else
			external = null;
		
		DRKTools.prefs.put("search_links_tag", tag);
		DRKTools.prefs.put("search_links_url", url);
		DRKTools.prefs.put("search_links_anchor", anchor);
		DRKTools.prefs.putBoolean("search_links_url_regex", url_regex);
		DRKTools.prefs.putBoolean("search_links_anchor_regex", anchor_regex);
		DRKTools.prefs.putInt("search_links_error", c_error.getSelectedIndex());
		DRKTools.prefs.putInt("search_links_nofollow", c_nofollow.getSelectedIndex());
		DRKTools.prefs.putInt("search_links_external", c_external.getSelectedIndex());
		
		SearchLinksDlg.this.dispatchEvent(new WindowEvent(SearchLinksDlg.this, WindowEvent.WINDOW_CLOSING));
	}
	
	
}
