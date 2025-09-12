package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.SaintBernardModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.collar.SaintBernardBarrelLayer;
import com.kitnjinx.modogs.entity.client.renderer.layer.collar.SaintBernardCollarLayer;
import com.kitnjinx.modogs.entity.custom.SaintBernardEntity;
import com.kitnjinx.modogs.entity.variant.SaintBernardVariant;
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

public class SaintBernardRenderer extends GeoEntityRenderer<SaintBernardEntity> {

    public static final Map<SaintBernardVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(SaintBernardVariant.class), (var) -> {
                var.put(SaintBernardVariant.BROWN,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/saint_bernard/saint_bernard_brown.png"));
                var.put(SaintBernardVariant.RED,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/saint_bernard/saint_bernard_red.png"));
                var.put(SaintBernardVariant.ORANGE,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/saint_bernard/saint_bernard_orange.png"));
                var.put(SaintBernardVariant.YELLOW_BROWN,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/saint_bernard/saint_bernard_yellow_brown.png"));
                var.put(SaintBernardVariant.MAHOGANY,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/saint_bernard/saint_bernard_mahogany.png"));
            });

    public SaintBernardRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SaintBernardModel());

        addRenderLayer(new SaintBernardCollarLayer(this));
        addRenderLayer(new SaintBernardBarrelLayer(this));

        this.shadowRadius = 0.6f;
    }

    @Override
    public ResourceLocation getTextureLocation(SaintBernardEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack poseStack, SaintBernardEntity animatable, BakedGeoModel model, @org.jetbrains.annotations.Nullable MultiBufferSource bufferSource, @org.jetbrains.annotations.Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {

        // Height ~28 in
        if(animatable.isBaby()) {
            withScale(0.65f, 0.65f);
        } else {
            withScale(1.3f, 1.3f);
        }

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public RenderType getRenderType(SaintBernardEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
