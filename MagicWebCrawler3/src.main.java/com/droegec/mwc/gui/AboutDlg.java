package com.droegec.mwc.gui;

import java.awt.Cursor;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import com.droegec.mwc.DRKTools;

public class AboutDlg extends JDialog {

	private static final long serialVersionUID = 3694401475372083360L;
	
	public AboutDlg(Frame owner) {
		super(owner);
		setTitle("About...");
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[]{0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel name = new JLabel("DRKSpiderJava");
		name.setText(MainWindow.APP_NAME_AND_VERSION);
		name.setFont(name.getFont().deriveFont(22.0f));
		name.setAlignmentY(TOP_ALIGNMENT);
		GridBagConstraints gbc_name = new GridBagConstraints();
		gbc_name.insets = new Insets(10, 10, 10, 10);
		gbc_name.fill = GridBagConstraints.BOTH;
		gbc_name.gridx = 0;
		gbc_name.gridy = 0;
		getContentPane().add(name, gbc_name);
		
		JLabel icon = new JLabel("");
		icon.setIcon(new ImageIcon("/images/logo.png"));
		GridBagConstraints gbc_icon = new GridBagConstraints();
		gbc_icon.insets = new Insets(10, 10, 10, 10);
		gbc_icon.gridx = 0;
		gbc_icon.gridy = 1;
		getContentPane().add(icon, gbc_icon);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AboutDlg.this.dispatchEvent(new WindowEvent(AboutDlg.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		JLabel copyright = new JLabel("(C) 2003-2014 by Leandro Fern\u00E1ndez");
		GridBagConstraints gbc_copyright = new GridBagConstraints();
		gbc_copyright.anchor = GridBagConstraints.WEST;
		gbc_copyright.insets = new Insets(5, 10, 5, 10);
		gbc_copyright.gridx = 0;
		gbc_copyright.gridy = 2;
		getContentPane().add(copyright, gbc_copyright);
		
		JLabel website = new JLabel("http://www.drk.com.ar");
		website.setCursor(new Cursor(Cursor.HAND_CURSOR));
		website.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DRKTools.showInBrowser("http://www.drk.com.ar", (Frame)AboutDlg.this.getOwner());
			}
		});
		GridBagConstraints gbc_website = new GridBagConstraints();
		gbc_website.anchor = GridBagConstraints.WEST;
		gbc_website.insets = new Insets(5, 10, 5, 10);
		gbc_website.gridx = 0;
		gbc_website.gridy = 3;
		getContentPane().add(website, gbc_website);
		GridBagConstraints gbc_btnClose = new GridBagConstraints();
		gbc_btnClose.anchor = GridBagConstraints.EAST;
		gbc_btnClose.insets = new Insets(10, 10, 10, 10);
		gbc_btnClose.gridx = 0;
		gbc_btnClose.gridy = 4;
		getContentPane().add(btnClose, gbc_btnClose);
		
		pack();
		setLocation(DRKTools.getCenterLocation(this));
		setVisible(true);
	}

}
