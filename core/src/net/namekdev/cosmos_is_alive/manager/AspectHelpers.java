package net.namekdev.cosmos_is_alive.manager;

import net.mostlyoriginal.api.system.core.PassiveSystem;
import net.namekdev.cosmos_is_alive.component.PosChild;
import net.namekdev.cosmos_is_alive.util.BagUtils;
import net.namekdev.cosmos_is_alive.util.BagUtils.IntBagPredicate;

import com.artemis.Aspect;
import com.artemis.AspectSubscriptionManager;
import com.artemis.EntitySubscription;
import com.artemis.Aspect.Builder;
import com.artemis.utils.IntBag;

public class AspectHelpers extends PassiveSystem {
	private AspectSubscriptionManager aspects;


	@Override
	protected void initialize() {
		aspects = world.getAspectSubscriptionManager();
	}
	
	public EntitySubscription getSubscription(Builder aspect) {
		return aspects.get(aspect);
	}

	public IntBag getEntities(Builder aspect) {
		return getSubscription(aspect).getEntities();
	}


	/*
	public IntBag getAllLeafs() {
		return getEntities(Aspect.all(LoveLeaf.class));
	}
	
	public IntBag getLeafs(final int parentTreeId) {
		IntBag leafs = getAllLeafs();

		return Utils.filterBag(leafs, new IntBagPredicate() {
			@Override
			public boolean apply(int e) {
				PosChild posChild = mPosChild.getSafe(e);
				return posChild != null && posChild.parent == parentTreeId;
			}
		});
	}
	*/
}
