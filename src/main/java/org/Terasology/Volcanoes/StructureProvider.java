/*
 Copyright 2020 The Terasology Foundation
 SPDX-License-Identifier: Apache-2.0
*/

package org.Terasology.Volcanoes;

import org.terasology.math.TeraMath;
import org.terasology.math.geom.Rect2i;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.WhiteNoise;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetProviderPlugin;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

@Produces(StructureFacet.class)
@Requires(@Facet(SurfaceHeightFacet.class))
public class StructureProvider implements FacetProviderPlugin {
    private Noise noise;

        @Override
        public void setSeed(long seed) {
            noise = new WhiteNoise(seed);
        }

        @Override
        public void process(GeneratingRegion region) {

            //Don't forget you sometimes have to extend the borders.
            //extendBy(top, bottom, sides) is the method used for this.
            //We'll cover this in the next section: Borders. :)

            Border3D border = region.getBorderForFacet(StructureFacet.class).extendBy(10, 20, 20);



            StructureFacet facet = new StructureFacet(region.getRegion(), border);
            SurfaceHeightFacet surfaceHeightFacet = region.getRegionFacet(SurfaceHeightFacet.class);

            Rect2i worldRegion = surfaceHeightFacet.getWorldRegion();

            for (int wz = worldRegion.minY(); wz <= worldRegion.maxY(); wz++) {
                for (int wx = worldRegion.minX(); wx <= worldRegion.maxX(); wx++) {
                    int surfaceHeight = TeraMath.floorToInt(surfaceHeightFacet.getWorld(wx, wz));

                    // check if height is within this region
                    if (surfaceHeight >= facet.getWorldRegion().minY() &&
                            surfaceHeight <= facet.getWorldRegion().maxY()) {

                        // TODO: check for overlap
                        if (noise.noise(wx, wz) > 0.99) {
                            facet.setWorld(wx, surfaceHeight, wz, new Structure());
                        }
                    }
                }
            }

            region.setRegionFacet(StructureFacet.class, facet);
        }
    }
