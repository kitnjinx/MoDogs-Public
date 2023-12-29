package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.IrishSetterRenderer;
import com.kitnjinx.modogs.entity.custom.IrishSetterEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class IrishSetterModel extends AnimatedGeoModel<IrishSetterEntity> {
    @Override
    public ResourceLocation getModelResource(IrishSetterEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/irish_setter.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(IrishSetterEntity object) {
        return IrishSetterRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(IrishSetterEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/irish_setter.animation.json");
    }
}
