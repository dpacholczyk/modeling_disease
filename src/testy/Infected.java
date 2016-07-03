package testy;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import testy.basemodels.Hospital;
import testy.basemodels.Person;
import testy.utils.Config;
import testy.utils.MoveMode;

public class Infected extends Person {
	
	public Infected(boolean immune, Grid<Object> grid,
			ContinuousSpace<Object> space) {
		super(immune, grid, space);
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void infect() {
		double currentTick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		GridPoint gridPoint = grid.getLocation(this);
		List<Object> healthyPeople = new ArrayList<Object>();
		for(Object obj : grid.getObjectsAt(gridPoint.getX(), gridPoint.getY())) {
			if(obj instanceof Healthy) {
				healthyPeople.add(obj);
			}
		}
		for(Object obj : grid.getObjectsAt(gridPoint.getX()+1, gridPoint.getY())) {
			if(obj instanceof Healthy) {
				healthyPeople.add(obj);
			}
		}
		for(Object obj : grid.getObjectsAt(gridPoint.getX()+1, gridPoint.getY()+1)) {
			if(obj instanceof Healthy) {
				healthyPeople.add(obj);
			}
		}
		for(Object obj : grid.getObjectsAt(gridPoint.getX(), gridPoint.getY()+1)) {
			if(obj instanceof Healthy) {
				healthyPeople.add(obj);
			}
		}
		for(Object obj : grid.getObjectsAt(gridPoint.getX()-1, gridPoint.getY())) {
			if(obj instanceof Healthy) {
				healthyPeople.add(obj);
			}
		}
		for(Object obj : grid.getObjectsAt(gridPoint.getX()-1, gridPoint.getY()-1)) {
			if(obj instanceof Healthy) {
				healthyPeople.add(obj);
			}
		}
		for(Object obj : grid.getObjectsAt(gridPoint.getX(), gridPoint.getY()-1)) {
			if(obj instanceof Healthy) {
				healthyPeople.add(obj);
			}
		}
		for(Object healthyPerson : healthyPeople) {
			if(!((Healthy)healthyPerson).isImmune()) {
				((Healthy)healthyPerson).setSick(true);
				((Healthy)healthyPerson).setInfectionTime(currentTick);
			}
		}
		
		goToHospital();
		cure();
	}

	private void goToHospital() {
		Context<Object> context = ContextUtils.getContext(this);
		for(Object obj : context) {
			if(obj instanceof Hospital) {
				Object hospital = obj;
				NdPoint personPoint = space.getLocation(this);
				if(nearestHospital == null) {
					nearestHospital = (Hospital) hospital;
				} else {
					NdPoint hospitalPoint = space.getLocation(hospital);
					if(space.getDistance(personPoint, hospitalPoint) < space.getDistance(personPoint, space.getLocation((Object)nearestHospital))) {
						nearestHospital = (Hospital) hospital;
					}
				}
			}
		}
		
		mode = MoveMode.TO_HOSPITAL;
	}
	
	private void cure() {
		if(atHospital) {
			System.out.println("W szpitalu");
			double currentTick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
			if(currentTick - timeAtHospital < Config.HEALING_TIME) {
				mode = MoveMode.TO_HOME;
				atHospital = false;
				immune = true;
				
				GridPoint gridPoint = grid.getLocation(this);
				NdPoint objPoint = space.getLocation(this);
				Context<Object> context = ContextUtils.getContext(this);
				context.remove(this);
				Healthy h = new Healthy(true, grid, space);
				h.setAtWork(this.isAtWork());
				h.setAtHome(this.isAtHome());
				h.setMode(this.getMode());
				h.setTimeAtWork(this.getTimeAtWork());
				h.setTimeAtHome(this.getTimeAtHome());
				h.setId(this.getId());
				h.setWorkPlace(this.getWorkPlace());
				h.setHomePosition(this.getHomePosition());
				context.add(h);
				space.moveTo(h, objPoint.getX(), objPoint.getY());
				grid.moveTo(h, gridPoint.getX(), gridPoint.getY());	
			}
		}
	}
	
}
