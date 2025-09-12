package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.CardiganCorgiModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.collar.CardiganCorgiCollarLayer;
import com.kitnjinx.modogs.entity.custom.CardiganCorgiEntity;
import com.kitnjinx.modogs.entity.variant.CardiganCorgiVariant;
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

public class CardiganCorgiRenderer extends GeoEntityRenderer<CardiganCorgiEntity> {

    public static final Map<CardiganCorgiVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(CardiganCorgiVariant.class), (var) -> {
                var.put(CardiganCorgiVariant.RED,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/cardigan_corgi/cardigan_corgi_red.png"));
                var.put(CardiganCorgiVariant.BLACK,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/cardigan_corgi/cardigan_corgi_black.png"));
                var.put(CardiganCorgiVariant.SABLE,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/cardigan_corgi/cardigan_corgi_sable.png"));
                var.put(CardiganCorgiVariant.BLUE_MERLE,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/cardigan_corgi/cardigan_corgi_blue_merle.png"));
            });

    public CardiganCorgiRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CardiganCorgiModel());

        addRenderLayer(new CardiganCorgiCollarLayer(this));

        this.shadowRadius = 0.35f;
    }

    @Override
    public ResourceLocation getTextureLocation(CardiganCorgiEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack poseStack, CardiganCorgiEntity animatable, BakedGeoModel model, @org.jetbrains.annotations.Nullable MultiBufferSource bufferSource, @org.jetbrains.annotations.Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {

        // Height ~12 inches
        if(animatable.isBaby()) {
            withScale(0.4f, 0.4f);
        } else {
            withScale(0.8f, 0.8f);
        }

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public RenderType getRenderType(CardiganCorgiEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
