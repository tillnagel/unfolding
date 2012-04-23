package de.fhpotsdam.unfolding.geo;

import processing.core.PApplet;
import processing.core.PVector;
import de.fhpotsdam.unfolding.core.Coordinate;

public abstract class AbstractProjection {

	public float zoom;
	public Transformation transformation;

	public AbstractProjection() {
		this(0);
	}

	public AbstractProjection(float zoom) {
		this(zoom, new Transformation(1, 0, 0, 0, 1, 0));
	}

	public AbstractProjection(float zoom, Transformation transformation) {
		this.zoom = zoom;
		this.transformation = transformation;
	}

	public abstract PVector rawProject(PVector point);

	public abstract PVector rawUnproject(PVector point);

	public PVector project(PVector point) {
		point = this.rawProject(point);
		if (this.transformation != null) {
			point = this.transformation.transform(point);
		}
		return point;
	}

	public PVector unproject(PVector point) {
		if (this.transformation != null) {
			point = this.transformation.untransform(point);
		}
		point = this.rawUnproject(point);
		return point;
	}

	public Coordinate locationCoordinate(Location location) {
		PVector point = new PVector(PApplet.PI * location.y / 180.0f, PApplet.PI * location.x / 180.0f);
		point = this.project(point);
		return new Coordinate(point.y, point.x, this.zoom);
	}

	public Location coordinateLocation(Coordinate coordinate) {
		coordinate = coordinate.zoomTo(this.zoom);
		PVector point = new PVector(coordinate.column, coordinate.row);
		point = this.unproject(point);
		return new Location(180.0f * point.y / PApplet.PI, 180.0f * point.x / PApplet.PI);
	}

}
