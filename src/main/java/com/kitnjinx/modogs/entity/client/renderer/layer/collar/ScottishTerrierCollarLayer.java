package com.kitnjinx.modogs.entity.client.renderer.layer.collar;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.custom.ScottishTerrierEntity;
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

public class ScottishTerrierCollarLayer extends GeoRenderLayer<ScottishTerrierEntity> {
    public static final Map<CollarVariant, ResourceLocation> LOCATION_BY_COLOR =
            Util.make(Maps.newEnumMap(CollarVariant.class), (col) -> {
                col.put(CollarVariant.NONE,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/scottish_terrier/collar/collar_none.png"));
                col.put(CollarVariant.WHITE,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/scottish_terrier/collar/collar_white.png"));
                col.put(CollarVariant.LIGHT_GRAY,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/scottish_terrier/collar/collar_light_gray.png"));
                col.put(CollarVariant.GRAY,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/scottish_terrier/collar/collar_gray.png"));
                col.put(CollarVariant.BLACK,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/scottish_terrier/collar/collar_black.png"));
                col.put(CollarVariant.BROWN,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/scottish_terrier/collar/collar_brown.png"));
                col.put(CollarVariant.RED,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/scottish_terrier/collar/collar_red.png"));
                col.put(CollarVariant.ORANGE,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/scottish_terrier/collar/collar_orange.png"));
                col.put(CollarVariant.YELLOW,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/scottish_terrier/collar/collar_yellow.png"));
                col.put(CollarVariant.LIME,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/scottish_terrier/collar/collar_lime.png"));
                col.put(CollarVariant.GREEN,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/scottish_terrier/collar/collar_green.png"));
                col.put(CollarVariant.CYAN,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/scottish_terrier/collar/collar_cyan.png"));
                col.put(CollarVariant.LIGHT_BLUE,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/scottish_terrier/collar/collar_light_blue.png"));
                col.put(CollarVariant.BLUE,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/scottish_terrier/collar/collar_blue.png"));
                col.put(CollarVariant.PURPLE,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/scottish_terrier/collar/collar_purple.png"));
                col.put(CollarVariant.MAGENTA,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/scottish_terrier/collar/collar_magenta.png"));
                col.put(CollarVariant.PINK,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/scottish_terrier/collar/collar_pink.png"));
            });

    public ScottishTerrierCollarLayer(GeoRenderer<ScottishTerrierEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    public ResourceLocation getCollarLocation(ScottishTerrierEntity instance) {
        return LOCATION_BY_COLOR.get(instance.getCollar());
    }

    public void render(PoseStack poseStack, ScottishTerrierEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType armorRenderType = RenderType.armorCutoutNoCull(getCollarLocation(animatable));

        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }
}
