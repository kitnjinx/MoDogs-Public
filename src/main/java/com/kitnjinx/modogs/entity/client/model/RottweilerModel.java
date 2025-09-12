package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.RottweilerRenderer;
import com.kitnjinx.modogs.entity.custom.RottweilerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RottweilerModel extends GeoModel<RottweilerEntity> {
    @Override
    public ResourceLocation getModelResource(RottweilerEntity object) {
        return ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "geo/rottweiler.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RottweilerEntity object) {
        return RottweilerRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(RottweilerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "animations/rottweiler.animation.json");
    }
}
