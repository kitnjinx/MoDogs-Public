package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.BullTerrierRenderer;
import com.kitnjinx.modogs.entity.custom.MiniBullTerrierEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MiniBullTerrierModel extends AnimatedGeoModel<MiniBullTerrierEntity> {
    @Override
    public ResourceLocation getModelLocation(MiniBullTerrierEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/bull_terrier.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(MiniBullTerrierEntity object) {
        return BullTerrierRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(MiniBullTerrierEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/bull_terrier.animation.json");
    }
}