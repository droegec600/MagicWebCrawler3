package com.droegec.mwc;

import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;




class ImageIconExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	//URL imageURL = getClass().getClassLoader().getResource("resources/images/control_play_new.png");
	ImageIcon Im = new ImageIcon("/images/control_play_new.png");
	if (Im == null) 
		System.out.println ("ImageIcon is NULL");
	else
		System.out.println ("ImageIcon is not NULL");

	}
/*
    private ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
*/
}
