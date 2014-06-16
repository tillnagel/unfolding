package de.fhpotsdam.unfolding.utils;

import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.geo.Location;

public class DecodePolylineTestApp extends PApplet {

	public void setup() {
		size(800, 600);

		String polyline5Digits = "galqHisis@gA}@pBsHuAaB}CgAyI_P@IKa@tDoG";
		String polyline6Digits = "ouaw~AcjijObM~F~Ocs@~DwTbAoF`B_Ekv@}`AqEcFaFoD}GqBmTqBgEyBuCiF{JeX_KgUyUq\\eo@ueA}G{MXqBc@iFuAkBv@}EbL}R~h@my@";

		List<Location> locations = GeoUtils.decodePolyline(polyline5Digits, 5);
		Location locationA = locations.get(0);
		println("First location of 5 digits array " + locationA + " is "
				+ ((locationA.getLat() == 50.20196f && locationA.getLon() == 8.57413f) ? " ok" : " not ok"));

		locations = GeoUtils.decodePolyline(polyline6Digits, 6);
		Location locationB = locations.get(0);
		println("First location of 6 digits array " + locationB + " is "
				+ ((locationB.getLat() == 50.20196f && locationB.getLon() == 8.57413f) ? " ok" : " not ok"));
	}

}
