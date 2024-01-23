package com.kitnjinx.modogs.entity.client.renderer.layer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.custom.BorderCollieEntity;
import com.kitnjinx.modogs.entity.variant.pattern_variation.ThreeMerleVariant;
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

public class BorderCollieMerleLayer extends GeoRenderLayer<BorderCollieEntity> {
    public static final Map<ThreeMerleVariant, ResourceLocation> BLACK_MERLE_PATTERNS =
            Util.make(Maps.newEnumMap(ThreeMerleVariant.class), (pat) -> {
                pat.put(ThreeMerleVariant.PATTERN1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/border_collie/merle/border_collie_black_merle1.png"));
                pat.put(ThreeMerleVariant.PATTERN2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/border_collie/merle/border_collie_black_merle2.png"));
                pat.put(ThreeMerleVariant.PATTERN3,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/border_collie/merle/border_collie_black_merle3.png"));
            });

    public static final Map<ThreeMerleVariant, ResourceLocation> RED_MERLE_PATTERNS =
            Util.make(Maps.newEnumMap(ThreeMerleVariant.class), (pat) -> {
                pat.put(ThreeMerleVariant.PATTERN1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/border_collie/merle/border_collie_red_merle1.png"));
                pat.put(ThreeMerleVariant.PATTERN2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/border_collie/merle/border_collie_red_merle2.png"));
                pat.put(ThreeMerleVariant.PATTERN3,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/border_collie/merle/border_collie_red_merle3.png"));
            });

    public static final Map<ThreeMerleVariant, ResourceLocation> LILAC_MERLE_PATTERNS =
            Util.make(Maps.newEnumMap(ThreeMerleVariant.class), (pat) -> {
                pat.put(ThreeMerleVariant.PATTERN1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/border_collie/merle/border_collie_lilac_merle1.png"));
                pat.put(ThreeMerleVariant.PATTERN2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/border_collie/merle/border_collie_lilac_merle2.png"));
                pat.put(ThreeMerleVariant.PATTERN3,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/border_collie/merle/border_collie_lilac_merle3.png"));
            });

    public static final ResourceLocation NO_PATTERN = new ResourceLocation(MoDogs.MOD_ID, "textures/entity/collar/60x40/collar_none.png");

    public BorderCollieMerleLayer(GeoRenderer<BorderCollieEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    public ResourceLocation getPatternLocation(BorderCollieEntity instance) {
        if (instance.isMerle()) {
            if (instance.getBaseColor() == 2) {
                return LILAC_MERLE_PATTERNS.get(instance.getMerleVariant());
            } else if (instance.getBaseColor() == 1) {
                return RED_MERLE_PATTERNS.get(instance.getMerleVariant());
            } else {
                return BLACK_MERLE_PATTERNS.get(instance.getMerleVariant());
            }
        } else {
            return NO_PATTERN;
        }
    }

    public void render(PoseStack poseStack, BorderCollieEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType armorRenderType = RenderType.armorCutoutNoCull(getPatternLocation(animatable));

        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }
}
