/*
 Copyright 2020 The Terasology Foundation
 SPDX-License-Identifier: Apache-2.0
*/

package org.Terasology.Volcanoes;

import org.terasology.core.world.CoreBiome;
import org.terasology.core.world.generator.facets.BiomeFacet;
import org.terasology.math.TeraMath;
import org.terasology.math.geom.Rect2i;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.WhiteNoise;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetBorder;
import org.terasology.world.generation.FacetProviderPlugin;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.facets.SeaLevelFacet;
import org.terasology.world.generation.facets.SurfaceHeightFacet;
import org.terasology.world.generator.plugin.RegisterPlugin;

@RegisterPlugin
@Requires({
        @Facet(value = SurfaceHeightFacet.class, border = @FacetBorder(sides = 150)),
        @Facet(value = BiomeFacet.class, border = @FacetBorder(sides = 150)),
        @Facet(value = SeaLevelFacet.class, border = @FacetBorder(sides = 150))
})
@Produces(StructureFacet.class)
public class StructureProvider implements FacetProviderPlugin {
    private Noise noise;

    @Override
    public void setSeed(long seed) {
        noise = new WhiteNoise(seed + 3);
    }

    @Override
    public void process(GeneratingRegion region) {

        Border3D border = region.getBorderForFacet(StructureFacet.class).extendBy(0, 150, 150);

        StructureFacet facet = new StructureFacet(region.getRegion(), border);
        SurfaceHeightFacet surfaceHeightFacet = region.getRegionFacet(SurfaceHeightFacet.class);
        SeaLevelFacet seaLevelFacet = region.getRegionFacet(SeaLevelFacet.class);
        BiomeFacet biomeFacet = region.getRegionFacet(BiomeFacet.class);

        Rect2i worldRegion = surfaceHeightFacet.getWorldRegion();

        for (int wz = worldRegion.minY(); wz <= worldRegion.maxY(); wz++) {
            for (int wx = worldRegion.minX(); wx <= worldRegion.maxX(); wx++) {
                int surfaceHeight = TeraMath.floorToInt(surfaceHeightFacet.getWorld(wx, wz));
                int seaLevel = seaLevelFacet.getSeaLevel();

                // check if height is within this region
//                if (surfaceHeight >= facet.getWorldRegion().minY() &&
                if (surfaceHeight >= facet.getWorldRegion().minY() &&
                        surfaceHeight <= facet.getWorldRegion().maxY()) {

                    // TODO: check for overlap
                    if (noise.noise(wx, wz) > 0.999 && surfaceHeight >= seaLevel) {
                        if (CoreBiome.DESERT.getId().equals(biomeFacet.getWorld(wx,wz).getId()))
                        {
                            facet.setWorld(wx, surfaceHeight, wz, new Structure());
                        }
                    }
                }
            }
        }

        region.setRegionFacet(StructureFacet.class, facet);
    }
}
