package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.BostonTerrierRenderer;
import com.kitnjinx.modogs.entity.custom.BostonTerrierEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BostonTerrierModel extends GeoModel<BostonTerrierEntity> {
    @Override
    public ResourceLocation getModelResource(BostonTerrierEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/boston_terrier.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BostonTerrierEntity object) {
        return BostonTerrierRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(BostonTerrierEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/boston_terrier.animation.json");
    }
}
