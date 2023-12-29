package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.ShetlandSheepdogRenderer;
import com.kitnjinx.modogs.entity.custom.ShetlandSheepdogEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ShetlandSheepdogModel extends AnimatedGeoModel<ShetlandSheepdogEntity> {
    @Override
    public ResourceLocation getModelResource(ShetlandSheepdogEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/shetland_sheepdog.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ShetlandSheepdogEntity object) {
        return ShetlandSheepdogRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(ShetlandSheepdogEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/shetland_sheepdog.animation.json");
    }
}