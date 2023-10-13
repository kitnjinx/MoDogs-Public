package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.RussellTerrierRenderer;
import com.kitnjinx.modogs.entity.custom.RussellTerrierEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RussellTerrierModel extends AnimatedGeoModel<RussellTerrierEntity> {
    @Override
    public ResourceLocation getModelLocation(RussellTerrierEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/russell_terrier.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(RussellTerrierEntity object) {
        return RussellTerrierRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(RussellTerrierEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/russell_terrier.animation.json");
    }
}
