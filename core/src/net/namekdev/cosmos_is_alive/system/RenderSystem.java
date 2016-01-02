package net.namekdev.cosmos_is_alive.system;

import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;
import net.namekdev.cosmos_is_alive.component.Colored;
import net.namekdev.cosmos_is_alive.component.Origin;
import net.namekdev.cosmos_is_alive.component.Pos;
import net.namekdev.cosmos_is_alive.component.PosChild;
import net.namekdev.cosmos_is_alive.component.Rotation;
import net.namekdev.cosmos_is_alive.component.Scale;
import net.namekdev.cosmos_is_alive.component.render.Layer;
import net.namekdev.cosmos_is_alive.component.render.Renderable;
import net.namekdev.cosmos_is_alive.component.render.RenderableChild;
import net.namekdev.cosmos_is_alive.component.render.ZOrder;

import com.artemis.Aspect;
import com.artemis.AspectSubscriptionManager;
import com.artemis.BaseEntitySystem;
import com.artemis.EntitySubscription;
import com.artemis.EntitySubscription.SubscriptionListener;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.LongArray;

/**
 *
 *
 * @author Namek
 */
public class RenderSystem extends BaseEntitySystem {
	private M<Colored> mColored;
	private M<Layer> mLayer;
	private M<Origin> mOrigin;
	private M<Pos> mPos;
	private M<PosChild> mPosChild;
	private M<Renderable> mRenderable;
	private M<RenderableChild> mRenderableChild;
	private M<Rotation> mRotation;
	private M<Scale> mScale;
	private M<ZOrder> mZOrder;

	private CameraSystem cameraSystem;

	public SpriteBatch sprites;
	public ShapeRenderer shapes;

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
		AspectSubscriptionManager subscriptions = world.getAspectSubscriptionManager();
		EntitySubscription renderableSub = subscriptions.get(Aspect.all(Renderable.class));
		EntitySubscription renderableChildSub = subscriptions.get(Aspect.all(RenderableChild.class));

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

		sprites = new SpriteBatch();
		shapes = new ShapeRenderer();
	}

	@Override
	protected void dispose() {
		sprites.dispose();
		shapes.dispose();
	}

	@Override
	protected void processSystem() {
		if (dirtyOrder) {
			refresh();
			dirtyOrder = false;
		}

		sprites.setProjectionMatrix(cameraSystem.camera.combined);

		Gdx.gl.glClearColor(0, 0, 1, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		sprites.begin();
		shapes.begin(ShapeType.Line);

		final long[] metas = sorted.items;
		for (int i = 0, n = sorted.size; i < n; ++i) {
			final int e = (int)(metas[i] & ((1 << ENTITY_ID_BITS_COUNT) - 1));

			Renderable renderable = mRenderable.get(e);

			if (!renderable.visible) {
				return;
			}

			Pos pos = mPos.get(e);
			Scale scale = mScale.getSafe(e);
			Origin origin = mOrigin.getSafe(e);
			Rotation rot = mRotation.getSafe(e);

			float scaleX = scale != null ? scale.x : 1f;
			float scaleY = scale != null ? scale.y : 1f;
			float originX = origin != null ? origin.x : Origin.DEFAULT_X;
			float originY = origin != null ? origin.y : Origin.DEFAULT_Y;
			float rotation = rot != null ? rot.rotation : 0;

			if ((renderable.type & Renderable.SPRITE) != 0) {
				TextureRegion img = renderable.sprite;
				float w = img.getRegionWidth();
				float h = img.getRegionHeight();

				float x = pos.x, y = pos.y;
				if (mPosChild.has(e)) {
					Pos parentPos = mPos.get(world.getEntity(mPosChild.get(e).parent));
					x += parentPos.x;
					y += parentPos.y;
				}

				if (mColored.has(e)) {
					Colored col = mColored.get(e);
					sprites.setColor(col.color);
				}
				else {
					sprites.setColor(Color.WHITE);
				}

				float ox = originX*w;
				float oy = originY*h;

				sprites.draw(img, x - ox, y - oy, ox, oy, w, h, scaleX, scaleY, rotation);

//				if (renderable.debugFrame && mCollider.has(e)) {
//					collisions.getRect(e, tmpRect);
//					shapes.rect(tmpRect.x, tmpRect.y, tmpRect.width, tmpRect.height);
//					renderable.debugFrame = false;
//				}
			}

			if ((renderable.type & Renderable.FRAME_ANIM) != 0) {
				// TODO
			}

			if ((renderable.type & Renderable.CUSTOM_RENDERER) != 0) {
				 renderable.renderer.render(e, sprites, shapes);
			}
		}

		sprites.end();
		shapes.end();
	}

	private void refresh() {
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
