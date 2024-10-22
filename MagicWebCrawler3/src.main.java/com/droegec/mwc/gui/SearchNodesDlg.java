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

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.DimensionUIResource;

import com.droegec.mwc.DRKTools;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class SearchNodesDlg extends JDialog {

	private static final long serialVersionUID = 2853309723880389463L;

	public boolean ok = false;
	private JTextField c_title;
	private JTextField c_description;
	private JTextField c_keywords;
	private JTextField c_author;
	private JCheckBox c_author_regex;
	private JCheckBox c_keywords_regex;
	private JCheckBox c_description_regex;
	private JCheckBox c_title_regex;
	private JComboBox<String> c_noindex;

	public String title;
	public String description;
	public String author;
	public String keywords;
	public boolean title_regex;
	public boolean keywords_regex;
	public boolean author_regex;
	public boolean description_regex;
	public Boolean noindex;


	public SearchNodesDlg(Frame owner) {
		super(owner);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Search nodes...");
		setModal(true);
		setMinimumSize(new DimensionUIResource(360, 220));
		
		JPanel controls = new JPanel();
		getContentPane().add(controls, BorderLayout.CENTER);
		GridBagLayout gbl_controls = new GridBagLayout();
		gbl_controls.columnWidths = new int[]{0, 0, 0, 0};
		gbl_controls.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_controls.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_controls.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		controls.setLayout(gbl_controls);
		
		JLabel lblTitile = new JLabel("Titile:");
		GridBagConstraints gbc_lblTitile = new GridBagConstraints();
		gbc_lblTitile.anchor = GridBagConstraints.EAST;
		gbc_lblTitile.insets = new Insets(5, 5, 5, 5);
		gbc_lblTitile.gridx = 0;
		gbc_lblTitile.gridy = 0;
		controls.add(lblTitile, gbc_lblTitile);
		
		c_title = new JTextField();
		GridBagConstraints gbc_c_title = new GridBagConstraints();
		gbc_c_title.insets = new Insets(0, 0, 5, 5);
		gbc_c_title.fill = GridBagConstraints.HORIZONTAL;
		gbc_c_title.gridx = 1;
		gbc_c_title.gridy = 0;
		controls.add(c_title, gbc_c_title);
		c_title.setColumns(10);
		
		c_title_regex = new JCheckBox("RegEx");
		GridBagConstraints gbc_c_title_regex = new GridBagConstraints();
		gbc_c_title_regex.insets = new Insets(0, 0, 5, 0);
		gbc_c_title_regex.gridx = 2;
		gbc_c_title_regex.gridy = 0;
		controls.add(c_title_regex, gbc_c_title_regex);
		
		JLabel lblDescription = new JLabel("Description:");
		GridBagConstraints gbc_lblDescription = new GridBagConstraints();
		gbc_lblDescription.insets = new Insets(5, 5, 5, 5);
		gbc_lblDescription.anchor = GridBagConstraints.EAST;
		gbc_lblDescription.gridx = 0;
		gbc_lblDescription.gridy = 1;
		controls.add(lblDescription, gbc_lblDescription);
		
		c_description = new JTextField();
		GridBagConstraints gbc_c_description = new GridBagConstraints();
		gbc_c_description.insets = new Insets(0, 0, 5, 5);
		gbc_c_description.fill = GridBagConstraints.HORIZONTAL;
		gbc_c_description.gridx = 1;
		gbc_c_description.gridy = 1;
		controls.add(c_description, gbc_c_description);
		c_description.setColumns(10);
		
		c_description_regex = new JCheckBox("RegEx");
		GridBagConstraints gbc_c_description_regex = new GridBagConstraints();
		gbc_c_description_regex.insets = new Insets(0, 0, 5, 0);
		gbc_c_description_regex.gridx = 2;
		gbc_c_description_regex.gridy = 1;
		controls.add(c_description_regex, gbc_c_description_regex);
		
		JLabel lblKeywords = new JLabel("Keywords:");
		GridBagConstraints gbc_lblKeywords = new GridBagConstraints();
		gbc_lblKeywords.anchor = GridBagConstraints.EAST;
		gbc_lblKeywords.insets = new Insets(5, 5, 5, 5);
		gbc_lblKeywords.gridx = 0;
		gbc_lblKeywords.gridy = 2;
		controls.add(lblKeywords, gbc_lblKeywords);
		
		c_keywords = new JTextField();
		GridBagConstraints gbc_c_keywords = new GridBagConstraints();
		gbc_c_keywords.insets = new Insets(0, 0, 5, 5);
		gbc_c_keywords.fill = GridBagConstraints.HORIZONTAL;
		gbc_c_keywords.gridx = 1;
		gbc_c_keywords.gridy = 2;
		controls.add(c_keywords, gbc_c_keywords);
		c_keywords.setColumns(10);
		
		c_keywords_regex = new JCheckBox("RegEx");
		GridBagConstraints gbc_c_keywords_regex = new GridBagConstraints();
		gbc_c_keywords_regex.insets = new Insets(0, 0, 5, 0);
		gbc_c_keywords_regex.gridx = 2;
		gbc_c_keywords_regex.gridy = 2;
		controls.add(c_keywords_regex, gbc_c_keywords_regex);
		
		JLabel lblAuthor = new JLabel("Author:");
		GridBagConstraints gbc_lblAuthor = new GridBagConstraints();
		gbc_lblAuthor.insets = new Insets(5, 5, 5, 5);
		gbc_lblAuthor.anchor = GridBagConstraints.EAST;
		gbc_lblAuthor.gridx = 0;
		gbc_lblAuthor.gridy = 3;
		controls.add(lblAuthor, gbc_lblAuthor);
		
		c_author = new JTextField();
		GridBagConstraints gbc_c_author = new GridBagConstraints();
		gbc_c_author.insets = new Insets(0, 0, 5, 5);
		gbc_c_author.fill = GridBagConstraints.HORIZONTAL;
		gbc_c_author.gridx = 1;
		gbc_c_author.gridy = 3;
		controls.add(c_author, gbc_c_author);
		c_author.setColumns(10);
		
		c_author_regex = new JCheckBox("RegEx");
		GridBagConstraints gbc_c_author_regex = new GridBagConstraints();
		gbc_c_author_regex.insets = new Insets(0, 0, 5, 0);
		gbc_c_author_regex.gridx = 2;
		gbc_c_author_regex.gridy = 3;
		controls.add(c_author_regex, gbc_c_author_regex);
		
		JLabel lblIsNoIndex = new JLabel("Is no index:");
		GridBagConstraints gbc_lblIsNoIndex = new GridBagConstraints();
		gbc_lblIsNoIndex.insets = new Insets(5, 5, 5, 5);
		gbc_lblIsNoIndex.anchor = GridBagConstraints.EAST;
		gbc_lblIsNoIndex.gridx = 0;
		gbc_lblIsNoIndex.gridy = 4;
		controls.add(lblIsNoIndex, gbc_lblIsNoIndex);
		
		c_noindex = new JComboBox<String>();
		c_noindex.setModel(new DefaultComboBoxModel<String>(new String[] {"", "Yes", "No"}));
		GridBagConstraints gbc_c_noindex = new GridBagConstraints();
		gbc_c_noindex.anchor = GridBagConstraints.WEST;
		gbc_c_noindex.insets = new Insets(5, 5, 5, 5);
		gbc_c_noindex.gridx = 1;
		gbc_c_noindex.gridy = 4;
		controls.add(c_noindex, gbc_c_noindex);
		
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
				SearchNodesDlg.this.dispatchEvent(new WindowEvent(SearchNodesDlg.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		buttons.add(btnCancel);

		prepareDgl();
		pack();
		setLocation(DRKTools.getCenterLocation(this));
		setVisible(true);
	}

	protected void prepareDgl() {
		c_title.setText(DRKTools.prefs.get("search_nodes_title", ""));
		c_description.setText(DRKTools.prefs.get("search_nodes_description", ""));
		c_keywords.setText(DRKTools.prefs.get("search_nodes_keywords", ""));
		c_author.setText(DRKTools.prefs.get("search_nodes_author", ""));
		c_title_regex.setSelected( DRKTools.prefs.getBoolean("search_nodes_title_regex", false));
		c_description_regex.setSelected(DRKTools.prefs.getBoolean("search_nodes_description_regex", false));
		c_keywords_regex.setSelected(DRKTools.prefs.getBoolean("search_nodes_keywords_regex", false));
		c_author_regex.setSelected(DRKTools.prefs.getBoolean("search_nodes_author_regex", false));
		c_noindex.setSelectedIndex(DRKTools.prefs.getInt("search_nodes_noindex", 0));

	}
	
	public void onOk() {
		ok = true;
		title = c_title.getText();
		description = c_description.getText();
		keywords = c_keywords.getText();
		author = c_author.getText();
		
		title_regex = c_title_regex.isSelected();
		description_regex = c_description_regex.isSelected();
		keywords_regex = c_keywords_regex.isSelected();
		author_regex = c_author_regex.isSelected();

		if (!"".equals(c_noindex.getSelectedItem()))
			noindex = "Yes".equals(c_noindex.getSelectedItem());
		else
			noindex = null;
		
		DRKTools.prefs.put("search_nodes_title", title);
		DRKTools.prefs.put("search_nodes_description", description);
		DRKTools.prefs.put("search_nodes_keywords", keywords);
		DRKTools.prefs.put("search_nodes_author", author);
		DRKTools.prefs.putBoolean("search_nodes_title_regex", title_regex);
		DRKTools.prefs.putBoolean("search_nodes_description_regex", description_regex);
		DRKTools.prefs.putBoolean("search_nodes_keywords_regex", keywords_regex);
		DRKTools.prefs.putBoolean("search_nodes_author_regex", author_regex);
		DRKTools.prefs.putInt("search_nodes_noindex", c_noindex.getSelectedIndex());

		SearchNodesDlg.this.dispatchEvent(new WindowEvent(SearchNodesDlg.this, WindowEvent.WINDOW_CLOSING));
	}

}
