package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.PoodleRenderer;
import com.kitnjinx.modogs.entity.custom.ToyPoodleEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ToyPoodleModel extends AnimatedGeoModel<ToyPoodleEntity> {
    @Override
    public ResourceLocation getModelResource(ToyPoodleEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/poodle.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ToyPoodleEntity object) {
        return PoodleRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(ToyPoodleEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/poodle.animation.json");
    }
}
