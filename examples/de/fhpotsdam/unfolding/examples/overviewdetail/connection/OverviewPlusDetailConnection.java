package de.fhpotsdam.unfolding.examples.overviewdetail.connection;

public interface OverviewPlusDetailConnection {
	
	public void setPadding(float padding);

	public void setDetailPosition(float x, float y);
	
	public void setDetailSize(float width, float height);
	
	public void setOverviewPosition(float x, float y);
	
	public void setOverviewSize(float width, float height);

	public void draw();
	
}
