package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.GermanSpitzRenderer;
import com.kitnjinx.modogs.entity.custom.GermanSpitzEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GermanSpitzModel extends AnimatedGeoModel<GermanSpitzEntity> {
    @Override
    public ResourceLocation getModelResource(GermanSpitzEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/german_spitz.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GermanSpitzEntity object) {
        return GermanSpitzRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(GermanSpitzEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/german_spitz.animation.json");
    }
}
