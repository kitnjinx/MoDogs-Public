package com.kitnjinx.modogs.entity.client.renderer.layer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.custom.HuskyEntity;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
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

public class HuskyCollarLayer extends GeoRenderLayer<HuskyEntity> {
    public static final Map<CollarVariant, ResourceLocation> LOCATION_BY_COLOR =
            Util.make(Maps.newEnumMap(CollarVariant.class), (col) -> {
                col.put(CollarVariant.NONE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/collar/collar_none.png"));
                col.put(CollarVariant.WHITE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/collar/collar_white.png"));
                col.put(CollarVariant.LIGHT_GRAY,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/collar/collar_light_gray.png"));
                col.put(CollarVariant.GRAY,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/collar/collar_gray.png"));
                col.put(CollarVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/collar/collar_black.png"));
                col.put(CollarVariant.BROWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/collar/collar_brown.png"));
                col.put(CollarVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/collar/collar_red.png"));
                col.put(CollarVariant.ORANGE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/collar/collar_orange.png"));
                col.put(CollarVariant.YELLOW,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/collar/collar_yellow.png"));
                col.put(CollarVariant.LIME,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/collar/collar_lime.png"));
                col.put(CollarVariant.GREEN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/collar/collar_green.png"));
                col.put(CollarVariant.CYAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/collar/collar_cyan.png"));
                col.put(CollarVariant.LIGHT_BLUE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/collar/collar_light_blue.png"));
                col.put(CollarVariant.BLUE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/collar/collar_blue.png"));
                col.put(CollarVariant.PURPLE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/collar/collar_purple.png"));
                col.put(CollarVariant.MAGENTA,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/collar/collar_magenta.png"));
                col.put(CollarVariant.PINK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/collar/collar_pink.png"));
            });

    private static final ResourceLocation MODEL = new ResourceLocation(MoDogs.MOD_ID, "geo/husky.geo.json");

    public HuskyCollarLayer(GeoRenderer<HuskyEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    public ResourceLocation getCollarLocation(HuskyEntity instance) {
        return LOCATION_BY_COLOR.get(instance.getCollar());
    }

    public void render(PoseStack poseStack, HuskyEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType armorRenderType = RenderType.armorCutoutNoCull(getCollarLocation(animatable));

        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }
}