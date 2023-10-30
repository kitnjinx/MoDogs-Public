package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.ScottishTerrierRenderer;
import com.kitnjinx.modogs.entity.custom.ScottishTerrierEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ScottishTerrierModel extends AnimatedGeoModel<ScottishTerrierEntity> {
    @Override
    public ResourceLocation getModelLocation(ScottishTerrierEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/scottish_terrier.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ScottishTerrierEntity object) {
        return ScottishTerrierRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ScottishTerrierEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/scottish_terrier.animation.json");
    }
}
