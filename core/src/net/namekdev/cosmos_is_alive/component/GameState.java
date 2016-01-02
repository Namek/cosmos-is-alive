package net.namekdev.cosmos_is_alive.component;

import com.artemis.PooledComponent;

public class GameState extends PooledComponent {
	public float levelTimeProgress = 0;
	public int levelIndex = 0;
	public int collectedMoney = 0;
	public int goalMoney;


	@Override
	public void reset() {
		levelTimeProgress = 0;
		levelIndex = 0;
		collectedMoney = goalMoney = 0;
	}

	public boolean hasWon() {
		return collectedMoney >= goalMoney;
	}
}
