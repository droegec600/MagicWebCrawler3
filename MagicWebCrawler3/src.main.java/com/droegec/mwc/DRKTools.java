package com.droegec.mwc;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Window;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RepaintManager;

public class DRKTools {

	public final static SimpleDateFormat dateFormatter = new SimpleDateFormat();

	public final static Preferences prefs =Preferences.userNodeForPackage(DRKTools.class);
	public final static DecimalFormat decimal_two = new DecimalFormat("#.00");

	public static final ImageIcon OK_ICON = new ImageIcon("/images/good.png");
	public static final ImageIcon FORBIDEN_ICON = new ImageIcon("/images/forbiden.png");
	public static final ImageIcon BAD_ICON = new ImageIcon("/images/bad.png");
	public static final ImageIcon UK_ICON = new ImageIcon("/images/uk.png");

	
	
        Preferences   prefs1 = Preferences.userRoot().node(this.getClass().getName());
	/**
	 * Retorna las coordenadas de ubicaci�n de una ventana
	 * seg�n su ventana padre
	 * @param wnd ventana que desea ubicar
	 * @return punto calculado de ubicaci�n
	 */
	public static Point getCenterLocation(Window wnd) {
		Point l = wnd.getOwner().getLocation();
		int x = l.x +  (wnd.getOwner().getWidth() - wnd.getWidth())/ 2;
		int y = l.y +  (wnd.getOwner().getHeight() - wnd.getHeight())/ 2;
		return new Point(x, y);
	}
	
	/**
	 * Calcula la diferencia de d�as
	 * @param d1
	 * @param d2
	 * @return d1 - d2 en d�as
	 */
	public static int dateDiffDays(java.util.Date d1, java.util.Date d2) {
		return (int) ((d1.getTime() / 86400000) - (d2.getTime() / 86400000));
	}
	/**
	 * Calcula la diferencia de a�os con error de -2/36525 d�as
	 * @param d1
	 * @param d2
	 * @return d1 - d2 en a�os
	 */
	public static int dateDiffYears(java.util.Date d1, java.util.Date d2) {
		int days = dateDiffDays(d1, d2);
		int years = (days - (days/1460))/365;
		return years;
	}
	
	public static String secondsToHoursMinutesSeconds(long s) {
		long hours = TimeUnit.SECONDS.toHours(s);
		long minutes = TimeUnit.SECONDS.toMinutes(s) - (TimeUnit.SECONDS.toHours(s)* 60);
		long seconds = TimeUnit.SECONDS.toSeconds(s) - (TimeUnit.SECONDS.toMinutes(s) *60);
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
	
	public static void disableDoubleBuffering(Component c) {
		RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(false);
	}
	
	public static void enableDoubleBuffering(Component c) {
		RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(true);
	}
	
	/**
	 * Abre el enlace en el navegador por defecto
	 * @param url Texto con la URL
	 * @param frame Padre
	 * @return true si lo puede mostrar
	 */
	public static boolean showInBrowser(String url, Frame frame)
	  {
	    //minimizes the app
	    if (frame != null)
	        frame.setExtendedState(JFrame.ICONIFIED);
	    
	    String os = System.getProperty("os.name").toLowerCase();
	    Runtime rt = Runtime.getRuntime();
	    try
	    {
	            if (os.indexOf( "win" ) >= 0)
	            {
	              String[] cmd = new String[4];
	              cmd[0] = "cmd.exe";
	              cmd[1] = "/C";
	              cmd[2] = "start";
	              cmd[3] = url;
	              rt.exec(cmd);
	            }
	            else if (os.indexOf( "mac" ) >= 0)
	            {
	                rt.exec( "open " + url);
	            }
	            else
	            {
	              //prioritized 'guess' of users' preference
	              String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
	                  "netscape","opera","links","lynx"};
	 
	              StringBuffer cmd = new StringBuffer();
	              for (int i=0; i<browsers.length; i++)
	                cmd.append( (i==0  ? "" : " || " ) + browsers[i] +" \"" + url + "\" ");
	 
	              rt.exec(new String[] { "sh", "-c", cmd.toString() });
	           }
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(frame,
	                                            "\n\n The system failed to invoke your default web browser while attempting to access: \n\n " + url + "\n\n",
	                                            "Browser Error",
	                                            JOptionPane.WARNING_MESSAGE);
	 
	        return false;
	    }
	    return true;
	}
}
