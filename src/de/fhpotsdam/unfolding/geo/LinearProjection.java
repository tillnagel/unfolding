package de.fhpotsdam.unfolding.geo;

import processing.core.PVector;

public class LinearProjection extends AbstractProjection {

  public LinearProjection() {
    super(0);
  }

  public LinearProjection(float zoom) {
    super(zoom, new Transformation(1, 0, 0, 0, 1, 0));
  }

  public LinearProjection(float zoom, Transformation transformation) {
    super(zoom, transformation);
  }  
  
  public PVector rawProject(PVector point) {
    return new PVector(point.x, point.y);
  }

  public PVector rawUnproject(PVector point) {
    return new PVector(point.x, point.y);
  }

}
