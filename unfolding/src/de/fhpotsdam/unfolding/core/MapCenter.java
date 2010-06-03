package de.fhpotsdam.unfolding.core;

import processing.core.PVector;

public class MapCenter {

  public Coordinate coordinate;
  public PVector point; 

  public MapCenter(Coordinate coordinate, PVector point) {
    this.coordinate = coordinate;
    this.point = point;
  }

}
