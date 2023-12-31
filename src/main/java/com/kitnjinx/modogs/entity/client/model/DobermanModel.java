package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.DobermanRenderer;
import com.kitnjinx.modogs.entity.custom.DobermanEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DobermanModel extends GeoModel<DobermanEntity> {
    @Override
    public ResourceLocation getModelResource(DobermanEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/doberman.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DobermanEntity object) {
        return DobermanRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(DobermanEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/doberman.animation.json");
    }
}