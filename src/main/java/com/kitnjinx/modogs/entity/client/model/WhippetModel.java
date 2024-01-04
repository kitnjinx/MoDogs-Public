package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.WhippetRenderer;
import com.kitnjinx.modogs.entity.custom.WhippetEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WhippetModel extends GeoModel<WhippetEntity> {
    @Override
    public ResourceLocation getModelResource(WhippetEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/whippet.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WhippetEntity object) {
        return WhippetRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(WhippetEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/whippet.animation.json");
    }
}