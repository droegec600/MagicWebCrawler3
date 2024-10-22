package com.droegec.mwc.gui.panes;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.droegec.mwc.ProgressListener;
import com.droegec.mwc.gui.MainWindow;
import com.droegec.mwc.model.DRKLink;
import com.droegec.mwc.model.NodeTextLocation;
import com.droegec.mwc.model.WebTreeNode;

@SuppressWarnings("serial")
public class ImageInfoPane extends InfoPane {

	final private WebTreeNode nodo;
	private JLabel imageControl = new JLabel("");
	private JLabel lbl_size = null;
	private JLabel size = null;
	private JLabel lbl_content_type = null;
	private JLabel contenttype = null;
	private JLabel lblstatus = null;
	private JTextField status = null;
	final private ProgressListener progress;
	
	/**
	 * This is the default constructor
	 */
	public ImageInfoPane(MainWindow wnd, WebTreeNode nodo, ProgressListener progress) {
		super();
		this.nodo = nodo;
		this.progress = progress;
		initialize();
		
		contenttype.setText(nodo.getContentType());
		size.setText(String.valueOf(nodo.getSize())+" bytes");
		status.setText(nodo.getStatus()+" - "+nodo.getError());
		
		GridBagConstraints gbc_imageControl = new GridBagConstraints();
		gbc_imageControl.anchor = GridBagConstraints.NORTHWEST;
		gbc_imageControl.weighty = 1.0;
		gbc_imageControl.weightx = 1.0;
		gbc_imageControl.gridx = 1;
		gbc_imageControl.gridy = 3;
		add(imageControl, gbc_imageControl);
		
		getImage();
	}

	protected void getImage() {
		progress.setUnknownProgress(true, "Loading image...");
		ImageInfoPane.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		new Thread() {
			@Override
			public void run() {
				final URL url = nodo.getURL();
				final ImageIcon icon = new ImageIcon(url); // bloquea hasta cargar la imagen
				
				SwingUtilities.invokeLater(new Runnable() {
					
					public void run() {
						ImageInfoPane.this.imageControl.setIcon(icon);
						ImageInfoPane.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						progress.setUnknownProgress(false, "");
					}
				});
			}
		}.start();
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
		gridBagConstraints5.insets = new Insets(3, 3, 5, 3);
		gridBagConstraints5.gridx = 1;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.anchor = GridBagConstraints.EAST;
		gridBagConstraints4.insets = new Insets(3, 3, 5, 5);
		gridBagConstraints4.gridy = 0;
		lblstatus = new JLabel();
		lblstatus.setText("Status:");
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 1;
		gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints2.insets = new Insets(3, 3, 5, 3);
		gridBagConstraints2.gridy = 1;
		contenttype = new JLabel();
		contenttype.setText("");
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.insets = new Insets(3, 3, 5, 5);
		gridBagConstraints11.anchor = GridBagConstraints.EAST;
		gridBagConstraints11.gridy = 1;
		lbl_content_type = new JLabel();
		lbl_content_type.setText("Content type:");
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.insets = new Insets(3, 3, 5, 3);
		gridBagConstraints1.fill = java.awt.GridBagConstraints.NONE;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints1.gridy = 2;
		size = new JLabel();
		size.setText("0 bytes");
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(3, 3, 5, 5);
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

	@Override
	public void locateLink(DRKLink link) {
	}

	@Override
	public void locateContent(NodeTextLocation gotoContent) {
	}

}
