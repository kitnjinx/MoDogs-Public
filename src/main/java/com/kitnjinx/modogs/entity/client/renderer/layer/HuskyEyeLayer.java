package com.kitnjinx.modogs.entity.client.renderer.layer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.custom.HuskyEntity;
import com.kitnjinx.modogs.entity.variant.pattern_variation.HuskyEyeVariant;
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

public class HuskyEyeLayer extends GeoRenderLayer<HuskyEntity> {
    public static final Map<HuskyEyeVariant, ResourceLocation> LOCATION_BY_COLOR =
            Util.make(Maps.newEnumMap(HuskyEyeVariant.class), (col) -> {
                col.put(HuskyEyeVariant.BLUE1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/eyes/husky_blue_eyes1.png"));
                col.put(HuskyEyeVariant.BLUE2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/eyes/husky_blue_eyes2.png"));
                col.put(HuskyEyeVariant.BROWN1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/eyes/husky_brown_eyes1.png"));
                col.put(HuskyEyeVariant.BROWN2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/eyes/husky_brown_eyes2.png"));
                col.put(HuskyEyeVariant.HETERO1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/eyes/husky_hetero_eyes1.png"));
                col.put(HuskyEyeVariant.HETERO2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/eyes/husky_hetero_eyes2.png"));
                });

    public HuskyEyeLayer(GeoRenderer<HuskyEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    public ResourceLocation getCollarLocation(HuskyEntity instance) {
        return LOCATION_BY_COLOR.get(instance.getEyeVariant());
    }

    public void render(PoseStack poseStack, HuskyEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType armorRenderType = RenderType.armorCutoutNoCull(getCollarLocation(animatable));

        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }
}