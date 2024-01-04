package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.ScottishTerrierRenderer;
import com.kitnjinx.modogs.entity.custom.ScottishTerrierEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ScottishTerrierModel extends GeoModel<ScottishTerrierEntity> {
    @Override
    public ResourceLocation getModelResource(ScottishTerrierEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/scottish_terrier.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ScottishTerrierEntity object) {
        return ScottishTerrierRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(ScottishTerrierEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/scottish_terrier.animation.json");
    }
}
