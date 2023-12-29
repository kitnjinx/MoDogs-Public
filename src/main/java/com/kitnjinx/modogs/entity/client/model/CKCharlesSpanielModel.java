package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.CKCharlesSpanielRenderer;
import com.kitnjinx.modogs.entity.custom.CKCharlesSpanielEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CKCharlesSpanielModel extends AnimatedGeoModel<CKCharlesSpanielEntity> {
    @Override
    public ResourceLocation getModelResource(CKCharlesSpanielEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/ck_charles_spaniel.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CKCharlesSpanielEntity object) {
        return CKCharlesSpanielRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(CKCharlesSpanielEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/ck_charles_spaniel.animation.json");
    }
}