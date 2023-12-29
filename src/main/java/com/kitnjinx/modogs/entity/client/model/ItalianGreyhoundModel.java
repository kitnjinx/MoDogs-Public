package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.ItalianGreyhoundRenderer;
import com.kitnjinx.modogs.entity.custom.ItalianGreyhoundEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ItalianGreyhoundModel extends AnimatedGeoModel<ItalianGreyhoundEntity> {
    @Override
    public ResourceLocation getModelResource(ItalianGreyhoundEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/italian_greyhound.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ItalianGreyhoundEntity object) {
        return ItalianGreyhoundRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(ItalianGreyhoundEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/italian_greyhound.animation.json");
    }
}