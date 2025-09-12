package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.CockerSpanielModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.collar.CockerSpanielCollarLayer;
import com.kitnjinx.modogs.entity.custom.CockerSpanielEntity;
import com.kitnjinx.modogs.entity.variant.CockerSpanielVariant;
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

public class CockerSpanielRenderer extends GeoEntityRenderer<CockerSpanielEntity> {

    public static final Map<CockerSpanielVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(CockerSpanielVariant.class), (var) -> {
                var.put(CockerSpanielVariant.RED,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/cocker_spaniel/cocker_spaniel_red.png"));
                var.put(CockerSpanielVariant.BROWN,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/cocker_spaniel/cocker_spaniel_brown.png"));
                var.put(CockerSpanielVariant.BUFF,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/cocker_spaniel/cocker_spaniel_buff.png"));
                var.put(CockerSpanielVariant.BLACK,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/cocker_spaniel/cocker_spaniel_black.png"));
                var.put(CockerSpanielVariant.SILVER,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/cocker_spaniel/cocker_spaniel_silver.png"));
            });

    public CockerSpanielRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CockerSpanielModel());

        addRenderLayer(new CockerSpanielCollarLayer(this));

        this.shadowRadius = 0.35f;
    }

    @Override
    public ResourceLocation getTextureLocation(CockerSpanielEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack poseStack, CockerSpanielEntity animatable, BakedGeoModel model, @org.jetbrains.annotations.Nullable MultiBufferSource bufferSource, @org.jetbrains.annotations.Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {

        // Height ~14 in
        if(animatable.isBaby()) {
            withScale(0.35f, 0.35f);
        } else {
            withScale(0.7f, 0.7f);
        }

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public RenderType getRenderType(CockerSpanielEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
