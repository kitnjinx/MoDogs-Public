package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.GreatDaneRenderer;
import com.kitnjinx.modogs.entity.custom.GreatDaneEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GreatDaneModel extends AnimatedGeoModel<GreatDaneEntity> {
    @Override
    public ResourceLocation getModelResource(GreatDaneEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/great_dane.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GreatDaneEntity object) {
        return GreatDaneRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(GreatDaneEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/great_dane.animation.json");
    }
}
