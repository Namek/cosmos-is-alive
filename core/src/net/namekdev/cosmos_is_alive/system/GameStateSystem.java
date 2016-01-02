package net.namekdev.cosmos_is_alive.system;

import net.mostlyoriginal.api.event.common.EventSystem;
import net.namekdev.cosmos_is_alive.component.GameState;
import net.namekdev.cosmos_is_alive.enums.C;
import net.namekdev.cosmos_is_alive.event.LostGameEvent;
import net.namekdev.cosmos_is_alive.event.WonLevelEvent;

import com.artemis.BaseSystem;
import com.artemis.EntityEdit;

public class GameStateSystem extends BaseSystem {
	private EventSystem events;
	public GameState gameState;

	
	@Override
	protected void initialize() {
		EntityEdit e = world.createEntity().edit();
		gameState = e.create(GameState.class);
		gameState.goalMoney = C.Levels.GoalMoney[0];
	}
	
	@Override
	protected void processSystem() {
		float dt = world.getDelta();
//		gameState.levelTimeProgress += dt * C.Levels.ProgressingSpeed[gameState.levelIndex];
		
		if (gameState.levelTimeProgress >= 1f) {
			gameState.levelTimeProgress = 1f;
			
			if (gameState.hasWon()) {
				events.dispatch(new WonLevelEvent(gameState));
			}
			else {
				events.dispatch(new LostGameEvent(gameState));
			}
		}
	}

	public void setNextLevel() {
		int level = ++gameState.levelIndex;
		gameState.collectedMoney = 0;
		gameState.goalMoney = C.Levels.GoalMoney[level];
		gameState.levelTimeProgress = 0;
	}
	
	public void setFirstLevel() {
		gameState.reset();
		gameState.goalMoney = C.Levels.GoalMoney[gameState.levelIndex];
	}
}
