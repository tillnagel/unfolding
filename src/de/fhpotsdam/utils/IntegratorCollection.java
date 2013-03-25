package de.fhpotsdam.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * This class offers a basic collection for {@link Integrator}s.
 * 
 * @author Bram Gotink
 */
public class IntegratorCollection {

	private final Map<String, Integrator> integrators;

	public IntegratorCollection() {
		integrators = new HashMap<String, Integrator>();
	}

	public boolean contains(String name) {
		return integrators.containsKey(name);
	}

	private void assertNotContains(String name) throws IllegalArgumentException {
		if (contains(name))
			throw new IllegalArgumentException(String.format("Name %s already taken", name));
	}

	private void assertContains(String name) throws NoSuchElementException {
		if (!contains(name))
			throw new NoSuchElementException(String.format("No integrator registered for name %s", name));
	}

	public void add(String name) {
		assertNotContains(name);
		integrators.put(name, new Integrator());
	}

	public void add(String name, float value) {
		assertNotContains(name);
		integrators.put(name, new Integrator(value));
	}

	public void add(String name, float value, float damping, float attraction) {
		assertNotContains(name);
		integrators.put(name, new Integrator(value, damping, attraction));
	}

	public void remove(String name) {
		integrators.remove(name);
	}

	/**
	 * Update for next time step. Returns true if any of the contained
	 * integrators actually updated, false if no longer changing.
	 */
	public boolean update() {
		boolean retVal = false;
		for (Integrator integrator : integrators.values()) {
			if(integrator.update()) retVal = true;
		}
		return retVal;
	}

	public float getValue(String name) {
		assertContains(name);
		return integrators.get(name).value;
	}

	public void setValue(String name, double value) {
		assertContains(name);
		integrators.get(name).set(value);
	}

	public float getTarget(String name) {
		assertContains(name);
		return integrators.get(name).target;
	}

	public void setTarget(String name, double target) {
		assertContains(name);
		integrators.get(name).target(target);
	}
}
