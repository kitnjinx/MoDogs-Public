package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.PitBullModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.PitBullWhiteLayer;
import com.kitnjinx.modogs.entity.client.renderer.layer.collar.PitBullCollarLayer;
import com.kitnjinx.modogs.entity.custom.PitBullEntity;
import com.kitnjinx.modogs.entity.variant.PitBullVariant;
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

public class PitBullRenderer extends GeoEntityRenderer<PitBullEntity> {

    public static final Map<PitBullVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(PitBullVariant.class), (var) -> {
                var.put(PitBullVariant.BROWN,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/pit_bull/pit_bull_brown.png"));
                var.put(PitBullVariant.BLACK,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/pit_bull/pit_bull_black.png"));
                var.put(PitBullVariant.BLUE,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/pit_bull/pit_bull_blue.png"));
                var.put(PitBullVariant.WHITE,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/pit_bull/pit_bull_white.png"));
            });

    public PitBullRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PitBullModel());

        addRenderLayer(new PitBullWhiteLayer(this));
        addRenderLayer(new PitBullCollarLayer(this));

        this.shadowRadius = 0.45f;
    }

    @Override
    public ResourceLocation getTextureLocation(PitBullEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack poseStack, PitBullEntity animatable, BakedGeoModel model, @org.jetbrains.annotations.Nullable MultiBufferSource bufferSource, @org.jetbrains.annotations.Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {

        // Height ~20 in
        if(animatable.isBaby()) {
            withScale(0.4f, 0.4f);
        } else {
            withScale(0.9f, 0.9f);
        }

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public RenderType getRenderType(PitBullEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
