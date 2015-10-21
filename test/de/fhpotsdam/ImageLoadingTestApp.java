package de.fhpotsdam;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import processing.core.PApplet;
import processing.core.PImage;

public class ImageLoadingTestApp extends PApplet {

	PImage image = null;

	public void setup() {
		size(600, 600);

		try {
			String filename = "http://a.tile.cloudmade.com/607e6483654b5c47b9056791d607ab74/65678/256/6/31/20.png";
			URL url = new URL(filename);
			URLConnection con = url.openConnection();
			con.setUseCaches(false);
			
			
			
			//InputStream is = url.openStream();
			InputStream is = con.getInputStream();
			byte bytes[] = loadBytes(is);
			if (bytes == null) {
			} else {
				Image awtImage = Toolkit.getDefaultToolkit().createImage(bytes);
				MediaTracker tracker = new MediaTracker(this.frame);
				tracker.addImage(awtImage, 0);
				try {
					tracker.waitForAll();
				} catch (InterruptedException e) {
					// e.printStackTrace(); // non-fatal, right?
				}

				image = new PImage(awtImage);
			}
		} catch (Exception e) {
			println(e);
		}
	}

	public void draw() {
		background(240);

		if (image != null) {
			image(image, 0, 0);
		}
	}

}
