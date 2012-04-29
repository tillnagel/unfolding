package de.fhpotsdam.unfolding.examples.fun;

import processing.core.PApplet;

public class ColorIntegrator {

	Integrator a;
	Integrator r;
	Integrator g;
	Integrator b;

	int value;

	public ColorIntegrator(int c) {
		this(c, Integrator.ATTRACTION, Integrator.DAMPING);
	}

	public ColorIntegrator(int c, float attraction, float damping) {
		a = new Integrator((c >> 24) & 0xff, attraction, damping);
		r = new Integrator((c >> 16) & 0xff, attraction, damping);
		g = new Integrator((c >> 8) & 0xff, attraction, damping);
		b = new Integrator(c & 0xff, attraction, damping);
	}

	public void set(int c) {
		a.set((c >> 24) & 0xff);
		r.set((c >> 16) & 0xff);
		g.set((c >> 8) & 0xff);
		b.set(c & 0xff);
	}

	public int get() {
		return value;
	}

	public void setAttraction(float attr) {
		a.setAttraction(attr);
		r.setAttraction(attr);
		g.setAttraction(attr);
		b.setAttraction(attr);
	}

	public void setDamping(float damp) {
		a.setDamping(damp);
		r.setDamping(damp);
		g.setDamping(damp);
		b.setDamping(damp);
	}

	public boolean update() {
		boolean updated = false;

		updated |= a.update();
		updated |= r.update();
		updated |= g.update();
		updated |= b.update();

		int a0 = PApplet.constrain((int) a.value, 0, 255);
		int r0 = PApplet.constrain((int) r.value, 0, 255);
		int g0 = PApplet.constrain((int) g.value, 0, 255);
		int b0 = PApplet.constrain((int) b.value, 0, 255);

		value = (a0 << 24) | (r0 << 16) | (g0 << 8) | b0;

		return updated;
	}

	public void target(int c) {
		a.target((c >> 24) & 0xff);
		r.target((c >> 16) & 0xff);
		g.target((c >> 8) & 0xff);
		b.target(c & 0xff);
	}
}
