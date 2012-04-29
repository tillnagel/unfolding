package de.fhpotsdam.unfolding.geo;

import processing.core.PApplet;
import processing.core.PVector;

public class MercatorProjection extends AbstractProjection {

  public MercatorProjection() {
    super(0);
  }

  public MercatorProjection(float zoom) {
    super(zoom, new Transformation(1, 0, 0, 0, 1, 0));
  }

  public MercatorProjection(float zoom, Transformation transformation) {
    super(zoom, transformation);
  }  
  
  public PVector rawProject(PVector point) {
    return new PVector(point.x, PApplet.log(PApplet.tan(0.25f * PApplet.PI + 0.5f * point.y)));
  }

  public PVector rawUnproject(PVector point) {
    return new PVector(point.x, 2.0f * PApplet.atan(PApplet.pow((float)Math.E, point.y)) - 0.5f * PApplet.PI);
  }

}
