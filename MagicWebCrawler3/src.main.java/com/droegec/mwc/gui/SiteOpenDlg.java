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
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.droegec.mwc.DRKTools;
import com.droegec.mwc.model.WebTreeModel;
import com.droegec.mwc.model.WebTreeNode;

import javax.swing.JPasswordField;

public class SiteOpenDlg extends JDialog {
	private static final long serialVersionUID = 1266346402452883021L;
	
	private JPanel controls = null;
	private JPanel buttons = null;
	private JButton btnOk = null;
	private JButton btnCancel = null;
	private JLabel lbl0;
	private JTextField url = null;
	private JLabel lbl2;
	private JSlider slide_depth = null;
	private JLabel depth_lbl = null;
	private JCheckBox keepContent;
	private JLabel lbl3;
	private JLabel lblRobotstxt;
	private JCheckBox obeyRobots;
	
	public boolean ok = false;
	private WebTreeModel document;
	private JScrollPane exclude_scroll;
	private JTextArea excludes;
	private JLabel lblUsernamepassword;
	private JTextField username;
	private JPasswordField password;
	private String old_username = "";
	private JCheckBox form_traversing;
	private JLabel lblFormTraversiong;
        
        // new  chd 13.01.2019
        
	private JLabel lblSearchParamter = null;
        private JTextField tfSearchParameter = null;
                
        private JLabel lblOutputDirectory = null;
        private JTextField tfOutputDirectory = null;
    
        private JLabel lblFollowLinkHTML = null;
        private JCheckBox cbxFollowLinkHTML = null;
                    
        private JLabel lblFollowLinkIMG = null;
        private JCheckBox cbxFollowLinkIMG = null;
    
        private JLabel lblSaveResource = null;
        private JCheckBox cbxSaveResource = null;
        
        private JLabel lblSaveIMG = null;
        private JCheckBox cbxSaveIMG = null;
        
        private JLabel lblExtractEMailAddress = null;
        private JCheckBox cbxExtractEMailAddress = null;
        
        private JLabel lblFilterHTMLLink = null;
        private JCheckBox cbxFilterHTMLLink = null;
        
        private JLabel lblFilterIMGLink = null;
        private JCheckBox cbxFilterIMGLink = null;
        
        private JLabel lblFilterScriptLink = null;
        private JCheckBox cbxFilterScriptLink = null;
        
        
        
	/**
	 * This is the default constructor
	 */
	public SiteOpenDlg(Frame owner) {
		super(owner);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setModal(true);

		controls = new JPanel();
		GridBagLayout gbl_jContentPane = new GridBagLayout();
		controls.setLayout(gbl_jContentPane);
		
		GridBagConstraints gbc_lbl0 = new GridBagConstraints();
		gbc_lbl0.gridx = 0;
		gbc_lbl0.insets = new Insets(5, 5, 5, 5);
		gbc_lbl0.anchor = GridBagConstraints.EAST;
		gbc_lbl0.gridy = 0;
		lbl0 = new JLabel();
		lbl0.setText("URL:");
		controls.add(lbl0, gbc_lbl0);
                
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints2.gridy = 0;
		gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints2.gridwidth = 3;
		gridBagConstraints2.gridx = 2;
		controls.add(getUrl(), gridBagConstraints2);
                
		GridBagConstraints gbc_lblUsernamepassword = new GridBagConstraints();
		gbc_lblUsernamepassword.anchor = GridBagConstraints.EAST;
		gbc_lblUsernamepassword.insets = new Insets(5, 5, 5, 5);
		gbc_lblUsernamepassword.gridx = 0;
		gbc_lblUsernamepassword.gridy = 1;
		controls.add(getLblUsernamepassword(), gbc_lblUsernamepassword);
                
		GridBagConstraints gbc_username = new GridBagConstraints();
		gbc_username.weightx = 0.5;
		gbc_username.gridwidth = 2;
		gbc_username.insets = new Insets(5, 5, 5, 5);
		gbc_username.fill = GridBagConstraints.HORIZONTAL;
		gbc_username.gridx = 2;
		gbc_username.gridy = 1;
		controls.add(getUsername(), gbc_username);
                
                
		GridBagConstraints gbc_password = new GridBagConstraints();
		gbc_password.weightx = 0.5;
		gbc_password.insets = new Insets(5, 5, 5, 5);
		gbc_password.fill = GridBagConstraints.HORIZONTAL;
		gbc_password.gridx = 4;
		gbc_password.gridy = 1;
		controls.add(getPassword(), gbc_password);

		
		GridBagConstraints depth_lbl_constraints = new GridBagConstraints();
		depth_lbl_constraints.gridx = 2;
		depth_lbl_constraints.insets = new Insets(5, 5, 5, 5);
		depth_lbl_constraints.gridy = 2;
		depth_lbl = new JLabel();
		depth_lbl.setText("3");
		controls.add(depth_lbl, depth_lbl_constraints);
                
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridwidth = 2;
		gridBagConstraints21.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints21.fill = GridBagConstraints.BOTH;
		gridBagConstraints21.gridy = 2;
		gridBagConstraints21.weightx = 1.0;
		gridBagConstraints21.weighty = 0.0;
		gridBagConstraints21.gridx = 3;
		controls.add(getDepth(), gridBagConstraints21);
                
		GridBagConstraints gbc_lbl2 = new GridBagConstraints();
		gbc_lbl2.ipadx = 4;
		gbc_lbl2.gridx = 0;
		gbc_lbl2.anchor = GridBagConstraints.EAST;
		gbc_lbl2.insets = new Insets(5, 5, 5, 5);
		gbc_lbl2.gridy = 2;
		lbl2 = new JLabel();
		lbl2.setText("Maximum depth:");
		controls.add(lbl2, gbc_lbl2);
                
		GridBagConstraints gbc_lblFormTraversiong = new GridBagConstraints();
		gbc_lblFormTraversiong.anchor = GridBagConstraints.EAST;
		gbc_lblFormTraversiong.insets = new Insets(5, 5, 5, 5);
		gbc_lblFormTraversiong.gridx = 0;
		gbc_lblFormTraversiong.gridy = 3;
		controls.add(getLblFormTraversiong(), gbc_lblFormTraversiong);
                
		GridBagConstraints gbc_form_traversing = new GridBagConstraints();
                gbc_form_traversing.anchor = GridBagConstraints.WEST;
		gbc_form_traversing.gridwidth = 3;
		gbc_form_traversing.insets = new Insets(5, 5, 5, 5);
		gbc_form_traversing.gridx = 2;
		gbc_form_traversing.gridy = 3;
		controls.add(getFormTraversing(), gbc_form_traversing);

		GridBagConstraints gbc_lbl3 = new GridBagConstraints();
		gbc_lbl3.anchor = GridBagConstraints.EAST;
		gbc_lbl3.insets = new Insets(5, 5, 5, 5);
		gbc_lbl3.gridx = 0;
		gbc_lbl3.gridy = 4;
		controls.add(getLbl3(), gbc_lbl3);
                
		GridBagConstraints gbc_keepContent = new GridBagConstraints();
		gbc_keepContent.anchor = GridBagConstraints.WEST;
		gbc_keepContent.insets = new Insets(5, 5, 5, 5);
		gbc_keepContent.gridwidth = 3;
		gbc_keepContent.gridx = 2;
		gbc_keepContent.gridy = 4;
		controls.add(getKeepContent(), gbc_keepContent);
		
		GridBagConstraints gbc_lblRobotstxt = new GridBagConstraints();
		gbc_lblRobotstxt.anchor = GridBagConstraints.EAST;
		gbc_lblRobotstxt.insets = new Insets(5, 5, 5, 5);
		gbc_lblRobotstxt.gridx = 0;
		gbc_lblRobotstxt.gridy = 5;
		controls.add(getLblRobotstxt(), gbc_lblRobotstxt);
                
		GridBagConstraints gbc_chckbxObeyRules = new GridBagConstraints();
		gbc_chckbxObeyRules.anchor = GridBagConstraints.WEST;
		gbc_chckbxObeyRules.insets = new Insets(5, 5, 5, 5);
		gbc_chckbxObeyRules.gridwidth = 3;
		gbc_chckbxObeyRules.gridx = 2;
		gbc_chckbxObeyRules.gridy = 5;
                controls.add(getObeyRobots(), gbc_chckbxObeyRules);
                // new CHD 13.01.2019
              
                GridBagConstraints gbc_lblSearchParameter = new GridBagConstraints();
		gbc_lblSearchParameter.anchor = GridBagConstraints.EAST;
		gbc_lblSearchParameter.insets = new Insets(5, 5, 5, 5);
		gbc_lblSearchParameter.gridx = 0;
		gbc_lblSearchParameter.gridy = 6;
		controls.add(getLblSearchParameter(), gbc_lblSearchParameter);
                
		GridBagConstraints gbc_tfSearchParameter = new GridBagConstraints();
		gbc_tfSearchParameter.weightx = 0.5;
		gbc_tfSearchParameter.gridwidth = 2;
		gbc_tfSearchParameter.insets = new Insets(5, 5, 5, 5);
		gbc_tfSearchParameter.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfSearchParameter.gridx = 2;
		gbc_tfSearchParameter.gridy = 6;
		controls.add(getTfSearchParameter(), gbc_tfSearchParameter);
                
                GridBagConstraints gbc_lblOutputDirectory = new GridBagConstraints();
		gbc_lblOutputDirectory.anchor = GridBagConstraints.EAST;
		gbc_lblOutputDirectory.insets = new Insets(5, 5, 5, 5);
		gbc_lblOutputDirectory.gridx = 0;
		gbc_lblOutputDirectory.gridy = 7;
		controls.add(getLblOutputDirectory(), gbc_lblOutputDirectory);
                
		GridBagConstraints gbc_tfOutputDirectory = new GridBagConstraints();
		gbc_tfOutputDirectory.weightx = 0.5;
		gbc_tfOutputDirectory.gridwidth = 2;
		gbc_tfOutputDirectory.insets = new Insets(5, 5, 5, 5);
		gbc_tfOutputDirectory.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfOutputDirectory.gridx = 2;
		gbc_tfOutputDirectory.gridy = 7;
		controls.add(getTfOutputDirectory() , gbc_tfOutputDirectory);
              
                 GridBagConstraints gbc_lblFollowLinkHTML = new GridBagConstraints();
		gbc_lblFollowLinkHTML.anchor = GridBagConstraints.EAST;
		gbc_lblFollowLinkHTML.insets = new Insets(5, 5, 5, 5);
		gbc_lblFollowLinkHTML.gridx = 0;
		gbc_lblFollowLinkHTML.gridy = 8;
		controls.add(getLblFollowLinkHTML() , gbc_lblFollowLinkHTML);
                
		GridBagConstraints gbc_cbxFollowLinkHTML = new GridBagConstraints();
		gbc_cbxFollowLinkHTML.anchor = GridBagConstraints.WEST;
		gbc_cbxFollowLinkHTML.insets = new Insets(5, 5, 5, 5);
		gbc_cbxFollowLinkHTML.gridwidth = 3;
		gbc_cbxFollowLinkHTML.gridx = 2;
		gbc_cbxFollowLinkHTML.gridy = 8;
                controls.add(getCbxFollowLinkHTML(), gbc_cbxFollowLinkHTML);
                
                GridBagConstraints gbc_lblFollowLinkIMG = new GridBagConstraints();
		gbc_lblFollowLinkIMG.anchor = GridBagConstraints.EAST;
		gbc_lblFollowLinkIMG.insets = new Insets(5, 5, 5, 5);
		gbc_lblFollowLinkIMG.gridx = 0;
		gbc_lblFollowLinkIMG.gridy = 9;
		controls.add(getLblFollowLinkIMG() , gbc_lblFollowLinkIMG);
                
		GridBagConstraints gbc_cbxFollowLinkIMG = new GridBagConstraints();
		gbc_cbxFollowLinkIMG.anchor = GridBagConstraints.WEST;
		gbc_cbxFollowLinkIMG.insets = new Insets(5, 5, 5, 5);
		gbc_cbxFollowLinkIMG.gridwidth = 3;
		gbc_cbxFollowLinkIMG.gridx = 2;
		gbc_cbxFollowLinkIMG.gridy = 9;
                controls.add(getCbxFollowLinkIMG(), gbc_cbxFollowLinkIMG);
                
                GridBagConstraints gbc_lblSaveResource = new GridBagConstraints();
		gbc_lblSaveResource.anchor = GridBagConstraints.EAST;
		gbc_lblSaveResource.insets = new Insets(5, 5, 5, 5);
		gbc_lblSaveResource.gridx = 0;
		gbc_lblSaveResource.gridy = 10;
		controls.add(getLblSaveResource() , gbc_lblSaveResource);
                
		GridBagConstraints gbc_cbxSaveResource = new GridBagConstraints();
		gbc_cbxSaveResource.anchor = GridBagConstraints.WEST;
		gbc_cbxSaveResource.insets = new Insets(5, 5, 5, 5);
		gbc_cbxSaveResource.gridwidth = 3;
		gbc_cbxSaveResource.gridx = 2;
		gbc_cbxSaveResource.gridy = 10;
                controls.add(getCbxSaveResource(), gbc_cbxSaveResource);
                
                GridBagConstraints gbc_lblSaveIMG = new GridBagConstraints();
		gbc_lblSaveIMG.anchor = GridBagConstraints.EAST;
		gbc_lblSaveIMG.insets = new Insets(5, 5, 5, 5);
		gbc_lblSaveIMG.gridx = 0;
		gbc_lblSaveIMG.gridy = 11;
		controls.add(getLblSaveIMG() , gbc_lblSaveIMG);
                
         	GridBagConstraints gbc_cbxSaveIMG = new GridBagConstraints();
		gbc_cbxSaveIMG.anchor = GridBagConstraints.WEST;
		gbc_cbxSaveIMG.insets = new Insets(5, 5, 5, 5);
		gbc_cbxSaveIMG.gridwidth = 3;
		gbc_cbxSaveIMG.gridx = 2;
		gbc_cbxSaveIMG.gridy = 11;
                controls.add(getCbxSaveIMG(), gbc_cbxSaveIMG); 
             
                GridBagConstraints gbc_lblExtractEMailAddress = new GridBagConstraints();
		gbc_lblExtractEMailAddress.anchor = GridBagConstraints.EAST;
		gbc_lblExtractEMailAddress.insets = new Insets(5, 5, 5, 5);
		gbc_lblExtractEMailAddress.gridx = 0;
		gbc_lblExtractEMailAddress.gridy = 12;
		controls.add(getLblExtractEMailAddress() , gbc_lblExtractEMailAddress);
                
         	GridBagConstraints gbc_cbxExtractEMailAddress= new GridBagConstraints();
		gbc_cbxExtractEMailAddress.anchor = GridBagConstraints.WEST;
		gbc_cbxExtractEMailAddress.insets = new Insets(5, 5, 5, 5);
		gbc_cbxExtractEMailAddress.gridwidth = 3;
		gbc_cbxExtractEMailAddress.gridx = 2;
		gbc_cbxExtractEMailAddress.gridy = 12;
                controls.add(getCbxExtractEMailAddress(), gbc_cbxExtractEMailAddress); 
                
                GridBagConstraints gbc_lblFilterIMGLink = new GridBagConstraints();
		gbc_lblFilterIMGLink.anchor = GridBagConstraints.EAST;
		gbc_lblFilterIMGLink.insets = new Insets(5, 5, 5, 5);
		gbc_lblFilterIMGLink.gridx = 0;
		gbc_lblFilterIMGLink.gridy = 14;
		controls.add(getLblFilterIMGLink() , gbc_lblFilterIMGLink);
                
         	GridBagConstraints gbc_cbxFilterIMGLink= new GridBagConstraints();
		gbc_cbxFilterIMGLink.anchor = GridBagConstraints.WEST;
		gbc_cbxFilterIMGLink.insets = new Insets(5, 5, 5, 5);
		gbc_cbxFilterIMGLink.gridwidth = 3;
		gbc_cbxFilterIMGLink.gridx = 2;
		gbc_cbxFilterIMGLink.gridy = 14;
                controls.add(getCbxFilterIMGLink(), gbc_cbxFilterIMGLink); 
                
                GridBagConstraints gbc_lblFilterScriptLink = new GridBagConstraints();
		gbc_lblFilterScriptLink.anchor = GridBagConstraints.EAST;
		gbc_lblFilterScriptLink.insets = new Insets(5, 5, 5, 5);
		gbc_lblFilterScriptLink.gridx = 0;
		gbc_lblFilterScriptLink.gridy = 15;
		controls.add(getLblFilterScriptLink() , gbc_lblFilterScriptLink);
                
         	GridBagConstraints gbc_cbxFilterScriptLink= new GridBagConstraints();
		gbc_cbxFilterScriptLink.anchor = GridBagConstraints.WEST;
		gbc_cbxFilterScriptLink.insets = new Insets(5, 5, 5, 5);
		gbc_cbxFilterScriptLink.gridwidth = 3;
		gbc_cbxFilterScriptLink.gridx = 2;
		gbc_cbxFilterScriptLink.gridy = 15;
                controls.add(getCbxFilterScriptLink(), gbc_cbxFilterScriptLink); 
                
                // new CHD End 13.01.2019
		GridBagConstraints gbc_exclude_scroll = new GridBagConstraints();
		gbc_exclude_scroll.weighty = 1.0;
		gbc_exclude_scroll.gridwidth = 5;
		gbc_exclude_scroll.insets = new Insets(5, 5, 5, 5);
		gbc_exclude_scroll.fill = GridBagConstraints.BOTH;
		gbc_exclude_scroll.gridx = 0;
		gbc_exclude_scroll.gridy = 16;
		controls.add(getExclude_scroll(), gbc_exclude_scroll);
		
		getContentPane().add(controls, BorderLayout.CENTER);
		getContentPane().add(getButtons(), BorderLayout.SOUTH);
		
		this.btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					SiteOpenDlg.this.document.setRoot(new WebTreeNode(new URL(getSiteURL())));
					SiteOpenDlg.this.document.setMaxDepth(getDepth().getValue());
					SiteOpenDlg.this.document.setContentInMemory(getKeepContent().isSelected());
					SiteOpenDlg.this.document.setObeyRobots(getObeyRobots().isSelected());
					SiteOpenDlg.this.document.setFormTraversing(getFormTraversing().isSelected());
					SiteOpenDlg.this.document.setExcludeURLs(getExcludes().getText());
                                        
                                        SiteOpenDlg.this.document.setSearchParameter(getTfSearchParameter().getText());
                                        SiteOpenDlg.this.document.setOutputDirectory(getTfOutputDirectory().getText());
                                        SiteOpenDlg.this.document.setFollowLinkHTML(getCbxFollowLinkHTML().isSelected());
                                        SiteOpenDlg.this.document.setFollowLinkIMG(getCbxFollowLinkIMG().isSelected());
                                        SiteOpenDlg.this.document.setSaveResource(getCbxSaveResource().isSelected());
                                        SiteOpenDlg.this.document.setSaveIMG(getCbxSaveIMG().isSelected());
                                        SiteOpenDlg.this.document.setExtractEMailAddress(getCbxExtractEMailAddress().isSelected());
                                        
                                        SiteOpenDlg.this.document.setFilterIMGLink(getCbxFilterIMGLink().isSelected());
                                        SiteOpenDlg.this.document.setFilterScriptLink(getCbxFilterScriptLink().isSelected());
                                        
					// Si cambió el usuario o escribió una clave
					if (!old_username.equals(getUsername().getText()) || getPassword().getPassword().length > 0)
						SiteOpenDlg.this.document.setUsernameAndPassword(getUsername().getText(), getPassword().getPassword());
					
					DRKTools.prefs.put("last_url", getSiteURL());
					DRKTools.prefs.putInt("last_depth", getDepth().getValue());
					DRKTools.prefs.putBoolean("keep_content", getKeepContent().isSelected());
					DRKTools.prefs.putBoolean("obey_robots", getObeyRobots().isSelected());
					DRKTools.prefs.putBoolean("form_traversing", getFormTraversing().isSelected());
					DRKTools.prefs.put("excludes", getExcludes().getText());
					DRKTools.prefs.put("username", getUsername().getText());
                                        
                                        // new CHD 14.01.2019
                                        
                                        DRKTools.prefs.put("search_parameter", getTfSearchParameter().getText());
                                        DRKTools.prefs.put("output_directory", getTfOutputDirectory().getText());
                                        DRKTools.prefs.putBoolean("follow_linkHTML",getCbxFollowLinkHTML().isSelected());
                                        DRKTools.prefs.putBoolean("follow_linkIMG", getCbxFollowLinkIMG().isSelected());
                                        DRKTools.prefs.putBoolean("save_HTML", getCbxSaveResource().isSelected());
                                        DRKTools.prefs.putBoolean("save_IMG", getCbxSaveIMG().isSelected());
                                        DRKTools.prefs.putBoolean("extractEMail", getCbxExtractEMailAddress().isSelected());
                                        
                                        DRKTools.prefs.putBoolean("filter_IMG", getCbxFilterIMGLink().isSelected());
                                        DRKTools.prefs.putBoolean("filter_Script", getCbxFilterScriptLink().isSelected());
                                        
					ok = true;
					SiteOpenDlg.this.dispatchEvent(new WindowEvent(SiteOpenDlg.this, WindowEvent.WINDOW_CLOSING));
					
				} catch (MalformedURLException e1) {
					JOptionPane.showMessageDialog(SiteOpenDlg.this, "Please enter a valid website address", "Malformed address", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		this.btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SiteOpenDlg.this.dispatchEvent(new WindowEvent(SiteOpenDlg.this, WindowEvent.WINDOW_CLOSING));
			}
		});

		
		getRootPane().setDefaultButton(getOk());
		pack();
		setLocation(DRKTools.getCenterLocation(this));
	}
	
	public void showNew() {
		this.setTitle("Crawling parameters for new website...");
		this.document = new WebTreeModel(new WebTreeNode());
		getUrl().setText(DRKTools.prefs.get("last_url", "http://"));
		getDepth().setValue(DRKTools.prefs.getInt("last_depth", 3));
		getKeepContent().setSelected(DRKTools.prefs.getBoolean("keep_content", false));
		getObeyRobots().setSelected(DRKTools.prefs.getBoolean("obey_robots", true));
		getFormTraversing().setSelected(DRKTools.prefs.getBoolean("form_traversing", false));
		getExcludes().setText(DRKTools.prefs.get("excludes", ""));
		getUsername().setText(DRKTools.prefs.get("username", ""));
                
                getTfSearchParameter().setText(DRKTools.prefs.get("search_parameter", ""));
                getTfOutputDirectory().setText(DRKTools.prefs.get("output_directory", "C:/Output/"));
                getCbxFollowLinkHTML().setSelected(DRKTools.prefs.getBoolean("follow_linkHTML", false));
                getCbxFollowLinkIMG().setSelected(DRKTools.prefs.getBoolean("follow_linkIMG", false));
                getCbxSaveResource().setSelected(DRKTools.prefs.getBoolean("save_HTML", false));
                getCbxSaveIMG().setSelected(DRKTools.prefs.getBoolean("save_IMG", false));
                getCbxExtractEMailAddress().setSelected(DRKTools.prefs.getBoolean("extractEMail", false));
                
                getCbxFilterIMGLink().setSelected(DRKTools.prefs.getBoolean("filter_IMG", true));
                getCbxFilterScriptLink().setSelected(DRKTools.prefs.getBoolean("filter_Script", false));
                
                                
		setVisible(true);
	}
	
	public void showModify(WebTreeModel document) {
		this.setTitle("Modify crawling parameters...");
		this.document = document;
		getUrl().setText(this.document.getRootNode().getURL().toString());
		getDepth().setValue(this.document.getMaxDepth());
		getKeepContent().setSelected(this.document.isContentInMemory());
		getObeyRobots().setSelected(this.document.getRobots() != null);
		getFormTraversing().setSelected(this.document.isFormTraversing());
		getExcludes().setText(this.document.getExcludeURLs());
		getUsername().setText(this.document.getUsername());
		this.old_username  = this.document.getUsername();
                
                // new CHD 14.01.2019
                getTfSearchParameter().setText(this.document.getSearchParameter());
                getTfOutputDirectory().setText(this.document.getOutputDirectory());
                getCbxFollowLinkHTML().setSelected(this.document.isFollowLinkHTML());
                getCbxFollowLinkIMG().setSelected(this.document.isFollowLinkIMG());
                getCbxSaveResource().setSelected(this.document.isSaveResource());
                getCbxSaveIMG().setSelected(this.document.isSaveIMG());
                getCbxExtractEMailAddress().setSelected(this.document.isExtractEMailAddress());
                
                getCbxFilterIMGLink().setSelected(this.document.isFilterIMGLink());
                getCbxFilterScriptLink().setSelected(this.document.isFilterScriptLink());
                
		setVisible(true);
	}

	/**
	 * This method initializes buttons	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtons() {
		if (buttons == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.RIGHT);
			flowLayout.setVgap(4);
			flowLayout.setHgap(4);
			buttons = new JPanel();
			buttons.setLayout(flowLayout);
			buttons.add(getOk(), null);
			buttons.add(getCancel(), null);
		}
		return buttons;
	}

	/**
	 * This method initializes ok	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOk() {
		if (btnOk == null) {
			btnOk = new JButton();
			btnOk.setText("OK");
		}
		return btnOk;
	}

	/**
	 * This method initializes cancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton();
			btnCancel.setText("Cancel");
		}
		return btnCancel;
	}

	/**
	 * This method initializes url	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getUrl() {
		if (url == null) {
			url = new JTextField();
			url.setColumns(30);
		}
		return url;
	}

	public String getSiteURL() {
		return url.getText();
	}

	/**
	 * This method initializes slide_depth	
	 * 	
	 * @return javax.swing.JSlider	
	 */
	private JSlider getDepth() {
		if (slide_depth == null) {
			slide_depth = new JSlider();
			slide_depth.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					depth_lbl.setText(Integer.toString(slide_depth.getValue()));
				}
			});
			slide_depth.setMaximum(15);
			slide_depth.setValue(3);
			slide_depth.setSnapToTicks(true);
			slide_depth.setPaintTicks(true);
			slide_depth.setPaintLabels(true);
			slide_depth.setPaintTrack(true);
			slide_depth.setMinimum(1);
		}
		return slide_depth;
	}

	private JCheckBox getKeepContent() {
		if (keepContent == null) {
			keepContent = new JCheckBox("keep content in memory");
		}
		return keepContent;
	}
	private JLabel getLbl3() {
		if (lbl3 == null) {
			lbl3 = new JLabel("Downloaded content:");
		}
		return lbl3;
	}
	
	private JLabel getLblRobotstxt() {
		if (lblRobotstxt == null) {
			lblRobotstxt = new JLabel("robots.txt:");
		}
		return lblRobotstxt;
	}
	
	private JCheckBox getObeyRobots() {
		if (obeyRobots == null) {
			obeyRobots = new JCheckBox("obey rules");
		}
		return obeyRobots;
	}
	
	public WebTreeModel getDocument() {
		return document;
	}

	private JScrollPane getExclude_scroll() {
		if (exclude_scroll == null) {
			exclude_scroll = new JScrollPane();
			exclude_scroll.setViewportBorder(new TitledBorder(null, "Exclude URLs", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			exclude_scroll.setViewportView(getExcludes());
		}
		return exclude_scroll;
	}
	private JTextArea getExcludes() {
		if (excludes == null) {
			excludes = new JTextArea();
			excludes.setToolTipText("Insert one regular expression per row");
			excludes.setRows(5);
		}
		return excludes;
	}
	private JLabel getLblUsernamepassword() {
		if (lblUsernamepassword == null) {
			lblUsernamepassword = new JLabel("Username/password:");
		}
		return lblUsernamepassword;
	}
	private JTextField getUsername() {
		if (username == null) {
			username = new JTextField();
			username.setToolTipText("Leave this field empty if no authentication is required. On edit, leave this field unchanged for keeping previously entered password.");
			username.setColumns(8);
		}
		return username;
	}
	private JPasswordField getPassword() {
		if (password == null) {
			password = new JPasswordField();
			password.setToolTipText("Leave this field empty for keeping previously entered password");
			password.setColumns(10);
		}
		return password;
	}
	private JCheckBox getFormTraversing() {
		if (form_traversing == null) {
			form_traversing = new JCheckBox("Treat forms as links");
			form_traversing.setToolTipText("If checked, the crawler will follow URLs found in forms as it were links. Turn this on if you know what you are doing only.");
		}
		return form_traversing;
	}
	private JLabel getLblFormTraversiong() {
		if (lblFormTraversiong == null) {
			lblFormTraversiong = new JLabel("Form traversing:");
		}
		return lblFormTraversiong;
	}
        // new CHD 13.01.2019
       	private JCheckBox getCbxFollowLinkHTML() {
		if (cbxFollowLinkHTML == null) {
			cbxFollowLinkHTML = new JCheckBox("follow HTML links");
			cbxFollowLinkHTML.setToolTipText("If checked, the crawler will follow HTML links");
                }       
                return cbxFollowLinkHTML;
                
	}
	private JLabel getLblFollowLinkHTML() {
		if (lblFollowLinkHTML == null) {
			lblFollowLinkHTML = new JLabel("follow HTML links:");
		}
		return lblFollowLinkHTML;
	}
        
       
        private JCheckBox getCbxFollowLinkIMG() {
		if (cbxFollowLinkIMG == null) {
			cbxFollowLinkIMG = new JCheckBox("follow Image links");
			cbxFollowLinkIMG.setToolTipText("If checked, the crawler will follow Image links.");
		}
		return cbxFollowLinkIMG;
	}
	private JLabel getLblFollowLinkIMG() {
		if (lblFollowLinkIMG == null) {
			lblFollowLinkIMG = new JLabel("follow Image links:");
		}
		return lblFollowLinkIMG;
	}
        
        private JCheckBox getCbxSaveResource() {
		if (cbxSaveResource == null) {
			cbxSaveResource = new JCheckBox("Save HTML files");
			cbxSaveResource.setToolTipText("If checked, the crawler will save resource files");
		}
		return cbxSaveResource;
	}
	private JLabel getLblSaveResource() {
		if (lblSaveResource== null) {
			lblSaveResource = new JLabel("Save HTML files");
		}
		return lblSaveResource;
	}
        
        private JCheckBox getCbxSaveIMG() {
		if (cbxSaveIMG == null) {
			cbxSaveIMG = new JCheckBox("Save image files");
			cbxSaveIMG.setToolTipText("If checked, the crawler will save image files");
		}
		return cbxSaveIMG;
	}
	private JLabel getLblSaveIMG() {
		if (lblSaveIMG== null) {
			lblSaveIMG = new JLabel("Save image files");
		}
		return lblSaveIMG;
	}
        
        private JCheckBox getCbxExtractEMailAddress() {
		if (cbxExtractEMailAddress == null) {
			cbxExtractEMailAddress = new JCheckBox("Extract eMails");
			cbxExtractEMailAddress.setToolTipText("If checked, eMails will be exporteds");
		}
		return cbxExtractEMailAddress;
	}
	private JLabel getLblExtractEMailAddress() {
		if (lblExtractEMailAddress== null) {
                    lblExtractEMailAddress = new JLabel("Extract eMails");
		}
		return lblExtractEMailAddress;
	}
        
        private JLabel getLblSearchParameter() {
		if (lblSearchParamter == null) {
			lblSearchParamter = new JLabel("Search Parameter:");
		}
		return lblSearchParamter;
	}
	private JTextField getTfSearchParameter() {
		if (tfSearchParameter == null) {
			tfSearchParameter = new JTextField();
			tfSearchParameter.setToolTipText("Search Parameter - regex expression possible.");
                        tfSearchParameter.setColumns(30);
		}
		return tfSearchParameter;
	}

        private JLabel getLblOutputDirectory() {
		if (lblOutputDirectory == null) {
			lblOutputDirectory = new JLabel("Output Directory:");
		}
		return lblOutputDirectory;
	}
	private JTextField getTfOutputDirectory() {
		if (tfOutputDirectory == null) {
			tfOutputDirectory = new JTextField();
			tfOutputDirectory.setToolTipText("Output Directory length <= 250 characters.");
                        tfOutputDirectory.setColumns(30);
		}
		return tfOutputDirectory;
	}
        
         private JCheckBox getCbxFilterIMGLink() {
		if (cbxFilterIMGLink == null) {
			cbxFilterIMGLink = new JCheckBox("Filter Image links");
			cbxFilterIMGLink.setToolTipText("If checked, Image Links will be inserted into the JTree.");
		}
		return cbxFilterIMGLink;
	}
	private JLabel getLblFilterIMGLink() {
		if (lblFilterIMGLink == null) {
                    lblFilterIMGLink = new JLabel("Filter Image link");
		}
		return lblFilterIMGLink;
	}
        
        private JCheckBox getCbxFilterScriptLink() {
		if (cbxFilterScriptLink == null) {
			cbxFilterScriptLink = new JCheckBox("Filter Script links");
			cbxFilterScriptLink.setToolTipText("If checked, Script Links will be inserted into the JTree.");
		}
		return cbxFilterScriptLink;
	}
	private JLabel getLblFilterScriptLink() {
		if (lblFilterScriptLink == null) {
                    lblFilterScriptLink = new JLabel("Filter Script link");
		}
		return lblFilterScriptLink;
	}

}