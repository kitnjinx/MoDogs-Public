package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.MudiRenderer;
import com.kitnjinx.modogs.entity.custom.MudiEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MudiModel extends AnimatedGeoModel<MudiEntity> {
    @Override
    public ResourceLocation getModelLocation(MudiEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/mudi.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(MudiEntity object) {
        return MudiRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(MudiEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/mudi.animation.json");
    }
}