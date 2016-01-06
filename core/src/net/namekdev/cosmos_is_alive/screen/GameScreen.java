package net.namekdev.cosmos_is_alive.screen;

import net.mostlyoriginal.api.event.common.Subscribe;
import net.namekdev.entity_tracker.EntityTracker;
import net.namekdev.entity_tracker.network.EntityTrackerServer;
import net.namekdev.entity_tracker.ui.EntityTrackerMainWindow;
import net.namekdev.cosmos_is_alive.MyNGame;
import net.namekdev.cosmos_is_alive.component.GameState;
import net.namekdev.cosmos_is_alive.enums.C;
import net.namekdev.cosmos_is_alive.event.LostGameEvent;
import net.namekdev.cosmos_is_alive.event.WonLevelEvent;
import net.namekdev.cosmos_is_alive.factory.EntityFactory;
import net.namekdev.cosmos_is_alive.manager.AspectHelpers;
import net.namekdev.cosmos_is_alive.manager.WorldInitManager;
import net.namekdev.cosmos_is_alive.system.CameraSystem;
import net.namekdev.cosmos_is_alive.system.GameStateSystem;
import net.namekdev.cosmos_is_alive.system.InputSystem;
import net.namekdev.cosmos_is_alive.system.MovementSystem;
import net.namekdev.cosmos_is_alive.system.OrbitSystem;
import net.namekdev.cosmos_is_alive.system.PlayerStateSystem;
import net.namekdev.cosmos_is_alive.system.SchedulerSystem;
import net.namekdev.cosmos_is_alive.system.TweenSystem;
import net.namekdev.cosmos_is_alive.system.base.collision.CollisionDetectionSystem;
import net.namekdev.cosmos_is_alive.system.base.events.EventSystem;
import net.namekdev.cosmos_is_alive.system.base.render.RenderSystem;

import com.artemis.BaseSystem;
import com.artemis.SystemInvocationStrategy;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.managers.TagManager;
import com.artemis.utils.Bag;

public class GameScreen extends BaseScreen<GameScreen> {
	World world;
	public boolean isPaused;

	public GameScreen init(MyNGame game) {
		super.init(game);

//		EntityTrackerServer entityTrackerServer = new EntityTrackerServer();
//		entityTrackerServer.start();

		WorldConfiguration cfg = new WorldConfigurationBuilder()
//			.with(new EntityTracker(entityTrackerServer))
//			.with(new EntityTracker(new EntityTrackerMainWindow()))
			.with(new AspectHelpers())
			.with(new EntityFactory())
			.with(new WorldInitManager())
			.with(new TagManager())

			// loop systems
			.with(new InputSystem())
			.with(new TweenSystem())
			.with(new GameStateSystem())
			.with(new MovementSystem())
			.with(new PlayerStateSystem())
			.with(new OrbitSystem())
			.with(new CollisionDetectionSystem())

			.with(new CameraSystem())
			.with(new RenderSystem())
//			.with(new CollisionDebugSystem())
			.with(new SchedulerSystem())
			.with(new EventSystem())
			.build();

		cfg.setInvocationStrategy(new SystemInvocationStrategy() {
			@Override
			protected void process(Bag<BaseSystem> systems) {
				Object[] systemsData = systems.getData();
				for (int i = 0, s = systems.size(); s > i; i++) {
					BaseSystem system = (BaseSystem) systemsData[i];
					if (!isPaused || system instanceof RenderSystem) {
						system.process();
					}
					updateEntityStates();
				}
			}
		});

		cfg.register(camera);
		cfg.register(camera2d);
		cfg.register(sprites);
		cfg.register(decals);
		cfg.register(models);
		cfg.register(shapes);
		cfg.register(shaderProvider);
		cfg.register(assets);

		world = new World(cfg);
		world.getSystem(EventSystem.class).registerEvents(this);

		return this;
	}

	@Override
	public void render(float delta) {
		world.setDelta(!isPaused ? delta : 0);
		world.process();
	}

	@Subscribe
	private void onLostGame(LostGameEvent evt) {
		final GameState state = evt.state;

		game.pushScreen(new InstructionScreen(setFirstLevel).init(game));
	}

	@Subscribe
	private void onWonLevel(WonLevelEvent evt) {
		final GameState state = evt.state;
		int nextLevelIndex = state.levelIndex + 1;

		// Is there next level?
		if (nextLevelIndex < C.Levels.LevelCount) {
			setNextLevel.run();
		}

		// No more levels - You won whole game
		else {
//			game.pushScreen(new CongratsScreen(talk, showWinScreen).init(game));
		}

		// TODO
		throw new RuntimeException("you won the game!");
	}

	private Runnable setNextLevel = new Runnable() {
		public void run() {
			world.getSystem(GameStateSystem.class).setNextLevel();
		}
	};

	private Runnable setFirstLevel = new Runnable() {
		public void run() {
			world.getSystem(GameStateSystem.class).setFirstLevel();
			game.pushScreen(new InstructionScreen(null).init(game));
		}
	};

	private Runnable showWinScreen = new Runnable() {
		public void run() {
			game.pushScreen(new WonGameScreen(setFirstLevel).init(game));
		}
	};
}
