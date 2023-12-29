package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.LabRetrieverRenderer;
import com.kitnjinx.modogs.entity.custom.LabRetrieverEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class LabRetrieverModel extends AnimatedGeoModel<LabRetrieverEntity> {
    @Override
    public ResourceLocation getModelResource(LabRetrieverEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/lab_retriever.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LabRetrieverEntity object) {
        return LabRetrieverRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(LabRetrieverEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/lab_retriever.animation.json");
    }
}
