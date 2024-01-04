package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.BulldogRenderer;
import com.kitnjinx.modogs.entity.custom.BulldogEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BulldogModel extends GeoModel<BulldogEntity> {
    @Override
    public ResourceLocation getModelResource(BulldogEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/bulldog.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BulldogEntity object) {
        return BulldogRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(BulldogEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/bulldog.animation.json");
    }
}
