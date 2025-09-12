package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.DachshundRenderer;
import com.kitnjinx.modogs.entity.custom.DachshundEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DachshundModel extends GeoModel<DachshundEntity> {
    @Override
    public ResourceLocation getModelResource(DachshundEntity object) {
        return ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "geo/dachshund.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DachshundEntity object) {
        return DachshundRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(DachshundEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "animations/dachshund.animation.json");
    }
}
