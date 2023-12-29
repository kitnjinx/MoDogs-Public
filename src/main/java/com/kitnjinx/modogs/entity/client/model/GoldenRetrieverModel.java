package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.GoldenRetrieverRenderer;
import com.kitnjinx.modogs.entity.custom.GoldenRetrieverEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GoldenRetrieverModel extends AnimatedGeoModel<GoldenRetrieverEntity> {
    @Override
    public ResourceLocation getModelResource(GoldenRetrieverEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/golden_retriever.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GoldenRetrieverEntity object) {
        return GoldenRetrieverRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(GoldenRetrieverEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/golden_retriever.animation.json");
    }
}
