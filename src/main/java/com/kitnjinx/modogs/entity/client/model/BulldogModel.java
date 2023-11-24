package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.BulldogRenderer;
import com.kitnjinx.modogs.entity.custom.BulldogEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BulldogModel extends AnimatedGeoModel<BulldogEntity> {
    @Override
    public ResourceLocation getModelLocation(BulldogEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/bulldog.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(BulldogEntity object) {
        return BulldogRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(BulldogEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/bulldog.animation.json");
    }
}
