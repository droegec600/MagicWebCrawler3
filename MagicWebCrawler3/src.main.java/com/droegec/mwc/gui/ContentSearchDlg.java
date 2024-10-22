package com.droegec.mwc.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.droegec.mwc.DRKTools;

public class ContentSearchDlg extends JDialog {
	public static final int CANCEL = 0;
	public static final int FIND_NEXT = 1;
	public static final int FIND_ALL = 2;
	private static final long serialVersionUID = 9035876010276685922L;
	private JTextArea txtSearch = new JTextArea();
	public int action = CANCEL;
	public boolean regex = false;
	public boolean case_insensitive = false;
	private JCheckBox checkRegularExpresion;
	private JCheckBox checkCaseInsensitive;
	
	/**
	 * Create the dialog.
	 */
	public ContentSearchDlg(Window owner) {
		super(owner);
		
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Search within content...");
		setBounds(100, 100, 595, 332);
		getContentPane().setLayout(new BorderLayout());
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton btnFindNext = new JButton("Find next");
		btnFindNext.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				action = FIND_NEXT;
				regex = checkRegularExpresion.isSelected();
				case_insensitive = checkCaseInsensitive.isSelected();
				DRKTools.prefs.putBoolean("search_content_use_regex", regex);
				DRKTools.prefs.putBoolean("search_content_case_insensitive", case_insensitive);
				DRKTools.prefs.put("search_content", getText());
				ContentSearchDlg.this.dispatchEvent(new WindowEvent(ContentSearchDlg.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		buttonPane.add(btnFindNext);
		JButton okButton = new JButton("Find all");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				action = FIND_ALL;
				regex = checkRegularExpresion.isSelected();
				case_insensitive = checkCaseInsensitive.isSelected();
				DRKTools.prefs.putBoolean("search_content_use_regex", regex);
				DRKTools.prefs.putBoolean("search_content_case_insensitive", case_insensitive);
				DRKTools.prefs.put("search_content", getText());
				ContentSearchDlg.this.dispatchEvent(new WindowEvent(ContentSearchDlg.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = CANCEL;
				ContentSearchDlg.this.dispatchEvent(new WindowEvent(ContentSearchDlg.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(6, 6, 6, 6));
		getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		JLabel lblSearchFor = new JLabel("Search for:");
		GridBagConstraints gbc_lblSearchFor = new GridBagConstraints();
		gbc_lblSearchFor.insets = new Insets(0, 0, 5, 5);
		gbc_lblSearchFor.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblSearchFor.gridx = 0;
		gbc_lblSearchFor.gridy = 0;
		panel.add(lblSearchFor, gbc_lblSearchFor);
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.weighty = 1.0;
		gbc_scrollPane.weightx = 1.0;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 0;
		panel.add(scrollPane, gbc_scrollPane);
		txtSearch.setRows(8);
		txtSearch.setColumns(40);
		scrollPane.setViewportView(txtSearch);
		
		checkCaseInsensitive = new JCheckBox("Case insensitive");
		GridBagConstraints gbc_chckbxCaseInsensitive = new GridBagConstraints();
		gbc_chckbxCaseInsensitive.anchor = GridBagConstraints.WEST;
		gbc_chckbxCaseInsensitive.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxCaseInsensitive.gridx = 1;
		gbc_chckbxCaseInsensitive.gridy = 1;
		panel.add(checkCaseInsensitive, gbc_chckbxCaseInsensitive);
		checkRegularExpresion = new JCheckBox("Regular expression");
		GridBagConstraints gbc_chckbxRegularExpresion = new GridBagConstraints();
		gbc_chckbxRegularExpresion.anchor = GridBagConstraints.NORTHWEST;
		gbc_chckbxRegularExpresion.gridx = 1;
		gbc_chckbxRegularExpresion.gridy = 2;
		gbc_chckbxRegularExpresion.fill = GridBagConstraints.NONE;
		panel.add(checkRegularExpresion, gbc_chckbxRegularExpresion);
		
		// Prefs
		checkRegularExpresion.setSelected(DRKTools.prefs.getBoolean("search_content_use_regex", false));
		checkCaseInsensitive.setSelected(DRKTools.prefs.getBoolean("search_content_case_insensitive", false));
		txtSearch.setText(DRKTools.prefs.get("search_content", ""));
		pack();
		setLocation(DRKTools.getCenterLocation(this));
	}

	public String getText() {
		return txtSearch.getText();
	}

}
