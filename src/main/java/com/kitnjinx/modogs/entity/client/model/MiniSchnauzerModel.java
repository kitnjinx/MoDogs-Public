package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.MiniSchnauzerRenderer;
import com.kitnjinx.modogs.entity.custom.MiniSchnauzerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MiniSchnauzerModel extends AnimatedGeoModel<MiniSchnauzerEntity> {
    @Override
    public ResourceLocation getModelLocation(MiniSchnauzerEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/schnauzer.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(MiniSchnauzerEntity object) {
        return MiniSchnauzerRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(MiniSchnauzerEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/schnauzer.animation.json");
    }
}
