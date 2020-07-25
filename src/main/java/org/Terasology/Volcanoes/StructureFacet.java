// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.Terasology.Volcanoes;

import org.terasology.math.Region3i;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.facets.base.SparseObjectFacet3D;

public class StructureFacet extends SparseObjectFacet3D<Structure> {

    public StructureFacet(Region3i targetRegion, Border3D border) {
        super(targetRegion, border);
    }
}
