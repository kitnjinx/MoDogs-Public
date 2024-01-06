package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.BerneseMountainDogModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.BerneseMountainDogCollarLayer;
import com.kitnjinx.modogs.entity.custom.BeagleEntity;
import com.kitnjinx.modogs.entity.custom.BerneseMountainDogEntity;
import com.kitnjinx.modogs.entity.variant.BerneseMountainDogVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;
import java.util.Map;

public class BerneseMountainDogRenderer extends GeoEntityRenderer<BerneseMountainDogEntity> {

    public static final Map<BerneseMountainDogVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(BerneseMountainDogVariant.class), (var) -> {
                var.put(BerneseMountainDogVariant.RUST,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bernese_mountain_dog/bernese_mountain_dog_rust.png"));
                var.put(BerneseMountainDogVariant.TAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bernese_mountain_dog/bernese_mountain_dog_tan.png"));
            });

    public BerneseMountainDogRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BerneseMountainDogModel());

        addRenderLayer(new BerneseMountainDogCollarLayer(this));

        this.shadowRadius = 0.575f;
    }

    @Override
    public ResourceLocation getTextureLocation(BerneseMountainDogEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, BerneseMountainDogEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~26 in
        if(animatable.isBaby()) {
            withScale(0.6f, 0.6f);
        } else {
            withScale(1.2f, 1.2f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(BerneseMountainDogEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
