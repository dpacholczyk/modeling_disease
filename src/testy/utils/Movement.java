package testy.utils;

import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.NdPoint;

public class Movement {
	public static NdPoint generateRandomPoint() {
		int shouldMove = RandomHelper.nextIntFromTo(0, 1);
		if(shouldMove == 1) {
			double x = RandomHelper.nextDoubleFromTo(0, Config.X_SIZE);
			double y = RandomHelper.nextDoubleFromTo(0, Config.Y_SIZE);
			
			return new NdPoint(x, y);
		} else {
			return null;
		}
	}
}
