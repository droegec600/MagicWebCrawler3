package com.droegec.mwc.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.droegec.mwc.DRKTools;

public class SearchWithGoogle extends JDialog {
	private static final long serialVersionUID = -9030947810849351167L;
	private JPanel jContentPane = null;
	private JPanel bottom = null;
	private JPanel main = null;
	private JButton ok = null;
	private JLabel lbl_1 = null;
	private JTextField search_term = null;
	private JLabel lbl_2 = null;
	private JTextField search_site = null;
	private JButton cancel = null;

	/**
	 * @param owner
	 * @wbp.parser.constructor
	 */
	public SearchWithGoogle(Frame owner, String site) {
		super(owner);
		initialize();
		if (site != null)
			getSearch_site().setText(site);
		
		getRootPane().setDefaultButton(getOk());
		setLocation(DRKTools.getCenterLocation(this));
		setVisible(true);
	}
	public SearchWithGoogle(Frame owner) {
		this(owner, null);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(542, 147);
		this.setModal(true);
		this.setTitle("Search with Google...");
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
			jContentPane.add(getBottom(), BorderLayout.SOUTH);
			jContentPane.add(getMain(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes bottom	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getBottom() {
		if (bottom == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			bottom = new JPanel();
			bottom.setLayout(flowLayout);
			bottom.add(getOk(), null);
			bottom.add(getCancel(), null);
		}
		return bottom;
	}

	/**
	 * This method initializes main	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getMain() {
		if (main == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = GridBagConstraints.EAST;
			gridBagConstraints2.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints2.gridy = 1;
			lbl_2 = new JLabel();
			lbl_2.setText("Within:");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.EAST;
			gridBagConstraints.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints.gridy = 0;
			lbl_1 = new JLabel();
			lbl_1.setText("Search for:");
			main = new JPanel();
			main.setLayout(new GridBagLayout());
			main.add(lbl_1, gridBagConstraints);
			main.add(getSearch_term(), gridBagConstraints1);
			main.add(lbl_2, gridBagConstraints2);
			main.add(getSearch_site(), gridBagConstraints3);
		}
		return main;
	}

	/**
	 * This method initializes ok	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOk() {
		if (ok == null) {
			ok = new JButton();
			ok.setText("Search...");
			ok.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String str;
					try {
						if (getSearch_site().getText().length() > 0)
							str = "http://www.google.com/search?q=site:"+URLEncoder.encode(search_site.getText(), "UTF-8")+"+"+URLEncoder.encode(search_term.getText(), "UTF-8");
						else
							str = "http://www.google.com/search?q="+URLEncoder.encode(search_term.getText(), "UTF-8");
						DRKTools.showInBrowser(str, null);
					}
					catch(UnsupportedEncodingException ex) {
						// TODO
					}
					SearchWithGoogle.this.dispatchEvent(new WindowEvent(SearchWithGoogle.this, WindowEvent.WINDOW_CLOSING));
				}
			});
		}
		return ok;
	}

	/**
	 * This method initializes search_term	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getSearch_term() {
		if (search_term == null) {
			search_term = new JTextField();
		}
		return search_term;
	}

	/**
	 * This method initializes search_site	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getSearch_site() {
		if (search_site == null) {
			search_site = new JTextField();
		}
		return search_site;
	}

	/**
	 * This method initializes cancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancel() {
		if (cancel == null) {
			cancel = new JButton();
			cancel.setText("Cancel");
			cancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					SearchWithGoogle.this.dispatchEvent(new WindowEvent(SearchWithGoogle.this, WindowEvent.WINDOW_CLOSING));
				}
			});
		}
		return cancel;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
