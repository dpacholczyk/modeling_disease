package testy.basemodels;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public abstract class SimulationObject {
	protected Grid<Object> grid;
	protected ContinuousSpace<Object> space;

	public SimulationObject(Grid<Object> grid, ContinuousSpace<Object> space) {
		this.grid = grid;
		this.space = space;
	}
}
