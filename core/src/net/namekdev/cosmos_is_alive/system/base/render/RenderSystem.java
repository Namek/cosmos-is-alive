package net.namekdev.cosmos_is_alive.system.base.render;

import net.namekdev.cosmos_is_alive.component.render.CustomRenderer;
import net.namekdev.cosmos_is_alive.component.render.Layer;
import net.namekdev.cosmos_is_alive.component.render.Renderable;
import net.namekdev.cosmos_is_alive.component.render.RenderableChild;
import net.namekdev.cosmos_is_alive.component.render.ZOrder;
import net.namekdev.cosmos_is_alive.system.base.render.renderers.DecalRenderer;
import net.namekdev.cosmos_is_alive.system.base.render.renderers.IRenderer;
import net.namekdev.cosmos_is_alive.system.base.render.renderers.ModelRenderer;
import net.namekdev.cosmos_is_alive.system.base.render.renderers.SpriteRenderer;

import com.artemis.Aspect;
import com.artemis.AspectSubscriptionManager;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.EntitySubscription.SubscriptionListener;
import com.artemis.annotations.Wire;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.LongArray;

/**
 *
 *
 * @author Namek
 */
public class RenderSystem extends BaseEntitySystem {
	private ComponentMapper<Layer> mLayer;
	private ComponentMapper<CustomRenderer> mCustomRenderer;
	private ComponentMapper<Renderable> mRenderable;
	private ComponentMapper<RenderableChild> mRenderableChild;
	private ComponentMapper<ZOrder> mZOrder;
	
	private IRenderer[] renderers;
	private IRenderer lastRenderer; 

	private LongArray sorted = new LongArray(100);
	private int renderablesCount = 0;
	private int renderableChildsCount = 0;

	public boolean dirtyOrder = false;

	private static final int TYPE_BITS_COUNT = 64;

	/** long size minus 1 (most left bit is zero for sorting) */
	private static final int META_BITS_COUNT = TYPE_BITS_COUNT - 1;

	private static final int LAYER_BITS_COUNT = 10;

	/** 0 - parent, 1 - child */
	private static final int HIERARCHY_BITS_COUNT = 1;
	private static final int ZORDER_BITS_COUNT = 11;
	private static final int ENTITY_ID_BITS_COUNT
		= META_BITS_COUNT - LAYER_BITS_COUNT - HIERARCHY_BITS_COUNT - ZORDER_BITS_COUNT;

	private static final int EID_SHIFT = 0;
	private static final int ZORDER_SHIFT = EID_SHIFT + ENTITY_ID_BITS_COUNT;
	private static final int HIERARCHY_SHIFT = ZORDER_SHIFT + ZORDER_BITS_COUNT;
	private static final int LAYER_SHIFT = HIERARCHY_SHIFT + HIERARCHY_BITS_COUNT;

	public static final int NO_CHILDREN_SORT_ID = 0;
	public static final int PARENT_SORT_ID = 0;
	public static final int CHILD_SORT_ID = 1;


	public RenderSystem() {
		super(Aspect.all(Renderable.class));
	}

	@Override
	protected void initialize() {
		renderers = new IRenderer[3];
		renderers[Renderable.MODEL] = new ModelRenderer();
		renderers[Renderable.DECAL] = new DecalRenderer();
		renderers[Renderable.SPRITE] = new SpriteRenderer();
		
		for (IRenderer renderer : renderers) {
			world.inject(renderer);
			renderer.initialize();
		}
		
		AspectSubscriptionManager subscriptions = world.getAspectSubscriptionManager();
		EntitySubscription renderableSub = subscriptions.get(Aspect.all(Renderable.class));
		EntitySubscription renderableChildSub = subscriptions.get(Aspect.all(RenderableChild.class));
		EntitySubscription customRendererSub = subscriptions.get(Aspect.all(CustomRenderer.class));

		renderableSub.addSubscriptionListener(new SubscriptionListener() {
			@Override
			public void inserted(IntBag entities) {
				renderablesCount += entities.size();
				sorted.ensureCapacity(renderablesCount + renderableChildsCount);
				dirtyOrder = true;
			}
			@Override
			public void removed(IntBag entities) {
				renderablesCount -= entities.size();
				dirtyOrder = true;
			}
		});

		renderableChildSub.addSubscriptionListener(new SubscriptionListener() {
			@Override
			public void inserted(IntBag entities) {
				renderableChildsCount += entities.size();
				sorted.ensureCapacity(renderablesCount + renderableChildsCount);
				dirtyOrder = true;
			}
			@Override
			public void removed(IntBag entities) {
				renderableChildsCount -= entities.size();
				dirtyOrder = true;
			}
		});
		
		customRendererSub.addSubscriptionListener(new SubscriptionListener() {
			@Override
			public void inserted(IntBag entities) {
				final int n = entities.size();
				final int[] ids = entities.getData();
				
				for (int i = 0; i < n; ++i) {
					IRenderer renderer = mCustomRenderer.get(ids[i]).renderer;
					world.inject(renderer);
				}
			}
			@Override
			public void removed(IntBag entities) {
			}
		});
	}

	@Override
	protected void dispose() {
	}

	@Override
	protected void processSystem() {
		if (dirtyOrder) {
			refreshEntityOrder();
			dirtyOrder = false;
		}

		Gdx.gl.glClearColor(0, 0, 1, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		
		lastRenderer = null;

		final long[] metas = sorted.items;
		final int n = sorted.size;
		for (int i = 0; i < n; ++i) {
			final int e = (int)(metas[i] & ((1 << ENTITY_ID_BITS_COUNT) - 1));

			Renderable renderable = mRenderable.get(e);

			if (!renderable.visible) {
				return;
			}

			for (int j = 0; j < renderers.length; ++j) {
				final boolean hasRenderer = j == renderable.type;
				
				if (hasRenderer) {
					IRenderer renderer = renderers[j];
					render(e, renderer);
				}
			}

			CustomRenderer custom = mCustomRenderer.getSafe(e);
			if (custom != null) {
				IRenderer renderer = custom.renderer;
				render(e, renderer);
			}
		}
		
		if (lastRenderer != null) {
			lastRenderer.end();
		}
	}
	
	private void render(int entityId, IRenderer renderer) {
		if (lastRenderer == null) {
			renderer.begin();
			lastRenderer = renderer;
		}
		else if (lastRenderer.getBatch() != renderer.getBatch()) {
			lastRenderer.end();
			renderer.begin();
			lastRenderer = renderer;
		}
		
		renderer.draw(entityId);
	}

	private void refreshEntityOrder() {
		final IntBag entities = getEntityIds();
		final int[] data = entities.getData();

		sorted.clear();

		for (int i = 0, n = entities.size(); i < n; ++i) {
			final int e = data[i];
			Renderable renderable = mRenderable.get(e);

			if (!renderable.visible) {
				continue;
			}

			RenderableChild renderableChild = mRenderableChild.getSafe(e);
			Layer layer = mLayer.create(e);
			ZOrder zorder = mZOrder.getSafe(e);

			int parenting = NO_CHILDREN_SORT_ID;
			if (renderable.children != null) {
				parenting = PARENT_SORT_ID;
			}
			else if (renderableChild != null) {
				parenting = CHILD_SORT_ID;
			}

			long meta = (e << EID_SHIFT) | (parenting << HIERARCHY_SHIFT);
			if (zorder != null) {
				meta |= zorder.z << ZORDER_SHIFT;
			}
			meta |= layer.layer << LAYER_SHIFT;

			sorted.add(meta);
		}

		sorted.sort();
	}
}
