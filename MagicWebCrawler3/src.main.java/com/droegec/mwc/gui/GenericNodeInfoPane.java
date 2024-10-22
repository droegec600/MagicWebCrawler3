package com.droegec.mwc.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.droegec.mwc.model.WebTreeNode;

import java.awt.Insets;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class GenericNodeInfoPane extends JPanel {

	@SuppressWarnings("unused")
	final private WebTreeNode nodo;
	private JLabel lbl_size = null;
	private JLabel size = null;
	private JLabel lbl_content_type = null;
	private JLabel contenttype = null;
	private JLabel lblstatus = null;
	private JTextField status = null;
	/**
	 * This is the default constructor
	 */
	public GenericNodeInfoPane(WebTreeNode nodo) {
		super();
		this.nodo = nodo;
		initialize();
		
		contenttype.setText(nodo.getContentType());
		size.setText(String.valueOf(nodo.getSize())+" bytes");
		status.setText(nodo.getStatus()+" - "+nodo.getError());
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints5.gridy = 0;
		gridBagConstraints5.weightx = 1.0;
		gridBagConstraints5.insets = new Insets(3, 3, 3, 3);
		gridBagConstraints5.gridx = 1;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.anchor = GridBagConstraints.EAST;
		gridBagConstraints4.insets = new Insets(3, 3, 3, 3);
		gridBagConstraints4.gridy = 0;
		lblstatus = new JLabel();
		lblstatus.setText("Status:");
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 1;
		gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints2.insets = new java.awt.Insets(3,3,3,3);
		gridBagConstraints2.gridy = 1;
		contenttype = new JLabel();
		contenttype.setText("");
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.insets = new java.awt.Insets(3,3,3,3);
		gridBagConstraints11.anchor = GridBagConstraints.EAST;
		gridBagConstraints11.gridy = 1;
		lbl_content_type = new JLabel();
		lbl_content_type.setText("Content type:");
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.insets = new java.awt.Insets(3,3,3,3);
		gridBagConstraints1.fill = java.awt.GridBagConstraints.NONE;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints1.gridy = 2;
		size = new JLabel();
		size.setText("0 bytes");
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new java.awt.Insets(3,3,3,3);
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.gridy = 2;
		lbl_size = new JLabel();
		lbl_size.setText("Size:");
		this.setLayout(new GridBagLayout());
		this.setSize(300, 200);
		this.add(lbl_size, gridBagConstraints);
		this.add(size, gridBagConstraints1);
		this.add(lbl_content_type, gridBagConstraints11);
		this.add(contenttype, gridBagConstraints2);
		this.add(lblstatus, gridBagConstraints4);
		this.add(getStatus(), gridBagConstraints5);
	}

	/**
	 * This method initializes status	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getStatus() {
		if (status == null) {
			status = new JTextField();
			status.setEditable(false);
		}
		return status;
	}

}
