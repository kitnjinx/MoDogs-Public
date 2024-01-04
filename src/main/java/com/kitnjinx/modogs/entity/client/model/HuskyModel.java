package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.HuskyRenderer;
import com.kitnjinx.modogs.entity.custom.HuskyEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HuskyModel extends GeoModel<HuskyEntity> {
    @Override
    public ResourceLocation getModelResource(HuskyEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/husky.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HuskyEntity object) {
        return HuskyRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(HuskyEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/husky.animation.json");
    }
}