package testy;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import testy.basemodels.Person;
import testy.utils.Disease;


public class Healthy extends Person {
	private boolean isSick = false;
	
	private double infectionTime = 0;

	public Healthy(boolean immune, Grid<Object> grid,
			ContinuousSpace<Object> space) {
		super(immune, grid, space);
	}

	public boolean isSick() {
		return isSick;
	}

	public void setSick(boolean isSick) {
		this.isSick = isSick;
	}

	public double getInfectionTime() {
		return infectionTime;
	}

	public void setInfectionTime(double infectionTime) {
		this.infectionTime = infectionTime;
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void checkIfInfected() {
		double currentTick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		if(isSick) {
			double diff = currentTick - infectionTime;
			if((Disease.INCUBATION_TIME - diff) == 0) {
				GridPoint gridPoint = grid.getLocation(this);
				NdPoint objPoint = space.getLocation(this);
				Context<Object> context = ContextUtils.getContext(this);
				context.remove(this);
				Infected infected = new Infected(false, grid, space);
				infected.setAtWork(this.isAtWork());
				infected.setAtHome(this.isAtHome());
				infected.setMode(this.getMode());
				infected.setTimeAtWork(this.getTimeAtWork());
				infected.setTimeAtHome(this.getTimeAtHome());
				infected.setId(this.getId());
				infected.setWorkPlace(this.getWorkPlace());
				infected.setHomePosition(this.getHomePosition());
				context.add(infected);
				space.moveTo(infected, objPoint.getX(), objPoint.getY());
				grid.moveTo(infected, gridPoint.getX(), gridPoint.getY());				
			}
		}
	}
}
