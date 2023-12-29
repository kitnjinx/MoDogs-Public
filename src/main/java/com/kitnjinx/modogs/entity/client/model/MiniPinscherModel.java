package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.MiniPinscherRenderer;
import com.kitnjinx.modogs.entity.custom.MiniPinscherEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MiniPinscherModel extends AnimatedGeoModel<MiniPinscherEntity> {
    @Override
    public ResourceLocation getModelResource(MiniPinscherEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/mini_pinscher.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MiniPinscherEntity object) {
        return MiniPinscherRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(MiniPinscherEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/mini_pinscher.animation.json");
    }
}
