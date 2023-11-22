package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.TreeWalkHoundRenderer;
import com.kitnjinx.modogs.entity.custom.TreeWalkHoundEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class TreeWalkHoundModel extends AnimatedGeoModel<TreeWalkHoundEntity> {
    @Override
    public ResourceLocation getModelLocation(TreeWalkHoundEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/tree_walk_hound.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(TreeWalkHoundEntity object) {
        return TreeWalkHoundRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(TreeWalkHoundEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/tree_walk_hound.animation.json");
    }
}
