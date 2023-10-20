package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.PitBullRenderer;
import com.kitnjinx.modogs.entity.custom.PitBullEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PitBullModel extends AnimatedGeoModel<PitBullEntity> {
    @Override
    public ResourceLocation getModelLocation(PitBullEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/pit_bull.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(PitBullEntity object) {
        return PitBullRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(PitBullEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/pit_bull.animation.json");
    }
}