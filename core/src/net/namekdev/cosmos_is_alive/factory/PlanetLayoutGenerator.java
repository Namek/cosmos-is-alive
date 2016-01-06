package net.namekdev.cosmos_is_alive.factory;

import com.badlogic.gdx.math.MathUtils;

public class PlanetLayoutGenerator {
	private static final int WIDTH = 0;
	private static final int HEIGHT = 1;
	private static final int DEPTH = 2;

	int[][][] m = null;
	int total;


	public int[][][] generate(int width, int height, int depth, int desiredTotal) {
		final int[][][] ret = m = new int[width][height][depth];

		for (int ix = 0; ix < width; ++ix) {
			for (int iy = 0; iy < height; ++iy) {
				for (int iz = 0; iz < depth; ++iz) {
					m[ix][iy][iz] = 1;
				}
			}
		}

		int size = total = width*height*depth;

		for (int ix = 0; ix < width; ++ix) {
			for (int iy = 0; iy < height; ++iy) {
				int maxNth = depth;
				for (int iz = 0; iz < depth-1 && total > desiredTotal; ++iz, --maxNth) {
					removeNthDeep(MathUtils.random(maxNth-1)+1, ix, iy);
				}
			}
		}

		while (total > desiredTotal) {
			int randNth = MathUtils.random(total-1)+1;

			int set = 0;
			for (int ix = 0; ix < width; ++ix) {
				for (int iy = 0; iy < height; ++iy) {
					for (int iz = depth-1; iz >= 0; --iz) {
						if (m[ix][iy][iz] == 1) {
							++set;

							if (set == randNth) {
								remove(ix, iy, iz);
							}
						}
					}
				}
			}
		}


		m = null;
		return ret;
	}

	private void remove(int ix, int iy, int iz) {
		m[ix][iy][iz] = 0;
		total -= 1;
	}

	private void removeNthDeep(int nth, int ix, int iy) {
		int cur = 0, i = 0, found = 0;
		for (i = 0; cur < nth; ++i) {
			if (m[ix][iy][i] == 1) {
				++cur;
				found = i;
			}
		}

		remove(ix, iy, found);
	}

}
