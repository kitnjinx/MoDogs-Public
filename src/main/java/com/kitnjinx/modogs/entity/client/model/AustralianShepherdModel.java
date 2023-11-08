package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.AustralianShepherdRenderer;
import com.kitnjinx.modogs.entity.custom.AustralianShepherdEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class AustralianShepherdModel extends AnimatedGeoModel<AustralianShepherdEntity> {
    @Override
    public ResourceLocation getModelLocation(AustralianShepherdEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/australian_shepherd.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(AustralianShepherdEntity object) {
        return AustralianShepherdRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(AustralianShepherdEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/australian_shepherd.animation.json");
    }
}
