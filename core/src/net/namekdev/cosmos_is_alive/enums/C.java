package net.namekdev.cosmos_is_alive.enums;

public interface C {
	public interface Player {
		public static final float Width = 111.25f/100;
		public static final float Height = 62.25f/100;
	}
	
	public interface Camera {
		public static final float ShakeWidthMin = 4;
		public static final float ShakeWidthMax = 10;
		public static final float ShakeHeightMin = 1;
		public static final float ShakeHeightMax = 3;
	}
	
	public interface World {
		
	}
	
	public interface Levels {
		public static final float[] ProgressingSpeed = { 0.1f, 0.12f, 0.14f };
		public static final int[] GoalMoney = { 100, 120, 140 };
		public static final int LevelCount = GoalMoney.length;
	}
}
