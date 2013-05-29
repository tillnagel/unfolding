package de.fhpotsdam.unfolding.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import processing.core.PVector;
import de.fhpotsdam.unfolding.geo.Location;

/**
 * Provides utility functions to simplify points, paths, and polygons.
 */
public class GeneralizationUtils {

	/**
	 * Calculates the moving average of vertices. Can be used to smoothen paths when the vertices are geo-locations.
	 * 
	 * <p>
	 * Opposite to {@link #simplify(List, float, boolean)} this keeps the same amount of vertices, so does not improve
	 * performance, but creates smoother paths. You might want to combine both methods.
	 * </p>
	 * 
	 * @param vertices
	 *            The vertices to smoothen.
	 * @param np
	 *            The number of points to average over.
	 * @return The smoothened vertices.
	 */
	public static List<PVector> computeMovingAverage(List<PVector> vertices, int np) {
		// Convert to primitive arrays for both x and y
		float[] xValues = new float[vertices.size()];
		float[] yValues = new float[vertices.size()];
		for (int i = 0; i < vertices.size(); i++) {
			xValues[i] = vertices.get(i).x;
			yValues[i] = vertices.get(i).y;
		}

		// Compute moving averages
		float[] xAvgValues = computeMovingAverage(xValues, np);
		float[] yAvgValues = computeMovingAverage(yValues, np);

		// Combine averaged x and y back to vertices
		List<PVector> averageVertices = new ArrayList<PVector>();
		for (int i = 0; i < xAvgValues.length; i++) {
			averageVertices.add(new PVector(xAvgValues[i], yAvgValues[i]));
		}

		return averageVertices;
	}

	/**
	 * Calculates the np-point moving average for some values.
	 * 
	 * @param np
	 *            number of points to average over.
	 * @return The smoothened values.
	 */
	protected static float[] computeMovingAverage(float[] values, int np) {
		float[] mm = new float[values.length - np];
		for (int i = 0; i < mm.length; i++) {
			float sum = 0;
			for (int j = 0; j < np; j++) {
				sum = sum + values[i + j];
			}
			mm[i] = sum / np;
		}
		return mm;
	}

	/**
	 * Simplifies points by using Douglas-Peucker algorithm. Can be used to reduce complexity. This is zoom-dependent
	 * when used for {@link Location}s due to the tolerance value.
	 * 
	 * <p>
	 * Opposite to {@link #computeMovingAverage(List, int)} this reduces the amount of vertices, and thus improves
	 * performance, but may result in still too complex paths. You might want to combine both methods.
	 * </p>
	 * 
	 * 
	 * <p>
	 * Based on Simplify.js by Vladimir Agafonkin. Ported to Java and modified for Unfolding by Till Nagel.
	 * </p>
	 * 
	 * @param points
	 *            A list of vertices.
	 * @param tolerance
	 *            The amount of simplification (in the same metric as the points).
	 * @param highestQuality
	 *            Whether to exclude distance-based preprocessing step which leads to highest quality simplification but
	 *            runs ~10-20 times slower.
	 * @return A list of simplified vertices.
	 */
	public static List<PVector> simplify(List<PVector> points, float tolerance, boolean highestQuality) {
		float sqTolerance = tolerance * tolerance;

		if (!highestQuality) {
			points = simplifyRadialDistance(points, sqTolerance);
		}

		points = simplifyDouglasPeucker(points, sqTolerance);

		return points;
	}

	/**
	 * Square distance between 2 points.
	 */
	protected static float getSquareDistance(PVector p1, PVector p2) {
		float dx = p1.x - p2.x;
		float dy = p1.y - p2.y;

		return dx * dx + dy * dy;
	}

	/**
	 * Square distance from a point to a segment.
	 */
	protected static float getSquareSegmentDistance(PVector p, PVector p1, PVector p2) {
		float x = p1.x;
		float y = p1.y;

		float dx = p2.x - x;
		float dy = p2.y - y;

		float t;

		if (dx != 0 || dy != 0) {

			t = ((p.x - x) * dx + (p.y - y) * dy) / (dx * dx + dy * dy);

			if (t > 1) {
				x = p2.x;
				y = p2.y;

			} else if (t > 0) {
				x += dx * t;
				y += dy * t;
			}
		}

		dx = p.x - x;
		dy = p.y - y;

		return dx * dx + dy * dy;
	}

	/**
	 * Distance-based simplification.
	 * 
	 * @param points
	 *            List of vertices.
	 * @param sqTolerance
	 *            The square tolerance value.
	 * @return A list of simplified vertices.
	 */
	protected static List<PVector> simplifyRadialDistance(List<PVector> points, float sqTolerance) {

		int i;
		float len = points.size();
		PVector point = null;
		PVector prevPoint = points.get(0);
		List<PVector> newPoints = new ArrayList<PVector>();
		newPoints.add(prevPoint);

		for (i = 1; i < len; i++) {
			point = points.get(i);

			if (getSquareDistance(point, prevPoint) > sqTolerance) {
				newPoints.add(point);
				prevPoint = point;
			}
		}

		if (prevPoint != point) {
			newPoints.add(point);
		}

		return newPoints;
	}

	/**
	 * Simplification using optimized Douglas-Peucker algorithm with recursion elimination.
	 * 
	 * @param points
	 *            List of vertices.
	 * @param sqTolerance
	 *            The square tolerance value.
	 * @return A list of simplified vertices.
	 */
	protected static List<PVector> simplifyDouglasPeucker(List<PVector> points, float sqTolerance) {

		int len = points.size();

		List<Integer> markers = new ArrayList<Integer>(len);
		for (int i = 0; i < len; i++) {
			markers.add(i, 0);
		}

		int first = 0;
		int last = len - 1;

		int i;
		float maxSqDist;
		float sqDist;
		int index = 0;

		Stack<Integer> firstStack = new Stack<Integer>();
		Stack<Integer> lastStack = new Stack<Integer>();

		List<PVector> newPoints = new ArrayList<PVector>();

		markers.set(first, 1);
		markers.set(last, 1);

		while (last > 0) {

			maxSqDist = 0;

			for (i = first + 1; i < last; i++) {
				sqDist = getSquareSegmentDistance(points.get(i), points.get(first), points.get(last));

				if (sqDist > maxSqDist) {
					index = i;
					maxSqDist = sqDist;
				}
			}

			if (maxSqDist > sqTolerance) {
				markers.set(index, 1);

				firstStack.push(first);
				lastStack.push(index);

				firstStack.push(index);
				lastStack.push(last);
			}

			if (firstStack.isEmpty()) {
				first = 0;
			} else {
				first = firstStack.pop();
			}
			if (lastStack.isEmpty()) {
				last = 0;
			} else {
				last = lastStack.pop();
			}
		}

		for (i = 0; i < len; i++) {
			if (markers.get(i) != 0) {
				newPoints.add(points.get(i));
			}
		}

		return newPoints;
	}

}
