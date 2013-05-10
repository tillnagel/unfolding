package de.fhpotsdam.unfolding.examples.fun;

public class Integrator {

	static final float ATTRACTION = 0.15f; // formerly 0.1f
	static final float DAMPING = 0.5f; // formerly 0.9f

	float value = 0;
	float vel = 0;
	float accel = 0;
	float force = 0;
	float mass = 1;

	float damping;
	float attraction;

	boolean targeting;
	float target;

	public Integrator() {
		this.value = 0;
		this.damping = DAMPING;
		this.attraction = ATTRACTION;
	}

	public Integrator(float value) {
		this.value = value;
		this.damping = DAMPING;
		this.attraction = ATTRACTION;
	}

	public Integrator(float value, float attraction, float damping) {
		this.value = value;
		this.damping = damping;
		this.attraction = attraction;
	}

	public void set(float v) {
		value = v;
	}

	public float get() {
		return value;
	}

	public void setAttraction(float a) {
		attraction = a;
	}

	public void setDamping(float d) {
		damping = d;
	}

	public boolean update() {
		if (targeting) {
			force += attraction * (target - value);
			accel = force / mass;
			vel = (vel + accel) * damping;
			value += vel;
			force = 0;
			return (vel > 0.0001f);
		}
		return false;
	}

	public void target(float t) {
		targeting = true;
		target = t;
	}
}