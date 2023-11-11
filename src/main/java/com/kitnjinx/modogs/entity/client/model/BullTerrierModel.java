package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.BullTerrierRenderer;
import com.kitnjinx.modogs.entity.custom.BullTerrierEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BullTerrierModel extends AnimatedGeoModel<BullTerrierEntity> {
    @Override
    public ResourceLocation getModelLocation(BullTerrierEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/bull_terrier.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(BullTerrierEntity object) {
        return BullTerrierRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(BullTerrierEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/bull_terrier.animation.json");
    }
}