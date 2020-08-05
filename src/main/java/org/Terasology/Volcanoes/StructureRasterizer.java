// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.Terasology.Volcanoes;

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
import org.terasology.world.generation.WorldRasterizerPlugin;
import org.terasology.world.generator.plugin.RegisterPlugin;
import org.terasology.world.generator.plugin.WorldGeneratorPluginLibrary;

import java.util.Map;
import java.util.Objects;

@RegisterPlugin
public class StructureRasterizer implements WorldRasterizerPlugin {
    @In
    private WorldGeneratorPluginLibrary worldGeneratorPluginLibrary;

    Prefab structure;

    @Override
    public void initialize() {
        structure = Objects.requireNonNull(CoreRegistry.get(PrefabManager.class)).getPrefab("Volcanoes:dungeonBedroom");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        StructureFacet structureFacet = chunkRegion.getFacet(StructureFacet.class);

        SpawnBlockRegionsComponent spawnBlockRegionsComponent =
                structure.getComponent(SpawnBlockRegionsComponent.class);


        for (Map.Entry<BaseVector3i, Structure> entry : structureFacet.getWorldEntries().entrySet()) {
            Vector3i basePosition = new Vector3i(entry.getKey());

            for (SpawnBlockRegionsComponent.RegionToFill regionToFill : spawnBlockRegionsComponent.regionsToFill) {
                Block block = regionToFill.blockType;
//                if (isAir(block)) {
//                    continue;
//                }
                Region3i region = regionToFill.region;
                for (Vector3i pos : region) {
                    pos.add(basePosition);
                    if (chunkRegion.getRegion().encompasses(pos)) {
                        chunk.setBlock(ChunkMath.calcRelativeBlockPos(pos), block);
                    }
                }
            }
        }
    }

    private boolean isAir(final Block block) {
        return block.getURI().getBlockFamilyDefinitionUrn().equals(BlockManager.AIR_ID.getBlockFamilyDefinitionUrn());
    }
}
