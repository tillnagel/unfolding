package de.fhpotsdam.unfolding.core;

import processing.core.PApplet;

/**
 * Internal(!) representation of coordinates.
 * 
 * It is used to determine which tiles to load. You probably won't use this class.
 */
public class Coordinate {

	public static int MAX_ZOOM = 20;

	public float row, column, zoom;

	public Coordinate(float row, float column, float zoom) {
		this.row = row;
		this.column = column;
		this.zoom = zoom;
	}

	public String toString() {
		return "(" + PApplet.nf(row, 1, 3) + ", " + PApplet.nf(column, 1, 3) + " @"
				+ PApplet.nf(zoom, 1, 3) + ")";
	}

	public boolean equals(Object o) {
		Coordinate c = (Coordinate) o;
		// return PApplet.abs(c.row - row) < PApplet.EPSILON && PApplet.abs(c.column - column) <
		// PApplet.EPSILON && PApplet.abs(c.zoom - zoom) < PApplet.EPSILON;
		return c.row == row && c.column == column && c.zoom == zoom;
	}

	public int hashCode() {
		return toString().hashCode();
	}

	public Coordinate copy() {
		return new Coordinate(row, column, zoom);
	}

	public Coordinate container() {
		return new Coordinate(PApplet.floor(row), PApplet.floor(column), zoom);
	}

	public Coordinate zoomTo(float destination) {
		return new Coordinate(row * PApplet.pow(2, destination - zoom), column
				* PApplet.pow(2, destination - zoom), destination);
	}

	public Coordinate zoomBy(float distance) {
		return new Coordinate(row * PApplet.pow(2, distance), column * PApplet.pow(2, distance),
				zoom + distance);
	}

	// Till: REVISIT up(dist) et al can be unified into getNeighbor(row, col)
	public Coordinate getNeighbor(float rowDist, float colDist) {
		return new Coordinate(row + rowDist, column + colDist, zoom);

		// up = getNeighbor(-1, 0);
		// right = getNeighbor(0, 1);
		// down = getNeighbor(1, 0);
		// left = getNeighbor(0, -1);
		// southWest = getNeighbor(1, -1);
	}

	public Coordinate up() {
		return up(1);
	}

	public Coordinate up(float distance) {
		return new Coordinate(row - distance, column, zoom);
	}

	public Coordinate right() {
		return right(1);
	}

	public Coordinate right(float distance) {
		return new Coordinate(row, column + distance, zoom);
	}

	public Coordinate down() {
		return down(1);
	}

	public Coordinate down(float distance) {
		return new Coordinate(row + distance, column, zoom);
	}

	public Coordinate left() {
		return left(1);
	}

	public Coordinate left(float distance) {
		return new Coordinate(row, column - distance, zoom);
	}

	public void roundValues() {
		row = PApplet.round(row);
		column = PApplet.round(column);
		zoom = PApplet.round(zoom);
	}

}
