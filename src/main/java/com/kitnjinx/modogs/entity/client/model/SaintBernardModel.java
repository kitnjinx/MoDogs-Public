package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.SaintBernardRenderer;
import com.kitnjinx.modogs.entity.custom.SaintBernardEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SaintBernardModel extends AnimatedGeoModel<SaintBernardEntity> {
    @Override
    public ResourceLocation getModelLocation(SaintBernardEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/saint_bernard.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(SaintBernardEntity object) {
        return SaintBernardRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(SaintBernardEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/saint_bernard.animation.json");
    }
}
