package com.droegec.mwc.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.droegec.mwc.DRKTools;
import com.droegec.mwc.gui.helpers.TablePreferences;
import com.droegec.mwc.model.PageSEOReview;
import com.droegec.mwc.model.WebTreeNode;

public class SEOReviewDlg extends JDialog {
	private static final long serialVersionUID = 3295806452993583161L;
	
	private JPanel jContentPane = null;
	final private PageSEOReview seo;
	private JTable keywordTable;
	private TableModel keywordsModel = new AbstractTableModel() {
		private static final long serialVersionUID = 2448003571498433783L;

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			switch(columnIndex) {
			case 0: 
				return seo.getKeywordInformation().get(rowIndex).keyword;
			case 1:
				return seo.getKeywordInformation().get(rowIndex).count;
			case 2:
				return seo.getKeywordInformation().get(rowIndex).ratio;
			}
			return null;
		}
		
		@Override
		public String getColumnName(int columnIndex) {
			switch(columnIndex) {
			case 0: 
				return "Keyword";
			case 1:
				return "Count";
			case 2:
				return "Relevance ‰";
			}
			return "";
		}

		@Override
		public int getRowCount() {
			return seo.getKeywordInformation().size();
		}
		
		@Override
		public int getColumnCount() {
			return 3;
		}
	};

	private JTextArea review;

	private JLabel performance;

	/**
	 * @param owner
	 */
	public SEOReviewDlg(Frame owner, WebTreeNode nodo) {
		super(owner);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		seo = nodo.getSEOReview();
		initialize();
		
		performance.setText(String.valueOf(seo.getResult())+"%");
		review.setText(seo.getReview());
		
		setLocation(DRKTools.getCenterLocation(this));
		new TablePreferences(keywordTable, PageSEOReview.KeywordInformation.class);
		setVisible(true);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(546, 638);
		this.setTitle("SEO Review");
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			
			JPanel buttonsPanel = new JPanel();
			jContentPane.add(buttonsPanel, BorderLayout.SOUTH);
			buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
			
			JButton btnNewButton = new JButton("Close");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					SEOReviewDlg.this.dispatchEvent(new WindowEvent(SEOReviewDlg.this, WindowEvent.WINDOW_CLOSING));
				}
			});
			btnNewButton.setHorizontalAlignment(SwingConstants.RIGHT);
			buttonsPanel.add(btnNewButton);
			
			JPanel informationPanel = new JPanel();
			jContentPane.add(informationPanel, BorderLayout.CENTER);
			GridBagLayout gbl_informationPanel = new GridBagLayout();
			informationPanel.setLayout(gbl_informationPanel);
			
			JLabel lblOverallSeoPerformance = new JLabel("Overall SEO performance:");
			lblOverallSeoPerformance.setFont(new Font("Tahoma", Font.BOLD, 11));
			GridBagConstraints gbc_lblOverallSeoPerformance = new GridBagConstraints();
			gbc_lblOverallSeoPerformance.anchor = GridBagConstraints.WEST;
			gbc_lblOverallSeoPerformance.insets = new Insets(10, 10, 10, 10);
			gbc_lblOverallSeoPerformance.gridx = 0;
			gbc_lblOverallSeoPerformance.gridy = 0;
			informationPanel.add(lblOverallSeoPerformance, gbc_lblOverallSeoPerformance);
			
			review = new JTextArea();
			review.setEditable(false);
			review.setFont(new Font("Monospaced", Font.PLAIN, 14));
			JScrollPane review_scroll = new JScrollPane();
			review_scroll.setViewportBorder(new TitledBorder(null, "Review", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			review_scroll.setViewportView(review);
			GridBagConstraints gbc_review_scroll = new GridBagConstraints();
			gbc_review_scroll.weighty = 0.5;
			gbc_review_scroll.gridwidth = 2;
			gbc_review_scroll.fill = GridBagConstraints.BOTH;
			gbc_review_scroll.insets = new Insets(0, 0, 5, 0);
			gbc_review_scroll.gridx = 0;
			gbc_review_scroll.gridy = 1;
			informationPanel.add(review_scroll, gbc_review_scroll);
			
			JPanel keywordsPanel = new JPanel();
			keywordsPanel.setBorder(new TitledBorder(null, "Keywords", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_keywordsPanel = new GridBagConstraints();
			gbc_keywordsPanel.weighty = 0.5;
			gbc_keywordsPanel.gridwidth = 2;
			gbc_keywordsPanel.insets = new Insets(0, 0, 5, 0);
			gbc_keywordsPanel.fill = GridBagConstraints.BOTH;
			gbc_keywordsPanel.gridx = 0;
			gbc_keywordsPanel.gridy = 2;
			informationPanel.add(keywordsPanel, gbc_keywordsPanel);
			keywordsPanel.setLayout(new BorderLayout(0, 0));
			
			JScrollPane scrollPane = new JScrollPane();
			keywordsPanel.add(scrollPane);
			
			keywordTable = new JTable();
			keywordTable.setModel(keywordsModel);
			keywordTable.setAutoCreateRowSorter(true);
			scrollPane.setViewportView(keywordTable);
			
			performance = new JLabel("-");
			performance.setFont(new Font("Tahoma", Font.BOLD, 11));
			GridBagConstraints gbc_performance = new GridBagConstraints();
			gbc_performance.insets = new Insets(10, 10, 10, 10);
			gbc_performance.anchor = GridBagConstraints.WEST;
			gbc_performance.weightx = 1.0;
			gbc_performance.gridx = 1;
			gbc_performance.gridy = 0;
			informationPanel.add(performance, gbc_performance);
			
		}
		return jContentPane;
	}

}
