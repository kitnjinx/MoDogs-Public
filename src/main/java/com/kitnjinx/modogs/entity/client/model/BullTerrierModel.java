package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.BullTerrierRenderer;
import com.kitnjinx.modogs.entity.custom.BullTerrierEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BullTerrierModel extends GeoModel<BullTerrierEntity> {
    @Override
    public ResourceLocation getModelResource(BullTerrierEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/bull_terrier.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BullTerrierEntity object) {
        return BullTerrierRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(BullTerrierEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/bull_terrier.animation.json");
    }
}