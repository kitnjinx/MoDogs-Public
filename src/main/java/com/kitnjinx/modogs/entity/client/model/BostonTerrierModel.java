package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.BostonTerrierRenderer;
import com.kitnjinx.modogs.entity.custom.BostonTerrierEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BostonTerrierModel extends AnimatedGeoModel<BostonTerrierEntity> {
    @Override
    public ResourceLocation getModelLocation(BostonTerrierEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/boston_terrier.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(BostonTerrierEntity object) {
        return BostonTerrierRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(BostonTerrierEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/boston_terrier.animation.json");
    }
}