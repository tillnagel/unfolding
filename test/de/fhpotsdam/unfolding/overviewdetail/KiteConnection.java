package de.fhpotsdam.unfolding.overviewdetail;

import de.fhpotsdam.unfolding.examples.overviewdetail.connection.OverviewPlusDetailConnection;
import processing.core.PApplet;
import processing.core.PVector;

public class KiteConnection implements OverviewPlusDetailConnection {

	private PApplet p;

	protected float width = 150;
	protected float height = 150;

	protected float padding = 6;

	// Destination this connection points to (detail pos)
	protected PVector dest = new PVector();
	// Position of the rectangle (zoom map)
	protected PVector pos = new PVector();

	// Corners of the background rectangle
	protected PVector tl = new PVector();
	protected PVector tr = new PVector();
	protected PVector bl = new PVector();
	protected PVector br = new PVector();

	public KiteConnection(PApplet p) {
		this.p = p;
	}

	@Override
	public void setDetailSize(float width, float height) {
		this.width = width;
		this.height = height;
	}
	
	public void setOverviewSize(float width, float height) {
		PApplet.println("KiteConnection only allows single overview/destination points.");
	}

	@Override
	public void setPadding(float padding) {
		this.padding = padding;
	}

	@Override
	public void setDetailPosition(float x, float y) {
		setDestination(x, y);
	}

	@Override
	public void setOverviewPosition(float x, float y) {
		setPosition(x, y);
	}
	
	public void setPosition(float x, float y) {
		pos.x = x - width / 2;
		pos.y = y - height / 2;

		tl.x = pos.x - padding;
		tl.y = pos.y - padding;

		tr.x = pos.x + width + padding;
		tr.y = pos.y - padding;

		bl.x = pos.x - padding;
		bl.y = pos.y + height + padding;

		br.x = pos.x + width + padding;
		br.y = pos.y + height + padding;
	}

	public void setDestination(float x, float y) {
		dest.x = x;
		dest.y = y;
	}

	public void drawDebug() {
		p.fill(240);
		p.rect(pos.x, pos.y, width, height);

		p.fill(255, 0, 0, 100);
		p.ellipse(tr.x, tr.y, 10, 10);
		p.ellipse(tl.x, tl.y, 10, 10);
	}

	public void draw() {
		p.fill(100, 100);
		p.noStroke();
		p.beginShape();

		// Start point (only if dest not in center middle (5) )
		if (!(dest.y > tr.y && dest.y < br.y && dest.x >= tl.x && dest.x < tr.x)) {
			vertex(dest);
		}

		if (dest.y <= tr.y) {
			// 1: top left
			if (dest.x < tl.x) {
				vertex(tr);
				vertex(br);
				vertex(bl);
			}
			// 2: top middle
			if (dest.x >= tl.x && dest.x < tr.x) {
				vertex(tr);
				vertex(br);
				vertex(bl);
				vertex(tl);
			}
			// 3: top right
			if (dest.x >= tr.x) {
				vertex(br);
				vertex(bl);
				vertex(tl);
			}
		}

		if (dest.y > tr.y && dest.y < br.y) {
			// 4: center left
			if (dest.x < tl.x) {
				vertex(tl);
				vertex(tr);
				vertex(br);
				vertex(bl);
			}
			// 5: center middle
			if (dest.x >= tl.x && dest.x < tr.x) {
				vertex(tl);
				vertex(tr);
				vertex(br);
				vertex(bl);
			}
			// 6: center right
			if (dest.x > tr.x) {
				vertex(br);
				vertex(bl);
				vertex(tl);
				vertex(tr);
			}
		}

		if (dest.y >= br.y) {
			// 7: bottom left
			if (dest.x < tl.x) {
				vertex(br);
				vertex(tr);
				vertex(tl);
			}
			// 8: bottom middle
			if (dest.x >= tl.x && dest.x < tr.x) {
				vertex(br);
				vertex(tr);
				vertex(tl);
				vertex(bl);
			}
			// 9: bottom right
			if (dest.x >= tr.x) {
				vertex(tr);
				vertex(tl);
				vertex(bl);
			}
		}

		p.endShape(PApplet.CLOSE);
	}

	private void vertex(PVector v) {
		p.vertex(v.x, v.y);
	}

	
}
