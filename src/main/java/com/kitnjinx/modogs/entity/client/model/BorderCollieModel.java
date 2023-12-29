package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.BorderCollieRenderer;
import com.kitnjinx.modogs.entity.custom.BorderCollieEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BorderCollieModel extends AnimatedGeoModel<BorderCollieEntity> {
    @Override
    public ResourceLocation getModelResource(BorderCollieEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/border_collie.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BorderCollieEntity object) {
        return BorderCollieRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(BorderCollieEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/border_collie.animation.json");
    }
}
