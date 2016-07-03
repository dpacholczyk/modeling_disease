package testy.basemodels;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Hospital extends SimulationObject {

	private int capacity = 10;
	
	private int peopleIn = 0;
	
	public Hospital(Grid<Object> grid, ContinuousSpace<Object> space) {
		super(grid, space);
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getPeopleIn() {
		return peopleIn;
	}

	public void setPeopleIn(int peopleIn) {
		this.peopleIn = peopleIn;
	}
	
	
}
