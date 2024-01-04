package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.CollieRenderer;
import com.kitnjinx.modogs.entity.custom.CollieEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CollieModel extends GeoModel<CollieEntity> {
    @Override
    public ResourceLocation getModelResource(CollieEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/collie.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CollieEntity object) {
        return CollieRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(CollieEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/collie.animation.json");
    }
}