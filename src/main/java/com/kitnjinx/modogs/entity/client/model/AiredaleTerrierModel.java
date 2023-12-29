package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.AiredaleTerrierRenderer;
import com.kitnjinx.modogs.entity.custom.AiredaleTerrierEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class AiredaleTerrierModel extends AnimatedGeoModel<AiredaleTerrierEntity> {
    @Override
    public ResourceLocation getModelResource(AiredaleTerrierEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/airedale_terrier.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AiredaleTerrierEntity object) {
        return AiredaleTerrierRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(AiredaleTerrierEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/airedale_terrier.animation.json");
    }
}
