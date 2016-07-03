package testy.basemodels;

import java.util.Date;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import testy.utils.Config;
import testy.utils.MoveMode;
import testy.utils.Movement;

public class Person extends SimulationObject {
	protected boolean immune = false;
	protected boolean atWork = false;
	protected boolean atHome = false;
	protected boolean atHospital = false;
	
	protected double timeAtWork = 0;
	protected double timeAtHome = 0;
	protected double timeAtHospital = 0;
	protected double immunity = 0;
	
	protected int id = 0;
	
	protected WorkPlace workPlace = null;
	protected Hospital nearestHospital = null;
	protected NdPoint homePosition = null;

	protected MoveMode mode = null;

	public Person(boolean immune, Grid<Object> grid, ContinuousSpace<Object> space) {
		super(grid, space);
		this.immune = immune;
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		GridPoint pt = grid.getLocation(this);
		move();
	}
	
	public void move() {
		NdPoint personPoint = space.getLocation(this);
		double currentTick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		if(currentTick == 1) {
			homePosition = space.getLocation(this);
			atHome = true;
			if(workPlace != null) {
				mode = MoveMode.TO_WORK;
			} else {
				mode = MoveMode.STAY;
			}
		}
		
		NdPoint otherPoint = null;
		
		if(mode == MoveMode.TO_HOSPITAL) {
			otherPoint = space.getLocation((Object)nearestHospital);
			atHome = false;
			atWork = false;
		} else {
			// decision about setting target: home/work
			if(mode == MoveMode.STAY) {
				if(atWork) {
					if(currentTick - timeAtWork >= Config.TIME_IN_WORK) {
						mode = MoveMode.TO_HOME;
					}
				}
				if(atHome) {
					if(currentTick - timeAtHome >= Config.TIME_IN_HOME) {
						mode = MoveMode.TO_WORK;
					}
				}

			}
			
			// leaving place close - employed
			if(this.workPlace != null && mode != MoveMode.STAY) {
				
				NdPoint workPlacePoint = space.getLocation(workPlace);
				if(mode == MoveMode.TO_HOME) {
					otherPoint = homePosition;
					if(space.getDistance(personPoint, otherPoint) < 1) {
						mode = MoveMode.STAY;
						atWork = false;
						atHome = true;
						timeAtWork = 0;
						timeAtHome = currentTick;
					}
				} 
				if(mode == MoveMode.TO_WORK) {
					otherPoint = new NdPoint(workPlacePoint.getX(), workPlacePoint.getY());
					if(space.getDistance(personPoint, otherPoint) < 1) {
						mode = MoveMode.STAY;
						atWork = true;
						atHome = false;
						timeAtWork = currentTick;
						timeAtHome = 0;
					}
				}
			}
			
			// unemployed control
			if(this.workPlace == null) {
					NdPoint randomPoint = Movement.generateRandomPoint();
					if(randomPoint != null) {
						otherPoint = randomPoint;
//						mode = MoveMode.IN_MOVE;
					}
			}
		}
		
		// movement
		if(otherPoint != null) {
			double angle = SpatialMath.calcAngleFor2DMovement(space, personPoint, otherPoint);
			space.moveByVector(this, 2, angle, 0);
			personPoint = space.getLocation(this);
			grid.moveTo(this, (int)personPoint.getX(), (int)personPoint.getY());
			
			if(space.getDistance(personPoint, otherPoint) < 1 && mode == MoveMode.TO_HOSPITAL) {
				atHospital = true;
				timeAtHospital = currentTick;
			}
		}

	}
	
	public void setWorkPlace(WorkPlace workplace) {
		this.workPlace = workplace; 
	}
	
	public WorkPlace getWorkPlace() {
		return this.workPlace;
	}

	public boolean isImmune() {
		return immune;
	}

	public void setImmune(boolean immune) {
		this.immune = immune;
	}

	public boolean isAtWork() {
		return atWork;
	}

	public void setAtWork(boolean atWork) {
		this.atWork = atWork;
	}

	public double getTimeAtWork() {
		return timeAtWork;
	}

	public void setTimeAtWork(double timeAtWork) {
		this.timeAtWork = timeAtWork;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public MoveMode getMode() {
		return mode;
	}

	public void setMode(MoveMode mode) {
		this.mode = mode;
	}

	public double getTimeAtHome() {
		return timeAtHome;
	}

	public void setTimeAtHome(double timeAtHome) {
		this.timeAtHome = timeAtHome;
	}

	public double getImmunity() {
		return immunity;
	}

	public void setImmunity(double immunity) {
		this.immunity = immunity;
	}

	public NdPoint getHomePosition() {
		return homePosition;
	}

	public void setHomePosition(NdPoint homePosition) {
		this.homePosition = homePosition;
	}

	public boolean isAtHome() {
		return atHome;
	}

	public void setAtHome(boolean atHome) {
		this.atHome = atHome;
	}
	
	
}
