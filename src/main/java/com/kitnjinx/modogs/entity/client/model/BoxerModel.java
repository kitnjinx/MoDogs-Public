package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.BoxerRenderer;
import com.kitnjinx.modogs.entity.custom.BoxerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BoxerModel extends GeoModel<BoxerEntity> {
    @Override
    public ResourceLocation getModelResource(BoxerEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/boxer.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BoxerEntity object) {
        return BoxerRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(BoxerEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/boxer.animation.json");
    }
}