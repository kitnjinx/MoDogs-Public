package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.DachshundRenderer;
import com.kitnjinx.modogs.entity.custom.DachshundEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DachshundModel extends AnimatedGeoModel<DachshundEntity> {
    @Override
    public ResourceLocation getModelResource(DachshundEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/dachshund.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DachshundEntity object) {
        return DachshundRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(DachshundEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/dachshund.animation.json");
    }
}
