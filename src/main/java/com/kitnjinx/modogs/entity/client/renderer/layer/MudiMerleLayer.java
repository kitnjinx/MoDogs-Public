package com.kitnjinx.modogs.entity.client.renderer.layer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.custom.MudiEntity;
import com.kitnjinx.modogs.entity.variant.MudiVariant;
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

public class MudiMerleLayer extends GeoRenderLayer<MudiEntity> {
    public static final Map<TwoMerleVariant, ResourceLocation> BLACK_MERLE_PATTERNS =
            Util.make(Maps.newEnumMap(TwoMerleVariant.class), (pat) -> {
                pat.put(TwoMerleVariant.PATTERN1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/mudi/merle/mudi_black_merle1.png"));
                pat.put(TwoMerleVariant.PATTERN2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/mudi/merle/mudi_black_merle2.png"));
            });

    public static final Map<TwoMerleVariant, ResourceLocation> BROWN_MERLE_PATTERNS =
            Util.make(Maps.newEnumMap(TwoMerleVariant.class), (pat) -> {
                pat.put(TwoMerleVariant.PATTERN1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/mudi/merle/mudi_brown_merle1.png"));
                pat.put(TwoMerleVariant.PATTERN2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/mudi/merle/mudi_brown_merle2.png"));
            });

    public static final ResourceLocation NO_PATTERN = new ResourceLocation(MoDogs.MOD_ID,
            "textures/entity/collar/53x38/collar_none.png");

    public MudiMerleLayer(GeoRenderer<MudiEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    public ResourceLocation getCollarLocation(MudiEntity instance) {
        if (instance.getVariant() == MudiVariant.WHITE || !instance.hasMerle()) {
            return NO_PATTERN;
        } else if (instance.isBrown()) {
            return BROWN_MERLE_PATTERNS.get(instance.getMerlePattern());
        } else {
            return BLACK_MERLE_PATTERNS.get(instance.getMerlePattern());
        }
    }

    public void render(PoseStack poseStack, MudiEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType armorRenderType = RenderType.armorCutoutNoCull(getCollarLocation(animatable));

        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }
}