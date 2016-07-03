package testy;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;
import testy.basemodels.Hospital;
import testy.basemodels.WorkPlace;
import testy.utils.Config;

public class TestBuilder implements ContextBuilder<Object> {

	@Override
	public Context build(Context<Object> context) {
		context.setId("Testy");
		
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace(
				"space", 
				context, 
				new RandomCartesianAdder<Object>(), 
				new repast.simphony.space.continuous.WrapAroundBorders(), 
				50, 
				50
			);
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid <Object> grid = gridFactory.createGrid (
				"grid", 
				context,
				new GridBuilderParameters<Object>(
						new WrapAroundBorders(), 
						new SimpleGridAdder<Object>(), 
						true, 
						Config.X_SIZE, 
						Config.Y_SIZE
					)
			);
		
		for(int i = 0; i < Config.HOSPITAL_COUNT; i++) {
			Hospital hospital = new Hospital(grid, space);
			context.add(hospital);
		}
		
		List<WorkPlace> places = new ArrayList<WorkPlace>();
		for(int i = 0; i < Config.WORPLACE_COUNT; i++) {
			WorkPlace place = new WorkPlace(grid, space);
			context.add(place);
			places.add(place);
		}
		
		for(int i = 0; i < Config.HEATLY_COUNT; i++) {
			Healthy person = new Healthy(false, grid, space);
			person.setId(i + 1);
			int working = RandomHelper.nextIntFromTo(0, 1);
			if(working == 1) {
				int workPlace = RandomHelper.nextIntFromTo(0, Config.WORPLACE_COUNT - 1);
				person.setWorkPlace(places.get(workPlace));
			}
			context.add(person);
		}
		
		for(int i = 0; i < Config.INFECTED_COUNT; i++) {
			Infected person = new Infected(false, grid, space);
			person.setId(i + 1);
			int working = RandomHelper.nextIntFromTo(0, 1);
			// na potrzeby testów przypisujê choremu miejsce pracy
			working = 1;
			if(working == 1) {
				int workPlace = RandomHelper.nextIntFromTo(0, Config.WORPLACE_COUNT - 1);
				person.setWorkPlace(places.get(workPlace));
			}
			context.add(person);
		}
				
		
		for(Object obj : context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int)pt.getX(), (int)pt.getY());
		}
		
		return context;
	}

}
