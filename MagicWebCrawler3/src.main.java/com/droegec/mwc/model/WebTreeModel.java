package com.droegec.mwc.model;

import java.io.IOException;
import java.io.Serializable;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;

import com.droegec.mwc.exceptions.CrawlerInterruptedException;
import com.droegec.mwc.exceptions.RobotsNotFoundException;
import org.apache.commons.lang3.StringUtils;

import com.droegec.mwc.DRKTools;
import com.droegec.mwc.ProgressListener;
import com.droegec.mwc.WorkManager;

import java.io.File;
import java.io.FileWriter;

/**
 * Modelo de árbol para el contenido de un sitio web. Implementa la
 * funcionalidad de obtención de la información.
 *
 * @author _Leo_
 */
public class WebTreeModel extends DefaultTreeModel implements WebTreeNodeListener, Serializable
{

    private static final long serialVersionUID = 7543636621632764210L;

    private transient Set<ProgressListener> listeners = new HashSet<ProgressListener>();
    private transient ExecutorService pool = null;
    private transient ConcurrentLinkedQueue<Future<?>> futures = null;
    private transient CookieManager cookieManager = new CookieManager();
    private Set<String> excludeURLs = new HashSet<String>();
    private Map<URL, WebTreeNode> URLs = new ConcurrentHashMap<URL, WebTreeNode>();
    private int maxDepth = 0;
    private boolean contentInMemory = false;
    private boolean obeyRobots = true;
    private String username = "";
    private char[] password;
    private boolean formTraversing = false;
    private boolean isParsed = false;
    private boolean isInterrupted = false;
    private Robots robots = null;
    private long totalParseTime = 0;

    // new CHD Begin 14.01.2019
    private String searchParameter = null;
    private String outputDirectory = null;

    private boolean followLinkHTML;
    private boolean followLinkIMG;
    private boolean saveResource;
    private boolean saveIMG;
    private boolean extractEMailAddress;
    private boolean filterLinkIMG;
    private boolean filterLinkScript;
    
    public  Set<String> emails = new HashSet<String>();

    public WebTreeModel(WebTreeNode root)
    {
        super(root);
    }

    /**
     * Comienza a navegar el sitio desde la raíz.
     *
     * @throws CrawlerInterruptedException
     */
    public void startBrowsing(WorkManager manager, boolean full) throws CrawlerInterruptedException
    {
        emails.clear();
        if (full)
        {
            URLs.clear();
        } else
        {
            // Elimino los nodos con error y los saco del mapa
            for (WebTreeNode node : getRootNode().purgeErrorNodes())
            {
                URLs.remove(node.getURL());
            }
        }

        setAuthenticationHandler();
        cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        // Obtengo la cantidad de threads y limito entre 1 y 15
        int tc = DRKTools.prefs.getInt("thread_count", 2);
        tc = (tc < 1 || tc > 15) ? 2 : tc;
        // new CHD 12.01.2019
        // tc = 1;

        // Construyo la bolsa de threads
        BlockingQueue<Runnable> linkedBlockingDeque = new LinkedBlockingDeque<Runnable>(tc);
        pool = new ThreadPoolExecutor(tc, tc, 30, TimeUnit.SECONDS, linkedBlockingDeque, new ThreadPoolExecutor.CallerRunsPolicy());
        futures = new ConcurrentLinkedQueue<Future<?>>();

        setUnknowProgress(true);
        isInterrupted = false;

        long start = System.currentTimeMillis();

        robots = null;
        if (obeyRobots)
        {
            try
            {
                robots = new Robots(getRootNode().getURL());
            } catch (RobotsNotFoundException e)
            {
                // Si no hay archivo seguimos adelante
            } catch (IOException e)
            {
                throw new CrawlerInterruptedException("Problems with robots.txt (" + e.toString() + "). Crawling aborted...", e);
            }
        }

        getRootNode().browse(1, URLs, manager, this, full);

        try
        {
            if (!isInterrupted)
            {
                for (Future<?> future : futures)
                {
                    if (future != null)
                    {
                        future.get();
                    }
                }
            }
            pool.shutdown();
            while (!pool.awaitTermination(1, TimeUnit.SECONDS));
        } catch (InterruptedException e)
        {
            // throw new CrawlerInterruptedException("Abnormal interruption. Crawling aborted...", e);
            // tue nichts
        } catch (ExecutionException e)
        {
            e.printStackTrace();// throw new CrawlerInterruptedException("Abnormal interruption. Crawling aborted...", e);
        }

        // Precalculamos
        getRootNode().countLinksDeep();

        totalParseTime = System.currentTimeMillis() - start;

        // Retiro el cookie manager propio de este docuemnto
        CookieHandler.setDefault(new CookieManager());

        setUnknowProgress(false);
        isParsed = true;
        pool = null;
        futures = null;
        saveEMailAddressToFile ();
    }

    public void setAuthenticationHandler()
    {
        Authenticator.setDefault(new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                if (password != null && getRequestingURL().getHost().equals(getRootNode().getURL().getHost()))
                {
                    return new PasswordAuthentication(username, password);
                } else
                {
                    return super.getPasswordAuthentication();
                }
            }
        });
    }

    /**
     * Add task to execution queue
     *
     * @param runnable
     */
    public void addTask(Runnable runnable)
    {
        futures.add(pool.submit(runnable));
    }

    public void interrupt()
    {
        if (!isInterrupted)
        {
            isInterrupted = true;
            try
            {
                pool.shutdown();
                pool.awaitTermination(500, TimeUnit.MILLISECONDS);
                pool.shutdownNow();
                pool.awaitTermination(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e)
            {
            }
        }
    }

    public void setMaxDepth(int maxDepth)
    {
        this.maxDepth = maxDepth;
    }

    public void setRoot(WebTreeNode root)
    {
        root.addWebTreeNodeListener(this);
        super.setRoot(root);
    }

    public boolean isParsed()
    {
        return isParsed;
    }

    public boolean isInterrupted()
    {
        return isInterrupted;
    }

    /**
     * @return the maximum depth for this model
     */
    public int getMaxDepth()
    {
        return maxDepth;
    }

    public int getTotalNodeCount()
    {
        return getRootNode().getTotalChildCount();
    }

    public int getInternalLinkCount()
    {
        return getRootNode().getInternalLinkCountDeep();
    }

    public int getExternalLinkCount()
    {
        return getRootNode().getExternalLinkCountDeep();
    }

    public int getNofollowLinkCount()
    {
        return getRootNode().getNofollowLinkCountDeep();
    }

    public int getErrorLinkCount()
    {
        return getRootNode().getErrorLinkCountDeep();
    }

    public Robots getRobots()
    {
        return robots;
    }

    public CookieStore getCookieStore()
    {
        return cookieManager.getCookieStore();
    }

    /**
     * Busca en el sitio desde la raíz.
     *
     * @param regex
     */
    public List<NodeTextLocation> search(String text, boolean regex, boolean case_insensitive)
    {

        final List<NodeTextLocation> nodes = new LinkedList<NodeTextLocation>();

        setUnknowProgress(true);

        getRootNode().search(text, regex, case_insensitive, nodes);

        setUnknowProgress(false);

        return nodes;

    }

    public WebTreeNode getRootNode()
    {
        return (WebTreeNode) root;
    }

    public void nodeChanged(final WebTreeNode node)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                WebTreeNode parent = (WebTreeNode) node.getParent();
                if (parent != null)
                {
                    fireTreeNodesChanged(node, parent.getPath(), new int[]
                    {
                        parent.getIndex(node)
                    }, new Object[]
                    {
                        node
                    });
                }
            }
        });
    }

    @Override
    public void nodeAdded(final WebTreeNode source, final Object[] path,
            final int[] childIndices,
            final WebTreeNode[] children)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                fireTreeNodesInserted(source, path, childIndices, children);
            }
        });
    }

    public void addProgressListener(ProgressListener listener)
    {
        listeners.add(listener);
    }

    public void removeProgressListener(ProgressListener listener)
    {
        listeners.remove(listener);
    }

    protected void setUnknowProgress(boolean on)
    {
        for (ProgressListener listener : listeners)
        {
            listener.setUnknownProgress(on, "");
        }
    }

    public boolean isContentInMemory()
    {
        return contentInMemory;
    }

    public void setContentInMemory(boolean contentInMemory)
    {
        this.contentInMemory = contentInMemory;
    }

    public void setObeyRobots(boolean obeyRobots)
    {
        this.obeyRobots = obeyRobots;
    }

    public SiteSEOReview getSEOResult()
    {
        return getRootNode().getDeepSEOResult();
    }

    // Serialize
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        // Recover transient properties
        listeners = new HashSet<ProgressListener>();
        cookieManager = new CookieManager();
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
        // Remove default model listeners for serialization
        out.defaultWriteObject();
    }

    public TreeModelListener[] prepareForSerialization()
    {
        TreeModelListener[] all = getTreeModelListeners();
        for (TreeModelListener treeModelListener : all)
        {
            removeTreeModelListener(treeModelListener);
        }
        return all;
    }

    public void recoverAfterSerialization(TreeModelListener[] all)
    {
        for (TreeModelListener treeModelListener : all)
        {
            addTreeModelListener(treeModelListener);
        }
    }

    public void setParsed(boolean b)
    {
        this.isParsed = b;
    }

    public void removeAllData()
    {
        root = new WebTreeNode(getRootNode().getURL());
        nodeChanged(root);
    }

    /**
     * List of all (unique) links found in this document
     *
     * @return
     */
    public List<DRKLink> getAllLinks()
    {
        return getRootNode().getLinksDeep();
    }

    public String getExcludeURLs()
    {
        return StringUtils.join(excludeURLs, "\n");
    }

    /**
     * Reeplaces all exclude URLs with those in text string
     *
     * @param text exclude regular expressions, one per line
     */
    public void setExcludeURLs(String text)
    {
        String[] urls = text.split("\n");
        excludeURLs = new HashSet<String>();
        for (String url : urls)
        {
            excludeURLs.add(url);
        }
        excludeURLs = Collections.unmodifiableSet(excludeURLs);
    }

    /**
     * Validates access (URL against user excludes). It's thread safe because
     * the hash is immutable
     *
     * @param url
     * @return false if this URL is excluded
     */
    public boolean canAccess(URL url)
    {
        for (String ex : excludeURLs)
        {
            if (url.toString().matches(ex))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Total parse time in milliseconds
     *
     * @return
     */
    public long getTotalParseTime()
    {
        return totalParseTime;
    }

    public void setUsernameAndPassword(String username, char[] password)
    {
        this.username = username;
        this.password = password;
    }

    public String getUsername()
    {
        return username;
    }

    public boolean isFormTraversing()
    {
        return formTraversing;
    }

    public void setFormTraversing(boolean formTraversing)
    {
        this.formTraversing = formTraversing;
    }

    public String getSearchParameter()
    {
        return searchParameter;
    }

    public void setSearchParameter(String searchParameter)
    {
        this.searchParameter = searchParameter;
    }

    public String getOutputDirectory()
    {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory)
    {
        this.outputDirectory = outputDirectory;
    }

    public boolean isFollowLinkHTML()
    {
        return followLinkHTML;
    }

    public void setFollowLinkHTML(boolean followLinkHTML)
    {
        this.followLinkHTML = followLinkHTML;
    }

    public boolean isFollowLinkIMG()
    {
        return followLinkIMG;
    }

    public void setFollowLinkIMG(boolean followLinkIMG)
    {
        this.followLinkIMG = followLinkIMG;
    }

    public boolean isSaveResource()
    {
        return saveResource;
    }

    public void setSaveResource(boolean saveResource)
    {
        this.saveResource = saveResource;
    }

    public boolean isSaveIMG()
    {
        return saveIMG;
    }

    
    public void setSaveIMG(boolean saveIMG)
    {
        this.saveIMG = saveIMG;
    }

    public boolean isExtractEMailAddress() 
    {
        return extractEMailAddress;
    }

    public void setExtractEMailAddress(boolean eMailAddress ) 
    {
        extractEMailAddress = eMailAddress ;
    }
    public Set<String> getEMailAddress() 
    {
        return emails;
    
    }
    
    public boolean isFilterIMGLink() 
    {
        return filterLinkIMG;
    }

    public void setFilterIMGLink(boolean FilterIMGLink ) 
    {
        filterLinkIMG = FilterIMGLink ;
    }
    
    public boolean isFilterScriptLink() 
    {
        return filterLinkScript;
    }

    public void setFilterScriptLink(boolean FilterScriptLink ) 
    {
        filterLinkScript = FilterScriptLink ;
    }
    
    
    
    
    
    
    
    
    
    private void saveEMailAddressToFile () {
    
       if (!extractEMailAddress) return;
        FileWriter writer = null;
        try
        {
            File dir = new File(outputDirectory);
            boolean IsCrated = dir.mkdirs();        
            writer = new FileWriter(outputDirectory + "eMailAdress.txt");
            for (String elem : emails) {
                // System.out.println (elem);
                writer.write (elem + "\n");
            }
            if (writer != null) writer.close();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
            
}
