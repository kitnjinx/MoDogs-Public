package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.CockerSpanielRenderer;
import com.kitnjinx.modogs.entity.custom.CockerSpanielEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CockerSpanielModel extends AnimatedGeoModel<CockerSpanielEntity> {
    @Override
    public ResourceLocation getModelLocation(CockerSpanielEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/cocker_spaniel.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(CockerSpanielEntity object) {
        return CockerSpanielRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(CockerSpanielEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/cocker_spaniel.animation.json");
    }
}