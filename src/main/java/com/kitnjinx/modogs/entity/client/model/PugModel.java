package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.PugRenderer;
import com.kitnjinx.modogs.entity.custom.PugEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PugModel extends AnimatedGeoModel<PugEntity> {
    @Override
    public ResourceLocation getModelLocation(PugEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/pug.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(PugEntity object) {
        return PugRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(PugEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/pug.animation.json");
    }
}