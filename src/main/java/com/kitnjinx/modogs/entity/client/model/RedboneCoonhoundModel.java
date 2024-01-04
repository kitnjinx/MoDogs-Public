package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.RedboneCoonhoundRenderer;
import com.kitnjinx.modogs.entity.custom.RedboneCoonhoundEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RedboneCoonhoundModel extends GeoModel<RedboneCoonhoundEntity> {
    @Override
    public ResourceLocation getModelResource(RedboneCoonhoundEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/redbone_coonhound.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RedboneCoonhoundEntity object) {
        return RedboneCoonhoundRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(RedboneCoonhoundEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/redbone_coonhound.animation.json");
    }
}
