package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.ShibaInuRenderer;
import com.kitnjinx.modogs.entity.custom.ShibaInuEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ShibaInuModel extends AnimatedGeoModel<ShibaInuEntity> {
    @Override
    public ResourceLocation getModelResource(ShibaInuEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/shiba_inu.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ShibaInuEntity object) {
        return ShibaInuRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(ShibaInuEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/shiba_inu.animation.json");
    }
}
