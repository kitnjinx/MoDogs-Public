package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.BloodhoundRenderer;
import com.kitnjinx.modogs.entity.custom.BloodhoundEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BloodhoundModel extends GeoModel<BloodhoundEntity> {
    @Override
    public ResourceLocation getModelResource(BloodhoundEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/bloodhound.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BloodhoundEntity object) {
        return BloodhoundRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(BloodhoundEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/bloodhound.animation.json");
    }
}