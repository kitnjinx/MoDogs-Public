package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.GreyhoundRenderer;
import com.kitnjinx.modogs.entity.custom.GreyhoundEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GreyhoundModel extends GeoModel<GreyhoundEntity> {
    @Override
    public ResourceLocation getModelResource(GreyhoundEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/greyhound.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GreyhoundEntity object) {
        return GreyhoundRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(GreyhoundEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/greyhound.animation.json");
    }
}