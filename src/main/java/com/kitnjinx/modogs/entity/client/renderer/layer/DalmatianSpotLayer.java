package com.kitnjinx.modogs.entity.client.renderer.layer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.custom.DalmatianEntity;
import com.kitnjinx.modogs.entity.variant.DalmatianVariant;
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

public class DalmatianSpotLayer extends GeoRenderLayer<DalmatianEntity> {
    public static final Map<DalmatianVariant, ResourceLocation> BLACK_SPOTS =
            Util.make(Maps.newEnumMap(DalmatianVariant.class), (pat) -> {
                pat.put(DalmatianVariant.PATTERN1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/dalmatian/dalmatian_black1.png"));
                pat.put(DalmatianVariant.PATTERN2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/dalmatian/dalmatian_black2.png"));
                pat.put(DalmatianVariant.PATTERN3,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/dalmatian/dalmatian_black3.png"));
                pat.put(DalmatianVariant.PATTERN4,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/dalmatian/dalmatian_black4.png"));
            });

    public static final Map<DalmatianVariant, ResourceLocation> BROWN_SPOTS =
            Util.make(Maps.newEnumMap(DalmatianVariant.class), (pat) -> {
                pat.put(DalmatianVariant.PATTERN1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/dalmatian/dalmatian_brown1.png"));
                pat.put(DalmatianVariant.PATTERN2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/dalmatian/dalmatian_brown2.png"));
                pat.put(DalmatianVariant.PATTERN3,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/dalmatian/dalmatian_brown3.png"));
                pat.put(DalmatianVariant.PATTERN4,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/dalmatian/dalmatian_brown4.png"));
            });

    public DalmatianSpotLayer(GeoRenderer<DalmatianEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    public ResourceLocation getSpotLocation(DalmatianEntity instance) {
        if (instance.isBrown()) {
            return BROWN_SPOTS.get(instance.getPatternVariant());
        } else {
            return BLACK_SPOTS.get(instance.getPatternVariant());
        }
    }

    public void render(PoseStack poseStack, DalmatianEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType armorRenderType = RenderType.armorCutoutNoCull(getSpotLocation(animatable));

        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }
}
