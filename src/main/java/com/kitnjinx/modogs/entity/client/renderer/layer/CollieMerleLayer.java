package com.kitnjinx.modogs.entity.client.renderer.layer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.custom.CollieEntity;
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

public class CollieMerleLayer extends GeoRenderLayer<CollieEntity> { 
    public static final Map<TwoMerleVariant, ResourceLocation> BLUE_TAN_MERLE_PATTERNS =
            Util.make(Maps.newEnumMap(TwoMerleVariant.class), (pat) -> {
                pat.put(TwoMerleVariant.PATTERN1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/collie/merle/collie_blue_tan_merle1.png"));
                pat.put(TwoMerleVariant.PATTERN2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/collie/merle/collie_blue_tan_merle2.png"));
            });

    public static final Map<TwoMerleVariant, ResourceLocation> SABLE_MERLE_PATTERNS =
            Util.make(Maps.newEnumMap(TwoMerleVariant.class), (pat) -> {
                pat.put(TwoMerleVariant.PATTERN1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/collie/merle/collie_sable_merle1.png"));
                pat.put(TwoMerleVariant.PATTERN2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/collie/merle/collie_sable_merle2.png"));
            });

    public static final ResourceLocation NO_PATTERN = new ResourceLocation(MoDogs.MOD_ID, 
            "textures/entity/collie/collar/collar_none.png");


    public CollieMerleLayer(GeoRenderer<CollieEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    public ResourceLocation getCollarLocation(CollieEntity instance) {
        if (!instance.isMerle()) {
            return NO_PATTERN;
        } else if (instance.isBlack()) {
            return BLUE_TAN_MERLE_PATTERNS.get(instance.getMerlePattern());
        } else {
            return SABLE_MERLE_PATTERNS.get(instance.getMerlePattern());
        }
    }

    public void render(PoseStack poseStack, CollieEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType armorRenderType = RenderType.armorCutoutNoCull(getCollarLocation(animatable));

        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }
}
