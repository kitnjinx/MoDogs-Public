package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.CockerSpanielRenderer;
import com.kitnjinx.modogs.entity.custom.CockerSpanielEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CockerSpanielModel extends GeoModel<CockerSpanielEntity> {
    @Override
    public ResourceLocation getModelResource(CockerSpanielEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/cocker_spaniel.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CockerSpanielEntity object) {
        return CockerSpanielRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(CockerSpanielEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/cocker_spaniel.animation.json");
    }
}