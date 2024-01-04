package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.MiniSchnauzerRenderer;
import com.kitnjinx.modogs.entity.custom.MiniSchnauzerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MiniSchnauzerModel extends GeoModel<MiniSchnauzerEntity> {
    @Override
    public ResourceLocation getModelResource(MiniSchnauzerEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/schnauzer.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MiniSchnauzerEntity object) {
        return MiniSchnauzerRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(MiniSchnauzerEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/schnauzer.animation.json");
    }
}
