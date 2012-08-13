package de.fhpotsdam.unfolding.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Generalizes polygons.
 * 
 * Based on Simplify.js by Vladimir Agafonkin.
 * 
 * Ported to Java and modified for Unfolding, Till Nagel.
 * 
 * 
 * 
 * Copyright (c) 2012, Vladimir Agafonkin. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public class GeneralizationUtils {

	// square distance between 2 points
	public static float getSquareDistance(PVector p1, PVector p2) {
		float dx = p1.x - p2.x;
		float dy = p1.y - p2.y;

		return dx * dx + dy * dy;
	}

	// square distance from a point to a segment
	public static float getSquareSegmentDistance(PVector p, PVector p1, PVector p2) {
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

	// distance-based simplification
	public static List<PVector> simplifyRadialDistance(List<PVector> points, float sqTolerance) {

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

	// simplification using optimized Douglas-Peucker algorithm with recursion elimination
	public static List<PVector> simplifyDouglasPeucker(List<PVector> points, float sqTolerance) {

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

	public static List<PVector> simplify(List<PVector> points, float tolerance, boolean highestQuality) {
		
		float sqTolerance = tolerance * tolerance;

		if (!highestQuality) {
			points = simplifyRadialDistance(points, sqTolerance);
		}

		int numPoints = points.size();
		points = simplifyDouglasPeucker(points, sqTolerance);
		int numSimplifiedPoints = points.size();
		//PApplet.println("Reduced " + numPoints + " to " + numSimplifiedPoints + " points.");
		
		return points;
	};

}
