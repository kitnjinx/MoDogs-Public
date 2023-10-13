package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.BloodhoundRenderer;
import com.kitnjinx.modogs.entity.custom.BloodhoundEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BloodhoundModel extends AnimatedGeoModel<BloodhoundEntity> {
    @Override
    public ResourceLocation getModelLocation(BloodhoundEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/bloodhound.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(BloodhoundEntity object) {
        return BloodhoundRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(BloodhoundEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/bloodhound.animation.json");
    }
}