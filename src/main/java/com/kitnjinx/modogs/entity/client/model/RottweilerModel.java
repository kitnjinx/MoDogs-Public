package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.RottweilerRenderer;
import com.kitnjinx.modogs.entity.custom.RottweilerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RottweilerModel extends AnimatedGeoModel<RottweilerEntity> {
    @Override
    public ResourceLocation getModelLocation(RottweilerEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/rottweiler.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(RottweilerEntity object) {
        return RottweilerRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(RottweilerEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/rottweiler.animation.json");
    }
}
