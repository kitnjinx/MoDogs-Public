package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.HuskyRenderer;
import com.kitnjinx.modogs.entity.custom.HuskyEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HuskyModel extends AnimatedGeoModel<HuskyEntity> {
    @Override
    public ResourceLocation getModelLocation(HuskyEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/husky.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(HuskyEntity object) {
        return HuskyRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(HuskyEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/husky.animation.json");
    }
}