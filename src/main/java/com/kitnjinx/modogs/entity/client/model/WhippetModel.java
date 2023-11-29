package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.WhippetRenderer;
import com.kitnjinx.modogs.entity.custom.WhippetEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class WhippetModel extends AnimatedGeoModel<WhippetEntity> {
    @Override
    public ResourceLocation getModelLocation(WhippetEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/greyhound.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(WhippetEntity object) {
        return WhippetRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(WhippetEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/greyhound.animation.json");
    }
}