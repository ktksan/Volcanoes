// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.Terasology.Volcanoes;

import com.google.common.collect.Maps;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.entitySystem.prefab.PrefabManager;
import org.terasology.math.ChunkMath;
import org.terasology.math.Region3i;
import org.terasology.math.geom.BaseVector3i;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;
import org.terasology.structureTemplates.components.SpawnBlockRegionsComponent;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizer;

import java.util.Map;

public class StructureRasterizer implements WorldRasterizer {
    @In
    PrefabManager prefabManager;

    @Override
    public void initialize() {

    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        StructureFacet structureFacet = chunkRegion.getFacet(StructureFacet.class);

            /*
            BlockRegionTransform transformation = event.getTransformation();

        Map<Vector3i, Block> blocksToPlace = Maps.newHashMap();

        for (RegionToFill regionToFill : spawnBlockRegionsComponent.regionsToFill) {
            Block block = regionToFill.blockType;
            if (entity.hasComponent(IgnoreAirBlocksComponent.class) && isAir(block)) {
                continue;
            }

            Region3i region = regionToFill.region;
            region = transformation.transformRegion(region);
            block = transformation.transformBlock(block);

            for (Vector3i pos : region) {
                blocksToPlace.put(pos, block);
            }
        }

        worldProvider.setBlocks(blocksToPlace);
             */
        Prefab structure = prefabManager.getPrefab("GooeysQuests:FireNIceRoom");
        SpawnBlockRegionsComponent spawnBlockRegionsComponent = structure.getComponent(SpawnBlockRegionsComponent.class);


        for (Map.Entry<BaseVector3i, Structure> entry : structureFacet.getWorldEntries().entrySet()) {
            // there should be a house here
            // create a couple 3d regions to help iterate through the cube shape, inside and out
            Vector3i centerHousePosition = new Vector3i(entry.getKey());
/*
            int extent = entry.getValue().getExtent();
            centerHousePosition.add(0, extent, 0);
            Region3i walls = Region3i.createFromCenterExtents(centerHousePosition, extent);
            Region3i inside = Region3i.createFromCenterExtents(centerHousePosition, extent - 1);

            // loop through each of the positions in the cube, ignoring the inside
            for (Vector3i newBlockPosition : walls) {
                if (chunkRegion.getRegion().encompasses(newBlockPosition)
                        && !inside.encompasses(newBlockPosition)) {
                    chunk.setBlock(ChunkMath.calcRelativeBlockPos(newBlockPosition), stone);
                }
            }

            */
            //................


//            Map<Vector3i, Block> blocksToPlace = Maps.newHashMap();

            for (SpawnBlockRegionsComponent.RegionToFill regionToFill : spawnBlockRegionsComponent.regionsToFill) {
                    Block block = regionToFill.blockType;
                    if (isAir(block)) {
                        continue;
                    }

                    Region3i region = regionToFill.region;
                    region.min().add(centerHousePosition);
               // region = transformation.transformRegion(region);
              //  block = transformation.transformBlock(block);
                for (Vector3i pos : region) {
                    //blocksToPlace.put(pos, block);
                    if (chunkRegion.getRegion().encompasses(pos)) {
                        chunk.setBlock(ChunkMath.calcRelativeBlockPos(pos), block);
                    }
                }
            }

            //worldProvider.setBlocks(blocksToPlace);
            //...........

        }
    }
    public Region3i transformRegion(Region3i region) {
        return Region3i.createBounded((region.min()), (region.max()));
    }
    private boolean isAir(final Block block) {
        return block.getURI().getBlockFamilyDefinitionUrn().equals(BlockManager.AIR_ID.getBlockFamilyDefinitionUrn());
    }
}
