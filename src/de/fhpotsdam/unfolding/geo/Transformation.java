package de.fhpotsdam.unfolding.geo;

import processing.core.PVector;

public class Transformation {

  public float ax, bx, cx, ay, by, cy;

  public Transformation(float ax, float bx, float cx, float ay, float by, float cy) {
    this.ax = ax;
    this.bx = bx;
    this.cx = cx;
    this.ay = ay;
    this.by = by;
    this.cy = cy;
  }

  public PVector transform(PVector point) {
    return new PVector(ax*point.x + bx*point.y + cx, ay*point.x + by*point.y + cy);
  }

  public PVector untransform(PVector point) {
    return new PVector((point.x*by - point.y*bx - cx*by + cy*bx) / (ax*by - ay*bx), (point.x*ay - point.y*ax - cx*ay + cy*ax) / (bx*ay - by*ax));
  }

}
