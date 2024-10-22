package com.droegec.mwc.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

import com.droegec.mwc.DRKTools;
import com.droegec.mwc.WorkManager;
import com.droegec.mwc.exceptions.CrawlerInterruptedException;
import com.droegec.mwc.gui.actions.CopyNodeURL;
import com.droegec.mwc.gui.actions.ExportTree;
import com.droegec.mwc.gui.actions.GoogleSearch;
import com.droegec.mwc.gui.actions.SEOReview;
import com.droegec.mwc.gui.actions.SearchLinksTo;
import com.droegec.mwc.gui.actions.SearchNodeGoogleLinks;
import com.droegec.mwc.gui.actions.SearchNodes;
import com.droegec.mwc.gui.actions.ShowDeepErrorNodes;
import com.droegec.mwc.gui.actions.ShowErrorLinks;
import com.droegec.mwc.gui.actions.ShowNodeInBrowser;
import com.droegec.mwc.gui.actions.SiteWideSeoReview;
import com.droegec.mwc.gui.actions.SourceLinks;
import com.droegec.mwc.gui.actions.TreeLoad;
import com.droegec.mwc.gui.actions.TreeSave;
import com.droegec.mwc.gui.panes.InfoPane;
import com.droegec.mwc.gui.panes.InfoPaneFactory;
import com.droegec.mwc.model.DRKLink;
import com.droegec.mwc.model.NodeTextLocation;
import com.droegec.mwc.model.WebTreeModel;
import com.droegec.mwc.model.WebTreeNode;

public class MainWindow extends JFrame implements WorkManager, ClipboardOwner, ListSelectionListener, TreeModelListener {
	private static final long serialVersionUID = 2541699256522264375L;

	/*
	 * GUI
	 */
	private static final int PROGRESS_WIDTH = 130;
	private static final int MINIMUM_PANEL_HEIGHT = 750;

	/*
	 * APP
	 */
	public static final String APP_NAME_AND_VERSION = "DRKSpiderJava v0.84";
	public static final String APP_NAME_AND_WEBSITE = "DRKSpiderJava by Leandro Fernández - http://www.drk.com.ar";
	public static final String UPDATE_URL = "http://www.drk.com.ar/spider.php?version";
	public static final String DOWNLOAD_URL = "http://www.drk.com.ar/spider.php";
	public static final String USER_AGENT = "DRKSpiderJava ("+APP_NAME_AND_VERSION+" - http://www.drk.com.ar)";
	
	private static final int MAX_RESULT_ITEMS = 6;
	
	private Object keepWorkingMutex = new Object();
	private CheckWebUpdates webCheck;
	private NodeTextLocation gotoContent;
	private DRKLink gotoLink = null;
	private boolean pause = false;
	private Thread worker = null;
	private boolean browsing = false;
	private boolean keepWorking = true;
	private Integer link_count = 0;
	private long start_time;
	private WebTreeModel document = null;
	
	private JPanel jContentPane = null;
	private JToolBar toolbar = null;
	private JPanel mainpanel = null;
	private JSplitPane vsplit = null;
	private JTree tree = null;
	private JMenuBar mainmenu = null;
	private JMenu file_menu = null;
	private JMenuItem fopen_mi = null;
	private JPanel status_panel = null;
	private JProgressBar progress = null;
	private JLabel status = null;
	private JMenuItem fstop_mi = null;
	private JPanel rpanel = null;
	private JPanel toppanel = null;
	private JScrollPane infopanel = null;
	private JTextField urlbox = null;
	private JLabel lbl_url = null;
	private JScrollPane tree_scroll = null;
	final private Action startCrawler;
	final private Action stopCrawler;
	final private Action act_csearch;
	final private Action act_cclrsearch;
	final private Action actionSEO;
	final private Action searchSiteInGoogle;
	final private Action errorsDialog;
	final private Action deepErrorsNodes;
	final private Action sourceLinks;
	final private Action exportDialog;
	final private Action copyNodeURL;
	final private Action siteWideSeoReview;
	final private Action showNodeInBrowser;
	final private Action searchNodeGoogleLinks;
	final private Action searchLinksTo;
	final private Action searchNodes;
	final private Action treeSave;
	final private Action treeLoad;
	final private Action recheckBrokenLinks;
	final private Action recrawl;
	final private Action crawlParameters;
	final private Action siteSummary;
	final private Action settingsAction;
	private Set<Action> singleSelectionActions = new HashSet<Action>(); 
	private Set<Action> allActions = new HashSet<Action>(); 
	private JMenu content_menu;
	private JMenuItem search_mi;
	private JList<NodeTextLocation> foundItems;
	private JScrollPane scrollPane;
	private List<NodeTextLocation> foundNodes = null;
	private FoundListModel foundItemsModel = new FoundListModel();
	private ContentSearchDlg searchDlg;
	private JMenuItem clr_search_mi;
	private JMenu tree_menu;
	private JMenuItem errornodes_mi;
	private JMenuItem searchSiteInGoogle_mi;
	private JMenuItem errorlinks_mi;
	private JMenuItem nodeexport_mi;
	private InfoPane current_pane;
	private JMenuItem searchLinks_mi;
	private JMenu global_menu;
	private JMenuItem about_mi;
	private JMenuItem settings_mi;
	private JMenuItem seoreview_mi;
	private JMenuItem inBrowser_mi;
	private JMenuItem googleLinks_mi;
	private JMenuItem mntmSave;
	private JMenuItem mntmLoad;
	private JMenuItem recheckBroken_mi;
	private JMenuItem recrawl_mi;
	private JMenuItem feedback_mi;
	private JLabel lbl_modified;
	private JTextField last_modified;
	private JLabel total_links;
	private JLabel speed;
	private JSeparator separator;
	private JSeparator separator_1;
	private JSeparator separator_2;

	protected double crawl_speed = 0.0;
	private JProgressBar overallProgress;
	private JMenuItem craw_parameters_mi;
	private JMenuItem sitesummary_mi;
	private JMenuItem seoReview_mi;
	private JMenuItem help_mi;
	private JMenuItem sourcelinks_mi;

	
	/**
	 * This method initializes 
	 * 
	 */
	public MainWindow() {
		super();
		
	   setIconImage(new ImageIcon("/images/logo.png").getImage());
		
		startCrawler = new StartCrawler();
		stopCrawler = new StopCrawler();
		act_csearch = new CSearch();
		act_cclrsearch = new CClrSearch();
		actionSEO = new SEOReview(this, "Seo Review");
		actionSEO.setEnabled(false);
		searchSiteInGoogle = new GoogleSearch(this, "Search within site (Google search)...");
		searchSiteInGoogle.setEnabled(false);
		errorsDialog = new ShowErrorLinks(this, "Show error links...");
		errorsDialog.setEnabled(false);
		searchLinksTo = new SearchLinksTo(this, "Search links...");
		searchLinksTo.setEnabled(false);
		searchNodes = new SearchNodes(this, "Search nodes...");
		searchNodes.setEnabled(false);
		deepErrorsNodes = new ShowDeepErrorNodes(this, "Show deep error nodes...");
		deepErrorsNodes.setEnabled(false);
		sourceLinks =  new SourceLinks(this, "Source links...");
		sourceLinks.setEnabled(false);
		exportDialog = new ExportTree(this, "Export...");
		exportDialog.setEnabled(false);
		copyNodeURL = new CopyNodeURL(this, "Copy URL");
		showNodeInBrowser = new ShowNodeInBrowser(this, "Open in browser...");
		showNodeInBrowser.setEnabled(false);
		searchNodeGoogleLinks = new SearchNodeGoogleLinks(this, "Get Google links...");
		searchNodeGoogleLinks.setEnabled(false);
		siteWideSeoReview = new SiteWideSeoReview(this, "Site wide SEO score");
		siteWideSeoReview.setEnabled(false);
		treeSave = new TreeSave(this,  "Save...");
		treeSave.setEnabled(false);
		treeLoad = new TreeLoad(this, "Load...");
		treeLoad.setEnabled(false);
		recheckBrokenLinks = new RecheckBrokenLinks();
		recheckBrokenLinks.setEnabled(false);
		recrawl =  new Recrawl();
		recrawl.setEnabled(false);
		crawlParameters = new CrawlParameters();
		crawlParameters.setEnabled(false);
		siteSummary = new SiteSummary();
		siteSummary.setEnabled(false);
		settingsAction = new SettingsAction();
		settingsAction.setEnabled(false);
		
		singleSelectionActions.add(actionSEO);
		singleSelectionActions.add(searchSiteInGoogle);
		singleSelectionActions.add(errorsDialog);
		singleSelectionActions.add(deepErrorsNodes);
		singleSelectionActions.add(searchLinksTo);
		singleSelectionActions.add(searchNodes);
		singleSelectionActions.add(sourceLinks);
		singleSelectionActions.add(exportDialog);
		singleSelectionActions.add(copyNodeURL);
		singleSelectionActions.add(showNodeInBrowser);
		singleSelectionActions.add(searchNodeGoogleLinks);
		
		allActions.addAll(singleSelectionActions);
		allActions.add(settingsAction);
		allActions.add(startCrawler);
		allActions.add(stopCrawler);
		allActions.add(act_cclrsearch);
		allActions.add(act_csearch);
		allActions.add(siteWideSeoReview);
		allActions.add(treeSave);
		allActions.add(treeLoad);
		allActions.add(recheckBrokenLinks);
		allActions.add(recrawl);
		allActions.add(crawlParameters);
		allActions.add(siteSummary);
		
		document = new WebTreeModel(new WebTreeNode());

		initialize();
		
		tree.setModel(document);
		tree.setCellRenderer(new SpiderTreeCellRederer());
		
		refreshGUI();
		
		setLocation(DRKTools.prefs.getInt("window.main.x", 10), DRKTools.prefs.getInt("window.main.y", 10));
		setSize(DRKTools.prefs.getInt("window.main.w", 800), DRKTools.prefs.getInt("window.main.h", 600));
		
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				DRKTools.prefs.putInt("window.main.x", getLocation().x);
				DRKTools.prefs.putInt("window.main.y", getLocation().y);
				DRKTools.prefs.putInt("window.main.w", getSize().width);
				DRKTools.prefs.putInt("window.main.h", getSize().height);
				super.windowClosing(e);
			}
		});
		
		webCheck = new CheckWebUpdates(this);
		webCheck.execute();
	}

	public WebTreeModel getDocument() {
		return document;
	}

	public void setDocument(WebTreeModel document) {
		this.document = document;
		this.document.setAuthenticationHandler();
		link_count = this.document.getTotalNodeCount();
		this.document.addProgressListener(this);
		this.document.addTreeModelListener(this);
		tree.setModel(this.document);
		
		refreshGUI();
	}

	protected void enableAllActions(boolean enable) {
		for (Action a : allActions) {
			a.setEnabled(enable);
		}
	}
	protected void enableContextActions() {
		for (Action a : singleSelectionActions) {
			a.setEnabled(!browsing && tree.getSelectionCount() == 1);
		} 
		actionSEO.setEnabled(!browsing && getSelectedNode() != null && getSelectedNode().getContentType() != null && getSelectedNode().getContentType().startsWith("text/html"));
	}
	
	protected void refreshGUI() {
		getTree().repaint();
		tree_scroll.invalidate();
		startCrawler.setEnabled(!browsing);
		stopCrawler.setEnabled(browsing);
		recrawl.setEnabled(!browsing && document.isParsed());
		recheckBrokenLinks.setEnabled(!browsing && document.isParsed());
		act_csearch.setEnabled(document.isContentInMemory());
		act_cclrsearch.setEnabled(document.isContentInMemory());
		siteWideSeoReview.setEnabled(document.isParsed() && !browsing);
		treeLoad.setEnabled(!browsing);
		treeSave.setEnabled(document.isParsed());
		crawlParameters.setEnabled(!browsing);
		siteSummary.setEnabled(document.isParsed() && !browsing);
		settingsAction.setEnabled(!browsing);
		enableContextActions();
		
		overallProgress.setVisible(browsing);
		
		// Status
		total_links.setText(link_count+" links found");
		speed.setText(DRKTools.decimal_two.format(crawl_speed)+" links per second");
	}
	
	public void guiPause() {
		if (!pause) {
			pause  = true;
			progress.setIndeterminate(true);
			enableAllActions(false);
		}
	}
	public void guiContinue() {
		if (pause) {
			pause = false;
			progress.setIndeterminate(false);
			refreshGUI();
		}
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setTitle(MainWindow.APP_NAME_AND_VERSION);
        this.setJMenuBar(getMainmenu());
        this.setContentPane(getJContentPane());
        this.setSize(new Dimension(800, 600));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			
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
			jContentPane.add(getToolbar(), java.awt.BorderLayout.NORTH);
			jContentPane.add(getMainpanel(), java.awt.BorderLayout.CENTER);
			jContentPane.add(getStatus_panel(), java.awt.BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes toolbar	
	 * 	
	 * @return javax.swing.JToolBar	
	 */
	private JToolBar getToolbar() {
		if (toolbar == null) {
			toolbar = new JToolBar();
			toolbar.add(startCrawler);
			toolbar.add(stopCrawler);
			toolbar.add(recrawl);
			toolbar.add(recheckBrokenLinks);
			toolbar.addSeparator();
			toolbar.add(showNodeInBrowser);
			toolbar.add(searchNodeGoogleLinks);
			toolbar.add(actionSEO);
			toolbar.add(searchSiteInGoogle);
		}
		return toolbar;
	}

	/**
	 * This method initializes mainpanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getMainpanel() {
		if (mainpanel == null) {
			mainpanel = new JPanel();
			mainpanel.setLayout(new BorderLayout());
			mainpanel.add(getVsplit(), java.awt.BorderLayout.CENTER);
		}
		return mainpanel;
	}

	/**
	 * This method initializes vsplit	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getVsplit() {
		if (vsplit == null) {
			vsplit = new JSplitPane();
			vsplit.setDividerLocation(225);
			vsplit.setRightComponent(getRpanel());
			vsplit.setLeftComponent(getTree_scroll());
		}
		return vsplit;
	}

	/**
	 * This method initializes tree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getTree() {
		if (tree == null) {
			tree = new JTree();
			tree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
				public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
					if (!browsing && tree.getSelectionCount() > 0) {
						showNode(getSelectedNode());
					}
					enableContextActions();
				}
			});
			tree.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (!browsing && e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1) {
						showTreePopupMenu();
					}
				}
			});
		}
		return tree;
	}

	protected void showTreePopupMenu() {
		if (tree.getSelectionCount() > 0) {
			JPopupMenu menu = new JPopupMenu();
			menu.add(new JMenuItem(searchLinksTo));
			menu.add(new JMenuItem(searchNodes));
			menu.add(new JMenuItem(errorsDialog));
			menu.add(new JMenuItem(deepErrorsNodes));
			menu.add(new JMenuItem(sourceLinks));
			menu.add(new JMenuItem(exportDialog));
			menu.add(new JMenuItem(copyNodeURL));
			menu.add(new JMenuItem(showNodeInBrowser));
			menu.add(new JMenuItem(searchNodeGoogleLinks));
			menu.add(new JMenuItem(searchSiteInGoogle));
			menu.add(new JMenuItem(actionSEO));
			menu.show(this, this.getMousePosition().x, this.getMousePosition().y);
		}
	}

	public WebTreeNode getSelectedNode() {
		return (isThereSelectedNode())?(WebTreeNode)tree.getSelectionPath().getLastPathComponent():null;
	}
	public boolean isThereSelectedNode() {
		return !tree.isSelectionEmpty();
	}
	private void showNode(WebTreeNode node) {
		urlbox.setText(node.getURL().toString());
		last_modified.setText(node.getLastModificationString());
		infopanel.getViewport().removeAll();
		current_pane = InfoPaneFactory.getInfoPane(this, node, this);
		int height = (int)(infopanel.getViewport().getSize().height*1.5);
		current_pane.setPreferredSize(new Dimension(infopanel.getViewport().getSize().width, (height > MINIMUM_PANEL_HEIGHT)?height:MINIMUM_PANEL_HEIGHT ));
		infopanel.setViewportView(current_pane);
		validate();
		if (gotoLink != null) {
			current_pane.locateLink(gotoLink);
			gotoLink = null;
		}
		if (gotoContent != null) {
			current_pane.locateContent(gotoContent);
			gotoContent = null;
		}
	}
	
	public void gotoNode(WebTreeNode node) {
		gotoNode(node, false);
	}
	public void gotoNode(WebTreeNode node, boolean force) {
		if (force)
			tree.getSelectionModel().clearSelection();
		tree.getSelectionModel().setSelectionPath(new TreePath(node.getPath()));
	}
	public void gotoLink(DRKLink link) {
		gotoLink = link;
		gotoNode(link.getParentNode(), true);
	}
	

	/**
	 * This method initializes mainmenu	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
	private JMenuBar getMainmenu() {
		if (mainmenu == null) {
			mainmenu = new JMenuBar();
			mainmenu.add(getFile_menu());
			mainmenu.add(getTree_menu());
			mainmenu.add(getContent_menu());
			mainmenu.add(getGlobal_menu());
		}
		return mainmenu;
	}

	/**
	 * This method initializes file_menu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getFile_menu() {
		if (file_menu == null) {
			file_menu = new JMenu();
			file_menu.setText("Site");
			file_menu.add(getFopen_mi());
			file_menu.add(getFstop_mi());
			file_menu.add(getRecrawl_mi());
			file_menu.add(getRecheckBroken_mi());
			file_menu.add(getCraw_parameters_mi());
			file_menu.add(new JSeparator());
			file_menu.add(getMntmLoad());
			file_menu.add(getMntmSave());
			file_menu.add(new JSeparator());
			file_menu.add(getSitesummary_mi());
			file_menu.add(getSeoreview_mi());
		}
		return file_menu;
	}
	
	class StartCrawler extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public StartCrawler() {
			// super("Start crawling new site",  new ImageIcon(StartCrawler.class.getResource("/images/control_play_new.png")));
			super ("Start crawling new site", new ImageIcon("/images/control_play_new.png"));
			putValue(SHORT_DESCRIPTION, "Start crawling a website");
		}
		
		public void actionPerformed(ActionEvent e) {


			SiteOpenDlg dlg = new SiteOpenDlg(MainWindow.this);
			dlg.showNew();
			
			if (dlg.ok) {
				document = dlg.getDocument();
				document.addProgressListener(MainWindow.this);
				document.addTreeModelListener(MainWindow.this);
				tree.setModel(document);
	
				
				browsing = true;
				link_count = 0;
				start_time = System.currentTimeMillis();
				
				status.setText("Crawling "+ document.getRootNode().getURL().toString() +"...");
				refreshGUI();
				
				worker = new Thread() {
					@Override
					public void run() {
	
						try {
							document.startBrowsing(MainWindow.this, true);
						} catch (CrawlerInterruptedException e) {
							JOptionPane.showMessageDialog(MainWindow.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						}
						
						finishedCrawling();
					}
				};
				
				// Lanzo el proceso...
				synchronized (keepWorkingMutex) {
					keepWorking = true;
				}
				worker.start();
			}
		}
	}
	
	class CrawlParameters extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public CrawlParameters() {
			// super("Site crawling parameters",  new ImageIcon(StartCrawler.class.getResource("/images/parameters.png")));
			super("Site crawling parameters",  new ImageIcon("/images/parameters.png"));
			
			putValue(SHORT_DESCRIPTION, "Modify crawling parameters for this site");
		}
		
		public void actionPerformed(ActionEvent e) {

			// Creamos el diálogo y le pasamos una action anónima
			SiteOpenDlg dlg = new SiteOpenDlg(MainWindow.this);
			dlg.showModify(document);
		}
	}

	class StopCrawler extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public StopCrawler() {
			// super("Stop crawling",  new ImageIcon(StartCrawler.class.getResource("/images/control_stop.png")));
			super ("Stop crawling", new ImageIcon("/images/control_stop.png"));
			putValue(SHORT_DESCRIPTION, "Stop crawler");
		}
		
		public void actionPerformed(ActionEvent e) {
	
			stopCrawler.setEnabled(false);
			status.setText("Stopping crawler...");

			synchronized (keepWorkingMutex) {
				keepWorking = false;
				document.interrupt();
			}
		}
	}
	
	class RecheckBrokenLinks extends AbstractAction {

		private static final long serialVersionUID = 1L;
		
		public RecheckBrokenLinks() {
			// super("Recheck broken links",  new ImageIcon(StartCrawler.class.getResource("/images/control_repeat.png")));
			super ("Recheck broken links", new ImageIcon("/images/control_repeat.png"));
			putValue(SHORT_DESCRIPTION, "Start crawling broken links");
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			worker = new Thread() {
				@Override
				public void run() {

					try {
						document.startBrowsing(MainWindow.this, false);
					} catch (CrawlerInterruptedException e) {
						JOptionPane.showMessageDialog(MainWindow.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					finishedCrawling();
				}
			};
			
			// Lanzo el proceso...
			worker.start();
		}
	}
	
	class Recrawl extends AbstractAction {

		private static final long serialVersionUID = 1L;
		
		public Recrawl() {
			// super("Recrawl site",  new ImageIcon(StartCrawler.class.getResource("/images/control_play.png")));
			super ("Recrawl site", new ImageIcon("/images/control_play.png"));
			putValue(SHORT_DESCRIPTION, "Start crawling broken links");
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			if (JOptionPane.showConfirmDialog(MainWindow.this, "This action is going to discard current tree. Are you sure?", "Discarding data", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
				
				// Elimino datos
				document.removeAllData();
				link_count = 0;
				start_time = System.currentTimeMillis();
				refreshGUI();
				
				worker = new Thread() {
					@Override
					public void run() {
	
						try {
							document.startBrowsing(MainWindow.this, true);
						} catch (CrawlerInterruptedException e) {
							JOptionPane.showMessageDialog(MainWindow.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
						
						finishedCrawling();
					}
				};
				
				// Lanzo el proceso...
				worker.start();
			}
		}
	}
	
	class CSearch extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public CSearch() {
			putValue(NAME, "Search within content...");
		}
		
		public void actionPerformed(ActionEvent e) {
	
			searchDlg = new ContentSearchDlg(MainWindow.this);
			searchDlg.setVisible(true);
			
			if (searchDlg.action != ContentSearchDlg.CANCEL) {
				foundItems.removeAll();
				status.setText("Searching...");
				enableAllActions(false);

				worker = new Thread() {
					@Override
					public void run() {

						foundNodes = document.search(searchDlg.getText(), searchDlg.regex, searchDlg.case_insensitive);
						
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								foundItemsModel.dataChanged();
								status.setText("");
								enableAllActions(true);
								refreshGUI();
							}							
						});
					}
				};
				
				// Lanzo el proceso...
				worker.start();
			}
		}
	}
	class CClrSearch extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public CClrSearch() {
			putValue(NAME, "Clear search result");
		}
		
		public void actionPerformed(ActionEvent e) {
			foundNodes = null;
			foundItemsModel.dataChanged();
			foundItems.setVisibleRowCount(0);
		}
	}
	class AboutAction extends AbstractAction {
		
		private static final long serialVersionUID = 1L;

		public AboutAction() {
			putValue(NAME, "About...");
			putValue(SHORT_DESCRIPTION, "About MagicWebCrawler");
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			new AboutDlg(MainWindow.this);
		}

	}
	class HelpAction extends AbstractAction {
		
		private static final long serialVersionUID = 1L;

		public HelpAction() {
			putValue(NAME, "Help...");
			putValue(SHORT_DESCRIPTION, "Open on-line help in your default browser.");
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			DRKTools.showInBrowser("http://www.drk.com.ar/spider/help/", MainWindow.this);
		}

	}
	class FeedbackAction extends AbstractAction {
		
		private static final long serialVersionUID = 1L;

		public FeedbackAction() {
			putValue(NAME, "Feedback...");
			putValue(SHORT_DESCRIPTION, "Send feedback, features request, or just say hello!");
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			DRKTools.showInBrowser("http://www.drk.com.ar/comment.php?referer=DRKSpiderJava", MainWindow.this);
		}

	}
	class SettingsAction extends AbstractAction {
		
		private static final long serialVersionUID = 1L;

		public SettingsAction() {
			putValue(NAME, "Settings...");
			putValue(SHORT_DESCRIPTION, "Configure DRKSpiderJava options.");
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			new SettingsDlg(MainWindow.this);
		}

	}

	class SiteSummary extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public SiteSummary() {
			//super("Site summary",  new ImageIcon(StartCrawler.class.getResource("/images/table.png")));
			super("Site summary",  new ImageIcon("/images/table.png"));
			putValue(SHORT_DESCRIPTION, "Display site summary");
		}
		
		public void actionPerformed(ActionEvent e) {

			new SummaryDlg(MainWindow.this, document);
		}
	}

	class FoundListModel extends DefaultListModel<NodeTextLocation> {
		
		private static final long serialVersionUID = 1L;

		public int getSize() {
			if (foundNodes != null) {
				return foundNodes.size();
			}
			return 0;
		}
		
		public NodeTextLocation getElementAt(int index) {
			if (foundNodes != null) {
				return foundNodes.get(index);
			}
			return null;
		}

		public void dataChanged() {
			foundItems.setVisibleRowCount((getSize()<MAX_RESULT_ITEMS)?getSize():MAX_RESULT_ITEMS);
			fireContentsChanged(this, 0, getSize()-1);
		}
	};
	
	/**
	 * This method initializes fopen_mi	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getFopen_mi() {
		if (fopen_mi == null) {
			fopen_mi = new JMenuItem();
			fopen_mi.setAction(startCrawler);
		}
		return fopen_mi;
	}

	/**
	 * This method initializes status_panel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getStatus_panel() {
		if (status_panel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints.gridy = 1;
			status = new JLabel();
			status.setText(MainWindow.APP_NAME_AND_WEBSITE);
			status_panel = new JPanel();
			GridBagLayout gbl_status_panel = new GridBagLayout();
			gbl_status_panel.rowWeights = new double[]{1.0, 0.0};
			gbl_status_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
			status_panel.setLayout(gbl_status_panel);
			status_panel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.LOWERED));
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.gridwidth = 8;
			gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
			gbc_scrollPane.gridx = 0;
			gbc_scrollPane.gridy = 0;
			status_panel.add(getScrollPane(), gbc_scrollPane);
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
			status_panel.add(getProgress(), gridBagConstraints1);
			GridBagConstraints gbc_separator_1 = new GridBagConstraints();
			gbc_separator_1.insets = new Insets(0, 0, 0, 5);
			gbc_separator_1.gridx = 2;
			gbc_separator_1.gridy = 1;
			status_panel.add(getSeparator_1(), gbc_separator_1);
			GridBagConstraints gbc_total_links = new GridBagConstraints();
			gbc_total_links.insets = new Insets(0, 0, 0, 5);
			gbc_total_links.gridx = 3;
			gbc_total_links.gridy = 1;
			status_panel.add(getTotal_links(), gbc_total_links);
			GridBagConstraints gbc_separator = new GridBagConstraints();
			gbc_separator.insets = new Insets(0, 0, 0, 5);
			gbc_separator.gridx = 4;
			gbc_separator.gridy = 1;
			status_panel.add(getSeparator(), gbc_separator);
			GridBagConstraints gbc_speed = new GridBagConstraints();
			gbc_speed.insets = new Insets(0, 0, 0, 5);
			gbc_speed.gridx = 5;
			gbc_speed.gridy = 1;
			status_panel.add(getSpeed(), gbc_speed);
			GridBagConstraints gbc_separator_2 = new GridBagConstraints();
			gbc_separator_2.insets = new Insets(0, 0, 0, 5);
			gbc_separator_2.gridx = 6;
			gbc_separator_2.gridy = 1;
			status_panel.add(getSeparator_2(), gbc_separator_2);
			status_panel.add(status, gridBagConstraints);
			GridBagConstraints gbc_overallProgress = new GridBagConstraints();
			gbc_overallProgress.fill = GridBagConstraints.BOTH;
			gbc_overallProgress.insets = new Insets(5, 5, 5, 5);
			gbc_overallProgress.gridx = 7;
			gbc_overallProgress.gridy = 1;
			status_panel.add(getOverallProgress(), gbc_overallProgress);
		}
		return status_panel;
	}

	/**
	 * This method initializes progress	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getProgress() {
		if (progress == null) {
			progress = new JProgressBar();
			progress.setMinimumSize(new Dimension(PROGRESS_WIDTH, progress.getSize().height));
		}
		return progress;
	}

	@Override
	public void setProgress(final float percent, final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progress.setIndeterminate(false);
				progress.setValue((int)(percent * 100));
				status.setText(text);
				progress.updateUI();
			}
		});
	}


	@Override
	public void setOverallProgress(final float percent) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				overallProgress.setIndeterminate(false);
				overallProgress.setValue((int)(percent * 100));
				overallProgress.updateUI();
			}
		});
	}
	
	@Override
	public void setUnknownProgress(final boolean unknown, final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				status.setText(text);
				progress.setIndeterminate(unknown);
			}
		});
	}
	
	public boolean keepWorking() {
		synchronized (keepWorkingMutex) {
			return keepWorking;
		}
	}

	@Override
	public void addLinkCount(final int count) {
		synchronized (link_count) {
			link_count += count;
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				long elapsed_time = System.currentTimeMillis() - start_time;
				crawl_speed  = (double)link_count / elapsed_time * 1000; 
				refreshGUI();
			}
		});
	}
	

	/**
	 * This method initializes fstop_mi	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getFstop_mi() {
		if (fstop_mi == null) {
			fstop_mi = new JMenuItem();
			fstop_mi.setAction(stopCrawler);
		}
		return fstop_mi;
	}

	/**
	 * This method initializes rpanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getRpanel() {
		if (rpanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.weighty = 1.0;
			gridBagConstraints3.insets = new java.awt.Insets(3,3,3,3);
			gridBagConstraints3.gridy = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.gridy = 0;
			rpanel = new JPanel();
			rpanel.setLayout(new GridBagLayout());
			rpanel.add(getToppanel(), gridBagConstraints2);
			rpanel.add(getInfopanel(), gridBagConstraints3);
		}
		return rpanel;
	}

	/**
	 * This method initializes toppanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getToppanel() {
		if (toppanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.insets = new Insets(5, 5, 0, 5);
			gridBagConstraints5.gridy = 0;
			lbl_url = new JLabel();
			lbl_url.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lbl_url.setText("URL:");
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints4.weightx = 0.9;
			toppanel = new JPanel();
			GridBagLayout gbl_toppanel = new GridBagLayout();
			toppanel.setLayout(gbl_toppanel);
			toppanel.add(getUrlbox(), gridBagConstraints4);
			toppanel.add(lbl_url, gridBagConstraints5);
			GridBagConstraints gbc_lbl_modified = new GridBagConstraints();
			gbc_lbl_modified.anchor = GridBagConstraints.EAST;
			gbc_lbl_modified.insets = new Insets(5, 5, 0, 5);
			gbc_lbl_modified.gridx = 2;
			gbc_lbl_modified.gridy = 0;
			toppanel.add(getLbl_modified(), gbc_lbl_modified);
			GridBagConstraints gbc_last_modified = new GridBagConstraints();
			gbc_last_modified.weightx = 0.1;
			gbc_last_modified.insets = new Insets(5, 5, 5, 5);
			gbc_last_modified.fill = GridBagConstraints.HORIZONTAL;
			gbc_last_modified.gridx = 3;
			gbc_last_modified.gridy = 0;
			toppanel.add(getLast_modified(), gbc_last_modified);
		}
		return toppanel;
	}

	/**
	 * This method initializes infopanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JScrollPane getInfopanel() {
		if (infopanel == null) {
			infopanel = new JScrollPane();
			infopanel.setLayout(new ScrollPaneLayout());
			infopanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.LOWERED));
			infopanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			infopanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return infopanel;
	}

	/**
	 * This method initializes urlbox	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getUrlbox() {
		if (urlbox == null) {
			urlbox = new JTextField();
			urlbox.setFont(new Font("Tahoma", Font.BOLD, 12));
			urlbox.setEditable(false);
		}
		return urlbox;
	}

	/**
	 * This method initializes tree_scroll	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getTree_scroll() {
		if (tree_scroll == null) {
			tree_scroll = new JScrollPane(getTree());
			tree_scroll.setPreferredSize(new java.awt.Dimension(150,300));
			tree_scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			tree_scroll.setViewportView(getTree());
		}
		return tree_scroll;
	}

	private JMenu getContent_menu() {
		if (content_menu == null) {
			content_menu = new JMenu("Content");
			content_menu.add(getSearch_mi());
			content_menu.add(getClr_search_mi());
		}
		return content_menu;
	}
	private JMenuItem getSearch_mi() {
		if (search_mi == null) {
			search_mi = new JMenuItem("Search within content...");
			search_mi.setAction(act_csearch);
		}
		return search_mi;
	}
	private JList<NodeTextLocation> getFoundItems() {
		if (foundItems == null) {
			foundItems = new JList<NodeTextLocation>();
			foundItems.setVisibleRowCount(0);
			foundItems.setModel(foundItemsModel);
			foundItems.addListSelectionListener(this);
		}
		return foundItems;
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getFoundItems());
		}
		return scrollPane;
	}
	private JMenuItem getClr_search_mi() {
		if (clr_search_mi == null) {
			clr_search_mi = new JMenuItem("Clear search result");
			clr_search_mi.setAction(act_cclrsearch);
		}
		return clr_search_mi;
	}
	private JMenu getTree_menu() {
		if (tree_menu == null) {
			tree_menu = new JMenu("Tree node");
			tree_menu.add(getSearchLinks_mi());
			tree_menu.add(getMntmErrorLinks());
			tree_menu.add(getMntmDeepErrorNodes());
			tree_menu.add(getSourcelinks_mi());
			tree_menu.add(getNodeexport_mi());
			tree_menu.addSeparator();
			tree_menu.add(getInBrowser_mi());
			tree_menu.add(getGoogleLinks_mi());
			tree_menu.add(getSearchSiteInGoogle_mi());
			tree_menu.add(getSeoReview_mi());
		}
		return tree_menu;
	}
	private JMenuItem getMntmDeepErrorNodes() {
		if (errornodes_mi == null) {
			errornodes_mi = new JMenuItem("Errors");
			errornodes_mi.setAction(deepErrorsNodes);
		}
		return errornodes_mi;
	}
	private JMenuItem getSearchSiteInGoogle_mi() {
		if (searchSiteInGoogle_mi == null) {
			searchSiteInGoogle_mi = new JMenuItem("Google");
			searchSiteInGoogle_mi.setAction(searchSiteInGoogle);
		}
		return searchSiteInGoogle_mi;
	}
	private JMenuItem getMntmErrorLinks() {
		if (errorlinks_mi == null) {
			errorlinks_mi = new JMenuItem("Errors");
			errorlinks_mi.setAction(errorsDialog);
		}
		return errorlinks_mi;
	}
	private JMenuItem getNodeexport_mi() {
		if (nodeexport_mi == null) {
			nodeexport_mi = new JMenuItem("Export");
			nodeexport_mi.setAction(exportDialog);
		}
		return nodeexport_mi;
	}
	private JMenuItem getSearchLinks_mi() {
		if (searchLinks_mi == null) {
			searchLinks_mi = new JMenuItem("Search links...");
			searchLinks_mi.setAction(searchLinksTo);
		}
		return searchLinks_mi;
	}

	
	// Clipboard owner
	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		// Nada a menos que hay que liberar recursos
	}

	// Eventos de selección de foundItems
	@Override
	public void valueChanged(ListSelectionEvent e) {
		gotoContent = foundItems.getSelectedValue();
		if (gotoContent != null)
			gotoNode(gotoContent.node, true);
	}
	private JMenu getGlobal_menu() {
		if (global_menu == null) {
			global_menu = new JMenu("MagicWebCrawler");
			global_menu.add(getAbout_mi());
			global_menu.add(getHelp_mi());
			global_menu.add(getFeedback_mi());
			global_menu.add(getSettings_mi());
		}
		return global_menu;
	}
	private JMenuItem getAbout_mi() {
		if (about_mi == null) {
			about_mi = new JMenuItem("About...");
			about_mi.setAction(new AboutAction());
		}
		return about_mi;
	}

	public static String getUserAgent() {
		return DRKTools.prefs.get("user_agent", MainWindow.USER_AGENT);
	}
	private JMenuItem getSettings_mi() {
		if (settings_mi == null) {
			settings_mi = new JMenuItem("Settings...");
			settings_mi.setAction(settingsAction);
		}
		return settings_mi;
	}
	private JMenuItem getSeoreview_mi() {
		if (seoreview_mi == null) {
			seoreview_mi = new JMenuItem("Seo review");
			seoreview_mi.setAction(siteWideSeoReview);
		}
		return seoreview_mi;
	}
	private JMenuItem getInBrowser_mi() {
		if (inBrowser_mi == null) {
			inBrowser_mi = new JMenuItem("Open in browser...");
			inBrowser_mi.setAction(showNodeInBrowser);
		}
		return inBrowser_mi;
	}
	private JMenuItem getGoogleLinks_mi() {
		if (googleLinks_mi == null) {
			googleLinks_mi = new JMenuItem("Search Google links...");
			googleLinks_mi.setAction(searchNodeGoogleLinks);
		}
		return googleLinks_mi;
	}

	private JMenuItem getMntmSave() {
		if (mntmSave == null) {
			mntmSave = new JMenuItem("Save...");
			mntmSave.setAction(treeSave);
		}
		return mntmSave;
	}
	private JMenuItem getMntmLoad() {
		if (mntmLoad == null) {
			mntmLoad = new JMenuItem("Load...");
			mntmLoad.setAction(treeLoad);
		}
		return mntmLoad;
	}

	
	// TreeModelListener
	@Override
	public void treeNodesChanged(final TreeModelEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				tree.updateUI();
				if (document.getRoot() == e.getTreePath().getLastPathComponent()) {
					tree.expandPath(new TreePath(document.getRoot()));
					tree.setSelectionPath(new TreePath(document.getRoot()));
				}
			}
		});
	}

	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				tree.updateUI();
			}
		});
	}

	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
	}

	@Override
	public void treeStructureChanged(TreeModelEvent e) {
	}
	private JMenuItem getRecheckBroken_mi() {
		if (recheckBroken_mi == null) {
			recheckBroken_mi = new JMenuItem("Recheck broken links");
			recheckBroken_mi.setAction(recheckBrokenLinks);
		}
		return recheckBroken_mi;
	}
	private JMenuItem getRecrawl_mi() {
		if (recrawl_mi == null) {
			recrawl_mi = new JMenuItem("Recrawl site");
			recrawl_mi.setAction(recrawl);
		}
		return recrawl_mi;
	}
	private JMenuItem getFeedback_mi() {
		if (feedback_mi == null) {
			feedback_mi = new JMenuItem("Feedback...");
			feedback_mi.setAction(new FeedbackAction());
		}
		return feedback_mi;
	}
	private JLabel getLbl_modified() {
		if (lbl_modified == null) {
			lbl_modified = new JLabel("Last modified:");
		}
		return lbl_modified;
	}
	private JTextField getLast_modified() {
		if (last_modified == null) {
			last_modified = new JTextField();
			last_modified.setEditable(false);
			last_modified.setColumns(6);
		}
		return last_modified;
	}
	private JLabel getTotal_links() {
		if (total_links == null) {
			total_links = new JLabel("");
		}
		return total_links;
	}
	private JLabel getSpeed() {
		if (speed == null) {
			speed = new JLabel("");
		}
		return speed;
	}
	private JSeparator getSeparator() {
		if (separator == null) {
			separator = new JSeparator();
			separator.setOrientation(SwingConstants.VERTICAL);
		}
		return separator;
	}
	private JSeparator getSeparator_1() {
		if (separator_1 == null) {
			separator_1 = new JSeparator();
			separator_1.setOrientation(SwingConstants.VERTICAL);
		}
		return separator_1;
	}
	private JSeparator getSeparator_2() {
		if (separator_2 == null) {
			separator_2 = new JSeparator();
			separator_2.setOrientation(SwingConstants.VERTICAL);
		}
		return separator_2;
	}
	private JProgressBar getOverallProgress() {
		if (overallProgress == null) {
			overallProgress = new JProgressBar();
			overallProgress.setMinimumSize(new Dimension(PROGRESS_WIDTH, overallProgress.getSize().height));
		}
		return overallProgress;
	}
	private JMenuItem getCraw_parameters_mi() {
		if (craw_parameters_mi == null) {
			craw_parameters_mi = new JMenuItem("Site crawling parameters");
			craw_parameters_mi.setAction(crawlParameters);
		}
		return craw_parameters_mi;
	}
	private JMenuItem getSitesummary_mi() {
		if (sitesummary_mi == null) {
			sitesummary_mi = new JMenuItem("Site summary");
			sitesummary_mi.setAction(siteSummary);
		}
		return sitesummary_mi;
	}

	/**
	 * Llamadao cuando termina el crawler. Estabiliza la GUI
	 * y muestra el summay
	 */
	private void finishedCrawling() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				browsing = false;
				overallProgress.setValue(0);
				overallProgress.setIndeterminate(false);
				progress.setValue(0);
				progress.setIndeterminate(false);
				status.setText("Finished crawling "+ document.getRootNode().getURL().toString() +".");
				tree.expandPath(new TreePath(document.getRoot()));
				tree.setSelectionPath(new TreePath(document.getRoot()));
				refreshGUI();
				new SummaryDlg(MainWindow.this, document);
			}							
		});
	}
	private JMenuItem getSeoReview_mi() {
		if (seoReview_mi == null) {
			seoReview_mi = new JMenuItem("SEO review");
			seoReview_mi.setAction(actionSEO);
		}
		return seoReview_mi;
	}
	private JMenuItem getHelp_mi() {
		if (help_mi == null) {
			help_mi = new JMenuItem("Help...");
			help_mi.setAction(new HelpAction());
		}
		return help_mi;
	}
	private JMenuItem getSourcelinks_mi() {
		if (sourcelinks_mi == null) {
			sourcelinks_mi = new JMenuItem("Show source links...");
			sourcelinks_mi.setAction(sourceLinks);
		}
		return sourcelinks_mi;
	}
}
