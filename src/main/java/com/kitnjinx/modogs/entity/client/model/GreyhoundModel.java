package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.GreyhoundRenderer;
import com.kitnjinx.modogs.entity.custom.GreyhoundEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GreyhoundModel extends AnimatedGeoModel<GreyhoundEntity> {
    @Override
    public ResourceLocation getModelLocation(GreyhoundEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/greyhound.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GreyhoundEntity object) {
        return GreyhoundRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GreyhoundEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/greyhound.animation.json");
    }
}