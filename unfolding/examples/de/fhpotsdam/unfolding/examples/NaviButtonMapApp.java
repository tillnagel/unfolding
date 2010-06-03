package de.fhpotsdam.unfolding.examples;

import org.apache.log4j.Logger;

import codeanticode.glgraphics.GLConstants;

import processing.core.PApplet;
import processing.core.PFont;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.events.PanMapEvent;
import de.fhpotsdam.unfolding.events.ZoomMapEvent;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.interactions.KeyboardHandler;
import de.fhpotsdam.unfolding.interactions.MouseHandler;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.MapUtils;


@SuppressWarnings("serial")
public class NaviButtonMapApp extends PApplet{
	
	public static Logger log = Logger.getLogger(NaviButtonMapApp.class);
		
	DebugDisplay debugDisplay;
	
	EventDispatcher eventDispatcher;
	
	Map map;
	
	public void setup(){
		size(800, 600, GLConstants.GLGRAPHICS);
		smooth();
		
		textFont(loadFont("Miso-Light-12.vlw"));
				
		// Creates default mapDisplay
		map = new Map(this, "map", 0, 0, 600, 600);
		
		debugDisplay = new DebugDisplay(this, map.mapDisplay, 600, 100, 250, 150);
		
		map.zoomToLevel(3);
		map.panCenterTo(new Location(0, 0));
		
		//default dispatcher
		eventDispatcher = MapUtils.createDefaultEventDispatcher(this, map);
	}
	
	public void draw(){
		background(0);
		map.draw();
		
		debugDisplay.draw();
		
		//Simple "Go to Berlin" - Button
		fill(255,0,0);
		rect(600, 0, 200, 100);
		fill(0);
		text("go to Berlin",650,50);
	}
	
	@Override
	public void mouseClicked() {
		if(mouseX>600 && mouseX<800
				&& mouseY>0 && mouseY < 100){
			
			//zoom and pan to berlin
			log.debug("Go To Berlin");
			PanMapEvent panMapEvent = new PanMapEvent(this, map.getId());
			Location berlin = new Location(52.439046f, 13.447266f);
			panMapEvent.setLocation(berlin);
			eventDispatcher.fireMapEvent(panMapEvent);
			ZoomMapEvent zoomMapEvent = new ZoomMapEvent(this, map.getId());
			zoomMapEvent.setSubType("zoomTo");
			zoomMapEvent.setZoomLevel(8);
			eventDispatcher.fireMapEvent(zoomMapEvent);
		}
	}

}
