package com.kitnjinx.modogs.entity.client.renderer.layer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.custom.ShetlandSheepdogEntity;
import com.kitnjinx.modogs.entity.variant.ShetlandSheepdogVariant;
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

public class ShetlandSheepdogMerleLayer extends GeoRenderLayer<ShetlandSheepdogEntity> {
    public static final Map<ThreeMerleVariant, ResourceLocation> SABLE_MERLE_PATTERNS =
            Util.make(Maps.newEnumMap(ThreeMerleVariant.class), (pat) -> {
                pat.put(ThreeMerleVariant.PATTERN1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/merle/shetland_sheepdog_sable_merle1.png"));
                pat.put(ThreeMerleVariant.PATTERN2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/merle/shetland_sheepdog_sable_merle2.png"));
                pat.put(ThreeMerleVariant.PATTERN3,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/merle/shetland_sheepdog_sable_merle3.png"));
            });
    
    public static final Map<ThreeMerleVariant, ResourceLocation> BLUE_MERLE_PATTERNS =
            Util.make(Maps.newEnumMap(ThreeMerleVariant.class), (pat) -> {
                pat.put(ThreeMerleVariant.PATTERN1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/merle/shetland_sheepdog_blue_merle1.png"));
                pat.put(ThreeMerleVariant.PATTERN2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/merle/shetland_sheepdog_blue_merle2.png"));
                pat.put(ThreeMerleVariant.PATTERN3,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/merle/shetland_sheepdog_blue_merle3.png"));
            });

    public static final Map<ThreeMerleVariant, ResourceLocation> BLACK_TAN_MERLE_PATTERNS =
            Util.make(Maps.newEnumMap(ThreeMerleVariant.class), (pat) -> {
                pat.put(ThreeMerleVariant.PATTERN1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/merle/shetland_sheepdog_black_tan_merle1.png"));
                pat.put(ThreeMerleVariant.PATTERN2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/merle/shetland_sheepdog_black_tan_merle2.png"));
                pat.put(ThreeMerleVariant.PATTERN3,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/merle/shetland_sheepdog_black_tan_merle3.png"));
            });

    public static final ResourceLocation BLACK_TAN_NO_MERLE = new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/shetland_sheepdog_black_tan.png");
    public static final ResourceLocation NO_PATTERN = new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/collar/collar_none.png");

    public ShetlandSheepdogMerleLayer(GeoRenderer<ShetlandSheepdogEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    public ResourceLocation getPatternLocation(ShetlandSheepdogEntity instance) {
        if (instance.getVariant() == ShetlandSheepdogVariant.BLACK_TAN) {
            if (instance.hasMerle()) {
                return BLACK_TAN_MERLE_PATTERNS.get(instance.getMerleVariant());
            } else {
                return BLACK_TAN_NO_MERLE;
            }
        } else if (!instance.hasMerle()) {
            return NO_PATTERN;
        } else if (instance.getVariant() == ShetlandSheepdogVariant.SABLE) {
            return SABLE_MERLE_PATTERNS.get(instance.getMerleVariant());
        } else {
            return BLUE_MERLE_PATTERNS.get(instance.getMerleVariant());
        }
    }

    public void render(PoseStack poseStack, ShetlandSheepdogEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType armorRenderType = RenderType.armorCutoutNoCull(getPatternLocation(animatable));

        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }
}
