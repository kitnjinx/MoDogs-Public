package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.PoodleRenderer;
import com.kitnjinx.modogs.entity.custom.PoodleEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PoodleModel extends AnimatedGeoModel<PoodleEntity> {
    @Override
    public ResourceLocation getModelResource(PoodleEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/poodle.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PoodleEntity object) {
        return PoodleRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(PoodleEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/poodle.animation.json");
    }
}
