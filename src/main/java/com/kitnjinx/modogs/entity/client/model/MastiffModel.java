package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.MastiffRenderer;
import com.kitnjinx.modogs.entity.custom.MastiffEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MastiffModel extends AnimatedGeoModel<MastiffEntity> {
    @Override
    public ResourceLocation getModelResource(MastiffEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/mastiff.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MastiffEntity object) {
        return MastiffRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(MastiffEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/mastiff.animation.json");
    }
}
