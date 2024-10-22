package com.droegec.mwc.processor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.droegec.mwc.DRKTools;
import com.droegec.mwc.ProgressListener;
import com.droegec.mwc.model.DRKLink;
import com.droegec.mwc.model.MetaTag;
import com.droegec.mwc.model.WebTreeModel;
import com.droegec.mwc.model.WebTreeNode;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLHandshakeException;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class HTMLParser extends Parser
{

    private static final long serialVersionUID = 2602183879460669165L;
    //String searchParameter = "Magazin";
    // String searchParameter = "^.*?(Magazin|magazin|Abo|abo|Mediadaten|Mediaportal).*$";
    private String searchParameter = "";
    private String outputDirectory = "D:/output/";
    private boolean followLinkHTML = false;
    private boolean followLinkIMG = true;
    private boolean saveResource = true;
    private boolean saveIMG = true;
    private boolean extractEMailAddress = true;

    private boolean insertIMGintoTree = true;
    private boolean insertScriptintoTree = false;

    public HTMLParser(WebTreeNode nodo, WebTreeModel model)
    {
        super(nodo, model);

        searchParameter = model.getSearchParameter();
        outputDirectory = model.getOutputDirectory();
        followLinkHTML = model.isFollowLinkHTML();
        followLinkIMG = model.isFollowLinkIMG();
        saveResource = model.isSaveResource();
        saveIMG = model.isSaveIMG();
        extractEMailAddress = model.isExtractEMailAddress();

        insertIMGintoTree = model.isFilterIMGLink();
        insertScriptintoTree = model.isFilterScriptLink();

    }

    /**
     * Returns text only from HTML string
     *
     * @param html string with HTML content
     * @return text only
     */
    static public String getTextFromHTML(String html)
    {
        // return html.replaceAll("<script[^>]*>[^<]*</script>", "").replaceAll("<style[^>]*>[^<]*</style>", "").replaceAll("<[^>]+>", "").replaceAll("\\s+", " ");
        return html.replaceAll("<[^>]+>", "").replaceAll("\\s+", " ");
    }

    /**
     * Inicia el procesamiento de la URL. El parser obtiene los links de la URL
     * con el que fue instanciado.
     */
    public void parse()
    {

        // Busco links
        findURLs();

    }

    /* (non-Javadoc)
	 * @see ar.com.drk.drkspiderjava.processor.Parser#findURLs()
     */
    @Override
    protected void findURLs()
    {

        String html = nodeContent(null);

        // Anchor
        Matcher regex = Pattern.compile("<a[\\s]+[^>]*>(.*?)(?=</a>)</a>", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE).matcher(html);

        while (regex.find())
        {
            String anchor = regex.group(1);
            String link = null;
            Map<String, String> attributes = new HashMap<String, String>();
            Matcher attmatcher = Pattern.compile("(\\b\\w+\\b)\\s*=\\s*(?:\"([^\"]*)\"|'([^']*)'|([^\"'<>\\s]+))", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE).matcher(regex.group());

            while (attmatcher.find())
            {
                if ("href".equals(attmatcher.group(1)))
                {
                    link = attmatcher.group(2);
                } else
                {
                    attributes.put(attmatcher.group(1), attmatcher.group(2));
                }
            }

            if (link != null && !link.startsWith("#") && !link.startsWith("mailto:") && !link.startsWith("javascript:"))

            {
                DRKLink newlink;
                try
                {

                    if (link.startsWith("http://") || link.startsWith("https://"))
                    {
                        //String url = nodo.getURL().toString();
                        if (checkUrl(link, searchParameter))
                        {
                            saveURLasHTMLDocument(link);
                            newlink = new DRKLink("a", anchor, new URL(link), attributes, nodo, regex.start(), regex.end());
                            nodo.addLink(newlink);
                        }
                    } else
                    {
                        // String path = (nodo.getURL().getPath().length() > 0)?nodo.getURL().getPath().substring(0, nodo.getURL().getPath().lastIndexOf('/')+1):"/";
                        if (link.startsWith("/"))
                        {
                            link = nodo.getURL().getProtocol() + "://" + nodo.getURL().getHost() + link;
                            if (checkUrl(link, searchParameter))
                            {
                                saveURLasHTMLDocument(link);
                                newlink = new DRKLink("a", anchor, new URL(link), attributes, nodo, regex.start(), regex.end());
                                nodo.addLink(newlink);
                            }
                        }
                    }
                } catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }
            }
        }

        // Images
        regex = Pattern.compile("<img[\\s]+[^>]+[/]?>", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE).matcher(html);

        while (regex.find() && (insertIMGintoTree))
        {
            DRKLink newlink;
            String anchor = "";
            String link = null;
            Map<String, String> attributes = new HashMap<String, String>();
            Matcher attmatcher = Pattern.compile("(\\b\\w+\\b)\\s*=\\s*(?:\"([^\"]*)\"|'([^']*)'|([^\"'<>\\s]+))", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE).matcher(regex.group());

            while (attmatcher.find())
            {
                if ("src".equals(attmatcher.group(1)))
                {
                    link = attmatcher.group(2);
                } else if ("alt".equals(attmatcher.group(1)))
                {
                    anchor = attmatcher.group(2);
                } else
                {
                    attributes.put(attmatcher.group(1), attmatcher.group(2));
                }
            }

            if (link != null)
            {
                try
                {

                    if (link.startsWith("http://") || link.startsWith("https://"))
                    {
                        if (checkUrlImage(link))
                        {
                            if (saveIMG)
                            {
                                getImages(link);
                            }
                            newlink = new DRKLink("img", anchor, new URL(link), attributes, nodo, regex.start(), regex.end());
                            nodo.addLink(newlink);
                        }
                    }
                } catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }
            }
        }

        // Link
        regex = Pattern.compile("<link[\\s]+[^>]+[/]?>", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE).matcher(html);

        while (regex.find())
        {
            DRKLink newlink;
            String anchor = "";
            String link = null;
            Map<String, String> attributes = new HashMap<String, String>();
            Matcher attmatcher = Pattern.compile("(\\b\\w+\\b)\\s*=\\s*(?:\"([^\"]*)\"|'([^']*)'|([^\"'<>\\s]+))", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE).matcher(regex.group());

            while (attmatcher.find())
            {
                if ("href".equals(attmatcher.group(1)))
                {
                    link = attmatcher.group(2);
                } else
                {
                    attributes.put(attmatcher.group(1), attmatcher.group(2));
                }
            }

            if (link != null)
            {
                try
                {

                    if (link.startsWith("http://") || link.startsWith("https://"))
                    {
                        //String url = nodo.getURL().toString();
                        if (checkUrl(link, searchParameter))
                        {
                            saveURLasHTMLDocument(link);
                            newlink = new DRKLink("link", anchor, new URL(link), attributes, nodo, regex.start(), regex.end());
                            nodo.addLink(newlink);
                        }
                    } else // String path = (nodo.getURL().getPath().length() > 0)?nodo.getURL().getPath().substring(0, nodo.getURL().getPath().lastIndexOf('/')+1):"/";
                    if (link.startsWith("/"))
                    {
                        link = nodo.getURL().getProtocol() + "://" + nodo.getURL().getHost() + link;
                        if (checkUrl(link, searchParameter))
                        {
                            saveURLasHTMLDocument(link);
                            newlink = new DRKLink("link", anchor, new URL(link), attributes, nodo, regex.start(), regex.end());
                            nodo.addLink(newlink);
                        }
                    }
                } catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }
            }
        }

        // Script or Style
        regex = Pattern.compile("<(script|style)[\\s]+[^>]+[/]?>", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE).matcher(html);

        while (regex.find() && (insertScriptintoTree))
        {
            DRKLink newlink;
            String anchor = "";
            String link = null;
            Map<String, String> attributes = new HashMap<String, String>();
            Matcher attmatcher = Pattern.compile("(\\b\\w+\\b)\\s*=\\s*(?:\"([^\"]*)\"|'([^']*)'|([^\"'<>\\s]+))", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE).matcher(regex.group());

            while (attmatcher.find() && insertScriptintoTree)
            {
                if ("src".equals(attmatcher.group(1)))
                {
                    link = attmatcher.group(2);
                } else
                {
                    attributes.put(attmatcher.group(1), attmatcher.group(2));
                }
            }

            if (link != null)
            {
                try
                {

                    if (link.startsWith("http://") || link.startsWith("https://"))
                    {
                        //String url = nodo.getURL().toString();
                        if (checkUrl(link, ""))
                        {
                            saveURLAsFile(link);
                            newlink = new DRKLink(regex.group(1), anchor, new URL(link), attributes, nodo, regex.start(), regex.end());
                            nodo.addLink(newlink);
                        }
                    } else
                    {
                        String path = (nodo.getURL().getPath().length() > 0) ? nodo.getURL().getPath().substring(0, nodo.getURL().getPath().lastIndexOf('/') + 1) : "/";
                        if (link.startsWith("/"))
                        {
                            link = nodo.getURL().getProtocol() + "://" + nodo.getURL().getHost() + link;
                            if (checkUrl(link, ""))
                            {
                                saveURLAsFile(link);
                                newlink = new DRKLink(regex.group(1), anchor, new URL(link), attributes, nodo, regex.start(), regex.end());
                                nodo.addLink(newlink);
                            }
                        }
                    }
                } catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    // Script o Style

    /* (non-Javadoc)
	 * @see ar.com.drk.drkspiderjava.processor.Parser#getSource(ProgressListener progress)
     */
    @Override
    public String getSource(ProgressListener progress)
    {
        return nodeContent(progress);
    }

    @Override
    public void finishedGetContent(String content, boolean ok)
    {
        getHeaderInfo(content);

        if (DRKTools.prefs.getBoolean("show_seo_in_tree", true))
        {
            nodo.createSEOReview(content);
        }
    }

    @Override
    public String getParserMIMEType()
    {
        return "text/html.*";
    }

    void getHeaderInfo(String html)
    {
        try
        {
            // Title
            Pattern p = Pattern.compile("\\Q<title>\\E([^\\<]+)\\Q</title>\\E", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(html);
            if (m.find())
            {
                nodo.setTitle(m.group(1));
            }

            // All meta tags
            p = Pattern.compile("\\Q<meta name=\"\\E([^\"]+)\\Q\" content=\"\\E([^\"]+)", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);//([^\"]+)
            m = p.matcher(html);
            while (m.find())
            {
                nodo.setMeta(m.group(1), new MetaTag(m.group(1), m.group(2), m.start(), m.end()));
            }

            p = Pattern.compile("\\<link([^\\>]+)", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
            m = p.matcher(html);
            while (m.find())
            {
                Pattern sp = Pattern.compile("rel\\=\"([^\"]+)\"", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
                Matcher sm = sp.matcher(m.group(1));
                sm.find();
                String rel = sm.group(1);
                sp = Pattern.compile("href\\=\"([^\"]+)\"", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
                sm = sp.matcher(m.group(1));
                sm.find();
                String href = sm.group(1);
                nodo.setRel(rel, href);
            }
        } catch (Exception e)
        {
            // TODO: handle exception
        }
    }

    public String getHTML()
    {
        return nodo.getContent();
    }

    private void exportEMailAddress(String eMailUrl)
    {

        Set<String> emails = new HashSet<String>();
        if (!extractEMailAddress)
        {
            return;
        }

        try
        {
            int htmlIndex = eMailUrl.indexOf(".html");
            if (htmlIndex < 0)
            {
                return;
            }

            System.out.println("exporteMailAddress: " + eMailUrl);

            Connection con = Jsoup.connect(eMailUrl);
            if (con == null)
            {
                return;
            }
            Document doc = con.get();
            if (doc == null)
            {
                return;
            }
            Pattern p = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
            Matcher matcher = p.matcher(doc.text());

            while (matcher.find())
            {
                emails = model.getEMailAddress();
                emails.add(matcher.group());
                // System.out.println(matcher.group());
            }
            // System.out.println(emails);
        } catch (IllegalArgumentException ex)
        {
            System.out.println("exportEMailAddress::IllegalArgumentException: " + eMailUrl);

        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    // new CHD Begin 12.01.2019
    private synchronized boolean checkUrl(String checkUrl, String searchParameter)
    {

        Document document;
        // define als static or instance var
        URL url1 = null;
        String domain = null;
        String path = null;

        // Step one: Check if the status is valid. Is invalid only insert node in Tree;
        URL url;
        try
        {
            url = new URL(checkUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();
            System.out.println("checkUrl(String checkUrl, String searchParameter): " + checkUrl);
            if (code == 404)
            {
                return true;
            }
        }  catch (UnknownHostException ex)
        {
            System.out.println("HTMLParser::checkUrl(): " + checkUrl + "\n" + ex.getMessage());
            // Logger.getLogger(HTMLParser.class.getName()).log(Level.SEVERE, null, ex);
            return true;
        }
        catch (SSLHandshakeException ex)
        {
            System.out.println("HTMLParser::checkUrl(): " + checkUrl);
            Logger.getLogger(HTMLParser.class.getName()).log(Level.SEVERE, null, ex);
            return true;
        } catch (MalformedURLException ex)
        {
            Logger.getLogger(HTMLParser.class.getName()).log(Level.SEVERE, null, ex);
            return true;
        } catch (IOException ex)
        {
            Logger.getLogger(HTMLParser.class.getName()).log(Level.SEVERE, null, ex);
            return true;
        }

        // Step two: Check, if it necessary to export eMail-Addresse. These eMail-Addresses
        // are saved in the outputDirectory with the filename "eMailAdress.txt".
        exportEMailAddress(checkUrl);

        // Step three: Check, if the domain ("www.heise.de") is equals to the path ("www.heise.de").
        // If the variable followLinkHTML is true, another domain is possible.
        // The same is also true for images
        try
        {
            url1 = new URL(checkUrl);
            path = url1.getHost();
            domain = nodo.getDomain();
            if (!domain.equals(path) && (!followLinkHTML))
            {
                return false;
            }
        } catch (MalformedURLException ex)
        {
            return true;
        }

        if (checkUrl.equals("") || (checkUrl == null))
        {
            return false;
        }
        if (searchParameter.equals(""))
        {
            return true;
        }

        // Step four: Check, if sublinks has searchParameter in the title.
        // Regex expression for searchParameter ist possible
        try
        {

            document = Jsoup.connect(checkUrl).get();
            
			 Connection con = Jsoup.connect(checkUrl);
			// doc = con.get();
			 int statusCode = 	con.response().statusCode();
            System.out.println ("StatusCode:" + statusCode);
            
            
            boolean found = document.title().matches(searchParameter);
            if (found)
            {
                return true;
            }
            Elements linksOnPage = document.select("a[href]");
            for (Element page : linksOnPage)
            {

                if (searchParameter.equals(""))
                {
                    return true;
                } else
                {

                    if (page.text().matches(searchParameter))
                    {
                        return true;
                    } else
                    {
                        return false;
                    }
                }
            }
        } catch (IOException ex)
        {
            return false;
        }

        return false;
    }

    private synchronized boolean checkUrlImage(String checkUrl)
    {

        Document document;
        // define als static or instance var
        URL url1 = null;
        String domain = null;
        String path = null;

        try
        {
            url1 = new URL(checkUrl);
            path = url1.getHost();
            domain = nodo.getDomain();
            if (!domain.equals(path) && (!followLinkIMG))
            {
                return false;
            }
        } catch (MalformedURLException ex)
        {
            return false;
        }

        URL url;
        try
        {
            url = new URL(checkUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            System.out.println("checkUrlImage(String checkUrl)");
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();
            if (code == 404)
            {
                return true;
            }
        } catch (MalformedURLException ex)
        {
            Logger.getLogger(HTMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(HTMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    private synchronized boolean saveURLasHTMLDocument(String Url)
    {
        URL url1 = null;
        String path;
        String domain;
        try
        {
            url1 = new URL(Url);
            path = url1.getHost();
            domain = nodo.getDomain();
            if (!domain.equals(path) && (!followLinkHTML))
            {
                return false;
            }
        } catch (MalformedURLException ex)
        {
            return false;
        }

        Document document = null;
        try
        {
            Connection con = Jsoup.connect(Url);
            System.out.println(" saveURLasHTMLDocument(String Url)");
            
            con = con.ignoreContentType(true);
            if (con == null)
            {
                return false;
            }
            document = con.get();
            if (document == null)
            {
                return false;
            }
            String html = document.outerHtml();
            if (saveResource)
            {
                boolean found = saveDocumentAsHTML(Url.toString(), html);
                return found;
            }
        } catch (HttpStatusException ex)
        {
            // Logger.getLogger(HTMLParser.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println ("HTMLParser::saveURLasHTMLDocument-HttpStatusException: " + Url + "\n" + ex.getMessage()+ " Status: " + ex.getStatusCode());

        } catch (UnsupportedMimeTypeException ex)
        {
            System.out.println("UnsupportedfMimeType Exception");

        } catch (IOException ex)
        {
            Logger.getLogger(HTMLParser.class.getName()).log(Level.SEVERE, null, ex);

        } finally
        {
            return true;
        }
    }

    private boolean saveDocumentAsHTML(String url, String html) throws IOException
    {
        String folder = null;
        int indexname = url.lastIndexOf("/");
        if (indexname == url.length())
        {
            url = url.substring(1, indexname);
        }
        indexname = url.lastIndexOf("/");
        String name = url.substring(indexname + 1, url.length());
        System.out.println("name - saveDocumentAsHTML: " + name);
        //Open a URL Stream
        if (name.equals(""))
        {
            int lastIndexOf = url.lastIndexOf("/");
            int IndexOf = url.indexOf("//");
            if (lastIndexOf < 0)
            {
                lastIndexOf = url.length();
            }
            name = url.substring(IndexOf + 2, lastIndexOf);
            name = name.replace("/", "-");
            
            if((name.indexOf(".png") < 0) && (name.indexOf(".css") < 0)) {
            	name = name + ".html";
            }
        }

        String directory = getDirectoryName(url, outputDirectory);
        if (directory == null)
        {
            return false;
        }
        int htmlIndex = name.indexOf(".html");
        if (htmlIndex < 0)
        {
            int jsIndex = name.indexOf(".js");
            if ((jsIndex < 0) && 
            	(name.indexOf(".png") < 0) && 
            	(name.indexOf(".css") < 0) &&
            	(name.indexOf(".pdf") < 0))
            {
                name = name + ".html";
            } else
            	System.out.println ("saveDocumentAsHTML: " + name);
        }
        String fileName = directory + name;

        if (fileName.length() > 250)
        {
            System.out.println("Pfad-Laenge von 250 ueberschritten!");
            return false;
        }

        
        BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(html);
        } catch (IOException e)
        {
            System.out.println("filename: " + fileName);
        } finally
        {
            if (writer != null)
            {
                writer.close();
            }
        }
        return true;
    }

    public static String getDirectoryName(String url, String location)
    {
        String pathname = null;
        String file = "";
        String relativePath = "";
        try
        {
            URL url1 = new URL(url);
            String filename = url1.getFile();
            int lastIndexOf = filename.lastIndexOf("/");
            if (lastIndexOf >= 0)
            {
                relativePath = filename.substring(0, lastIndexOf + 1);
                file = filename.substring(lastIndexOf + 1);
            }

            String directory = location + url1.getHost() + relativePath;
            if (directory.length() > 250)
            {
                System.out.println("Pfad-Laenge von 250 ueberschritten!");
                return null;
            }
            File dir = new File(directory);
            boolean IsCrated = dir.mkdirs();
            return directory;

        } catch (MalformedURLException ex)
        {
            ex.printStackTrace();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return pathname;
    }

    private void getImages(String src)
    {
        String folder = null;
        int indexname = src.lastIndexOf("/");
        if (indexname == src.length())
        {
            src = src.substring(1, indexname);
        }
        indexname = src.lastIndexOf("/");
        String name = src.substring(indexname + 1, src.length());
        System.out.println(name);
        //Open a URL Stream
        InputStream in = null;
        OutputStream out = null;
        String directory = null;
        try
        {
            URL url = new URL(src);
            in = url.openStream();
            directory = getDirectoryName(url.toString(), outputDirectory);
            String fileName = directory + name;
            if (fileName.length() > 250)
            {
                System.out.println("Pfad-Laenge von 250 ueberschritten!");
                return;
            }

            out = new BufferedOutputStream(new FileOutputStream(fileName));
            for (int b; (b = in.read()) != -1;)
            {
                out.write(b);
            }
        } catch (IOException ex)
        {
            if (directory != null)
            {
                System.out.println("directory: " + directory);
                File dir = new File(directory);
                dir.delete();
            }
            return;
        } finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
                if (in != null)
                {
                    in.close();
                }
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }
private boolean saveURLAsFile(String url)
    {
        String folder = null;
        int indexname = url.lastIndexOf("/");
        if (indexname == url.length())
        {
            url = url.substring(1, indexname);
        }
        indexname = url.lastIndexOf("/");
        String name = url.substring(indexname + 1, url.length());
        System.out.println(name);
        //Open a URL Stream
        
        String directory = getDirectoryName(url, outputDirectory);
        if (directory == null)
        {
            return false;
        }
        String fileName = directory + name;
        if (fileName.length() > 250)
        {
            System.out.println("Pfad-Laenge von 250 ueberschritten!");
            return false;
        }

        if ((name.indexOf(".png") > 0) || (name.indexOf(".css") > 0)) {
        	if (name.indexOf(".html") > 0) {
        		name.replace (".html", "");
        		System.out.println ("saveURLAsFile: " + name);
        	} else
        		System.out.println ("saveURLAsFile: " + name);
        	
        	
        }
        if (name.indexOf(".js") > 0)
        {
            BufferedInputStream inputStream = null;
            FileOutputStream fileOS = null;
            try
            {
                inputStream = new BufferedInputStream(new URL(url).openStream());
                fileOS = new FileOutputStream(fileName);
                byte data[] = new byte[1024];
                int byteContent;
                while ((byteContent = inputStream.read(data, 0, 1024)) != -1)
                {
                    fileOS.write(data, 0, byteContent);
                }
            } catch (FileNotFoundException ex)
            {
                System.out.println ("HTMLParser::saveURLAsFile-FileNotFound: " + url);
            } catch (IOException ex)
            {
                ex.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) inputStream.close();
                    if (fileOS != null) fileOS.close();
                } catch (IOException ex) {
                    
                }
            }
        }
        
        return true;
    }

}
