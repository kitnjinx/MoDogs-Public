package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.SchnauzerRenderer;
import com.kitnjinx.modogs.entity.custom.SchnauzerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SchnauzerModel extends AnimatedGeoModel<SchnauzerEntity> {
    @Override
    public ResourceLocation getModelLocation(SchnauzerEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/schnauzer.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(SchnauzerEntity object) {
        return SchnauzerRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(SchnauzerEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/schnauzer.animation.json");
    }
}
