package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.TreeWalkHoundRenderer;
import com.kitnjinx.modogs.entity.custom.TreeWalkHoundEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TreeWalkHoundModel extends GeoModel<TreeWalkHoundEntity> {
    @Override
    public ResourceLocation getModelResource(TreeWalkHoundEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/tree_walk_hound.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TreeWalkHoundEntity object) {
        return TreeWalkHoundRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(TreeWalkHoundEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/tree_walk_hound.animation.json");
    }
}
