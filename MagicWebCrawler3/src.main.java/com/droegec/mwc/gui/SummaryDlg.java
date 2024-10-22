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
import java.net.HttpCookie;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import com.droegec.mwc.DRKTools;
import com.droegec.mwc.gui.helpers.CookieTableModel;
import com.droegec.mwc.gui.helpers.TablePreferences;
import com.droegec.mwc.model.WebTreeModel;

public class SummaryDlg extends JDialog {
	private static final long serialVersionUID = 5604239796277523573L;
	private final WebTreeModel document;
	private JLabel parse_time;
	private JLabel real_speed;
	private JLabel interrupted;
	private JTable cookies;
	
	public SummaryDlg(Frame owner, WebTreeModel document) {
		super(owner);
		setTitle("Crawl summary");
		this.document = document;
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JPanel content = new JPanel();
		getContentPane().add(content, BorderLayout.CENTER);
		GridBagLayout gbl_content = new GridBagLayout();
		content.setLayout(gbl_content);
		
		JLabel lblUrlBase = new JLabel("URL Base:");
		GridBagConstraints gbc_lblUrlBase = new GridBagConstraints();
		gbc_lblUrlBase.insets = new Insets(5, 5, 5, 5);
		gbc_lblUrlBase.anchor = GridBagConstraints.EAST;
		gbc_lblUrlBase.gridx = 0;
		gbc_lblUrlBase.gridy = 0;
		content.add(lblUrlBase, gbc_lblUrlBase);
		
		JLabel url_base = new JLabel("-");
		GridBagConstraints gbc_url_base = new GridBagConstraints();
		gbc_url_base.weightx = 1.0;
		gbc_url_base.insets = new Insets(5, 5, 5, 0);
		gbc_url_base.anchor = GridBagConstraints.WEST;
		gbc_url_base.gridx = 1;
		gbc_url_base.gridy = 0;
		content.add(url_base, gbc_url_base);
		
		JPanel buttons = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttons.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(buttons, BorderLayout.SOUTH);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SummaryDlg.this.dispatchEvent(new WindowEvent(SummaryDlg.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		buttons.add(btnClose);
		
		JLabel lblNodes = new JLabel("Parsed links:");
		GridBagConstraints gbc_lblNodes = new GridBagConstraints();
		gbc_lblNodes.anchor = GridBagConstraints.EAST;
		gbc_lblNodes.insets = new Insets(5, 5, 5, 5);
		gbc_lblNodes.gridx = 0;
		gbc_lblNodes.gridy = 1;
		content.add(lblNodes, gbc_lblNodes);
		
		JLabel node_count = new JLabel("0");
		GridBagConstraints gbc_node_count = new GridBagConstraints();
		gbc_node_count.anchor = GridBagConstraints.WEST;
		gbc_node_count.insets = new Insets(5, 5, 5, 0);
		gbc_node_count.gridx = 1;
		gbc_node_count.gridy = 1;
		content.add(node_count, gbc_node_count);
		
		JLabel lblInternalLinks = new JLabel("Internal links:");
		GridBagConstraints gbc_lblInternalLinks = new GridBagConstraints();
		gbc_lblInternalLinks.anchor = GridBagConstraints.EAST;
		gbc_lblInternalLinks.insets = new Insets(5, 5, 5, 5);
		gbc_lblInternalLinks.gridx = 0;
		gbc_lblInternalLinks.gridy = 2;
		content.add(lblInternalLinks, gbc_lblInternalLinks);
		
		JLabel internal_link_count = new JLabel("0");
		GridBagConstraints gbc_internal_link_count = new GridBagConstraints();
		gbc_internal_link_count.insets = new Insets(5, 5, 5, 0);
		gbc_internal_link_count.anchor = GridBagConstraints.WEST;
		gbc_internal_link_count.gridx = 1;
		gbc_internal_link_count.gridy = 2;
		content.add(internal_link_count, gbc_internal_link_count);
		
		JLabel lblExternalLinks = new JLabel("External links:");
		GridBagConstraints gbc_lblExternalLinks = new GridBagConstraints();
		gbc_lblExternalLinks.anchor = GridBagConstraints.EAST;
		gbc_lblExternalLinks.insets = new Insets(5, 5, 5, 5);
		gbc_lblExternalLinks.gridx = 0;
		gbc_lblExternalLinks.gridy = 3;
		content.add(lblExternalLinks, gbc_lblExternalLinks);
		
		JLabel external_link_count = new JLabel("0");
		GridBagConstraints gbc_external_link_count = new GridBagConstraints();
		gbc_external_link_count.anchor = GridBagConstraints.WEST;
		gbc_external_link_count.insets = new Insets(5, 5, 5, 0);
		gbc_external_link_count.gridx = 1;
		gbc_external_link_count.gridy = 3;
		content.add(external_link_count, gbc_external_link_count);
		
		JLabel lblErrorLinks = new JLabel("Error links:");
		GridBagConstraints gbc_lblErrorLinks = new GridBagConstraints();
		gbc_lblErrorLinks.anchor = GridBagConstraints.EAST;
		gbc_lblErrorLinks.insets = new Insets(5, 5, 5, 5);
		gbc_lblErrorLinks.gridx = 0;
		gbc_lblErrorLinks.gridy = 4;
		content.add(lblErrorLinks, gbc_lblErrorLinks);
		
		JLabel error_link_count = new JLabel("0");
		GridBagConstraints gbc_error_link_count = new GridBagConstraints();
		gbc_error_link_count.anchor = GridBagConstraints.WEST;
		gbc_error_link_count.insets = new Insets(5, 5, 5, 5);
		gbc_error_link_count.gridx = 1;
		gbc_error_link_count.gridy = 4;
		content.add(error_link_count, gbc_error_link_count);
		
		JLabel lblTotalLinks = new JLabel("Total links:");
		GridBagConstraints gbc_lblTotalLinks = new GridBagConstraints();
		gbc_lblTotalLinks.anchor = GridBagConstraints.EAST;
		gbc_lblTotalLinks.insets = new Insets(5, 5, 5, 5);
		gbc_lblTotalLinks.gridx = 0;
		gbc_lblTotalLinks.gridy = 5;
		content.add(lblTotalLinks, gbc_lblTotalLinks);
		
		JLabel total_link_count = new JLabel("0");
		GridBagConstraints gbc_total_link_count = new GridBagConstraints();
		gbc_total_link_count.anchor = GridBagConstraints.WEST;
		gbc_total_link_count.insets = new Insets(5, 5, 5, 0);
		gbc_total_link_count.gridx = 1;
		gbc_total_link_count.gridy = 5;
		content.add(total_link_count, gbc_total_link_count);
		
		JLabel lblNofollowLinks = new JLabel("Nofollow links:");
		GridBagConstraints gbc_lblNofollowLinks = new GridBagConstraints();
		gbc_lblNofollowLinks.anchor = GridBagConstraints.EAST;
		gbc_lblNofollowLinks.insets = new Insets(5, 5, 5, 5);
		gbc_lblNofollowLinks.gridx = 0;
		gbc_lblNofollowLinks.gridy = 6;
		content.add(lblNofollowLinks, gbc_lblNofollowLinks);
		
		JLabel nofollow_link_count = new JLabel("0");
		GridBagConstraints gbc_nofollow_link_count = new GridBagConstraints();
		gbc_nofollow_link_count.anchor = GridBagConstraints.WEST;
		gbc_nofollow_link_count.insets = new Insets(5, 5, 5, 0);
		gbc_nofollow_link_count.gridx = 1;
		gbc_nofollow_link_count.gridy = 6;
		content.add(nofollow_link_count, gbc_nofollow_link_count);
			
		JLabel lblCrawlTime = new JLabel("Crawl time:");
		GridBagConstraints gbc_lblCrawlTime = new GridBagConstraints();
		gbc_lblCrawlTime.anchor = GridBagConstraints.EAST;
		gbc_lblCrawlTime.insets = new Insets(5, 5, 5, 5);
		gbc_lblCrawlTime.gridx = 0;
		gbc_lblCrawlTime.gridy = 7;
		content.add(lblCrawlTime, gbc_lblCrawlTime);
	
		parse_time = new JLabel("0");
		GridBagConstraints gbc_parse_time = new GridBagConstraints();
		gbc_parse_time.anchor = GridBagConstraints.WEST;
		gbc_parse_time.insets = new Insets(5, 5, 5, 0);
		gbc_parse_time.gridx = 1;
		gbc_parse_time.gridy = 7;
		content.add(parse_time, gbc_parse_time);
		parse_time.setText(DRKTools.secondsToHoursMinutesSeconds(this.document.getTotalParseTime() / 1000));
			
		JLabel lblSpeed = new JLabel("Speed:");
		GridBagConstraints gbc_lblSpeed = new GridBagConstraints();
		gbc_lblSpeed.anchor = GridBagConstraints.EAST;
		gbc_lblSpeed.insets = new Insets(5, 5, 5, 5);
		gbc_lblSpeed.gridx = 0;
		gbc_lblSpeed.gridy = 8;
		content.add(lblSpeed, gbc_lblSpeed);
	
		real_speed = new JLabel("New label");
		GridBagConstraints gbc_real_speed = new GridBagConstraints();
		gbc_real_speed.anchor = GridBagConstraints.WEST;
		gbc_real_speed.insets = new Insets(5, 5, 5, 0);
		gbc_real_speed.gridx = 1;
		gbc_real_speed.gridy = 8;
		content.add(real_speed, gbc_real_speed);
		
		JLabel lblWasInterrupted = new JLabel("Was interrupted:");
		GridBagConstraints gbc_lblWasInterrupted = new GridBagConstraints();
		gbc_lblWasInterrupted.anchor = GridBagConstraints.EAST;
		gbc_lblWasInterrupted.insets = new Insets(5, 5, 5, 5);
		gbc_lblWasInterrupted.gridx = 0;
		gbc_lblWasInterrupted.gridy = 9;
		content.add(lblWasInterrupted, gbc_lblWasInterrupted);
		
		interrupted = new JLabel("false");
		GridBagConstraints gbc_interrupted = new GridBagConstraints();
		gbc_interrupted.anchor = GridBagConstraints.WEST;
		gbc_interrupted.insets = new Insets(5, 5, 5, 0);
		gbc_interrupted.gridx = 1;
		gbc_interrupted.gridy = 9;
		content.add(interrupted, gbc_interrupted);
		interrupted.setText(String.valueOf(this.document.isInterrupted()));
		
		JScrollPane cookie_scroll = new JScrollPane();
		cookie_scroll.setBorder(new TitledBorder(null, "Cookies", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_cookie_scroll = new GridBagConstraints();
		gbc_cookie_scroll.weighty = 1.0;
		gbc_cookie_scroll.weightx = 1.0;
		gbc_cookie_scroll.gridwidth = 2;
		gbc_cookie_scroll.insets = new Insets(5, 5, 0, 0);
		gbc_cookie_scroll.fill = GridBagConstraints.BOTH;
		gbc_cookie_scroll.gridx = 0;
		gbc_cookie_scroll.gridy = 10;
		content.add(cookie_scroll, gbc_cookie_scroll);
		
		cookies = new JTable();
		cookies.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cookies.setAutoCreateRowSorter(true);
		cookie_scroll.setViewportView(cookies);
		
		internal_link_count.setText(String.valueOf(this.document.getInternalLinkCount()));
		external_link_count.setText(String.valueOf(this.document.getExternalLinkCount()));
		error_link_count.setText(String.valueOf(this.document.getErrorLinkCount()));
		total_link_count.setText(String.valueOf(this.document.getInternalLinkCount() + this.document.getExternalLinkCount()));
		nofollow_link_count.setText(String.valueOf(this.document.getNofollowLinkCount()));
		url_base.setText(this.document.getRootNode().getURL().toString());
		node_count.setText(String.valueOf(this.document.getTotalNodeCount()));
		double speed = ((double)this.document.getTotalNodeCount() / this.document.getTotalParseTime() * 1000);
		real_speed.setText(DRKTools.decimal_two.format(speed)+" links/second");

		cookies.setModel(new CookieTableModel(document.getCookieStore().getCookies()));
		new TablePreferences(cookies, HttpCookie.class, "summary");
		
		pack();
		setLocation(DRKTools.getCenterLocation(this));
		setVisible(true);
	}

}
