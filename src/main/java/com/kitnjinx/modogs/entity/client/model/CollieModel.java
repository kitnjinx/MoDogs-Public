package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.CollieRenderer;
import com.kitnjinx.modogs.entity.custom.CollieEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CollieModel extends AnimatedGeoModel<CollieEntity> {
    @Override
    public ResourceLocation getModelLocation(CollieEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/collie.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(CollieEntity object) {
        return CollieRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(CollieEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/collie.animation.json");
    }
}