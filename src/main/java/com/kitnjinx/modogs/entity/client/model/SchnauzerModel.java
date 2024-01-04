package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.SchnauzerRenderer;
import com.kitnjinx.modogs.entity.custom.SchnauzerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SchnauzerModel extends GeoModel<SchnauzerEntity> {
    @Override
    public ResourceLocation getModelResource(SchnauzerEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/schnauzer.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SchnauzerEntity object) {
        return SchnauzerRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(SchnauzerEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/schnauzer.animation.json");
    }
}
