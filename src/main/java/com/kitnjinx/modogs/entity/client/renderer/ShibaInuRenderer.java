package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.ShibaInuModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.collar.ShibaInuCollarLayer;
import com.kitnjinx.modogs.entity.custom.ShibaInuEntity;
import com.kitnjinx.modogs.entity.variant.ShibaInuVariant;
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

public class ShibaInuRenderer extends GeoEntityRenderer<ShibaInuEntity> {

    public static final Map<ShibaInuVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(ShibaInuVariant.class), (var) -> {
                var.put(ShibaInuVariant.RED,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/shiba_inu/shiba_inu_red.png"));
                var.put(ShibaInuVariant.BLACK_TAN,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/shiba_inu/shiba_inu_black_tan.png"));
                var.put(ShibaInuVariant.CREAM,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/shiba_inu/shiba_inu_cream.png"));
                var.put(ShibaInuVariant.DARK_CREAM,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/shiba_inu/shiba_inu_dark_cream.png"));
            });

    public ShibaInuRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ShibaInuModel());

        addRenderLayer(new ShibaInuCollarLayer(this));

        this.shadowRadius = 0.375f;
    }

    @Override
    public ResourceLocation getTextureLocation(ShibaInuEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack poseStack, ShibaInuEntity animatable, BakedGeoModel model, @org.jetbrains.annotations.Nullable MultiBufferSource bufferSource, @org.jetbrains.annotations.Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {

        // Height ~15 inches
        if(animatable.isBaby()) {
            withScale(0.375f, 0.375f);
        } else {
            withScale(0.75f, 0.75f);
        }

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public RenderType getRenderType(ShibaInuEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
