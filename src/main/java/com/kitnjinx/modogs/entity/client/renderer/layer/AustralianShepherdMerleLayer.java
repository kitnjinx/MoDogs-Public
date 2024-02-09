package com.kitnjinx.modogs.entity.client.renderer.layer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.custom.AustralianShepherdEntity;
import com.kitnjinx.modogs.entity.variant.pattern_variation.TwoMerleVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.Map;

public class AustralianShepherdMerleLayer extends GeoRenderLayer<AustralianShepherdEntity> {
    public static final Map<TwoMerleVariant, ResourceLocation> BLUE_MERLE_PATTERNS =
            Util.make(Maps.newEnumMap(TwoMerleVariant.class), (pat) -> {
                pat.put(TwoMerleVariant.PATTERN1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/australian_shepherd/merle/australian_shepherd_blue_merle1.png"));
                pat.put(TwoMerleVariant.PATTERN2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/australian_shepherd/merle/australian_shepherd_blue_merle2.png"));
            });

    public static final Map<TwoMerleVariant, ResourceLocation> RED_MERLE_PATTERNS =
            Util.make(Maps.newEnumMap(TwoMerleVariant.class), (pat) -> {
                pat.put(TwoMerleVariant.PATTERN1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/australian_shepherd/merle/australian_shepherd_red_merle1.png"));
                pat.put(TwoMerleVariant.PATTERN2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/australian_shepherd/merle/australian_shepherd_red_merle2.png"));
            });

    public static final ResourceLocation NO_PATTERN = new ResourceLocation(MoDogs.MOD_ID, "textures/entity/collar/60x40/collar_none.png");

    public AustralianShepherdMerleLayer(GeoRenderer<AustralianShepherdEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    public ResourceLocation getPatternLocation(AustralianShepherdEntity instance) {
        if (instance.isMerle()) {
            if (instance.isRed()) {
                return RED_MERLE_PATTERNS.get(instance.getMerleVariant());
            } else {
                return BLUE_MERLE_PATTERNS.get(instance.getMerleVariant());
            }
        } else {
            return NO_PATTERN;
        }
    }

    public void render(PoseStack poseStack, AustralianShepherdEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType armorRenderType = RenderType.armorCutoutNoCull(getPatternLocation(animatable));

        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }
}
