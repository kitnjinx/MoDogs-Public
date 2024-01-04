package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.HuskyModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.HuskyCollarLayer;
import com.kitnjinx.modogs.entity.custom.AiredaleTerrierEntity;
import com.kitnjinx.modogs.entity.custom.HuskyEntity;
import com.kitnjinx.modogs.entity.variant.HuskyVariant;
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

public class HuskyRenderer extends GeoEntityRenderer<HuskyEntity> {

    public static final Map<HuskyVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(HuskyVariant.class), (var) -> {
                var.put(HuskyVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/husky_black.png"));
                var.put(HuskyVariant.GRAY,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/husky_gray.png"));
                var.put(HuskyVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/husky_red.png"));
                var.put(HuskyVariant.SABLE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/husky_sable.png"));
                var.put(HuskyVariant.WHITE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/husky_white.png"));
            });

    public HuskyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HuskyModel());

        addRenderLayer(new HuskyCollarLayer(this));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(HuskyEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, HuskyEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~22 inches
        if(animatable.isBaby()) {
            stack.scale(0.5f, 0.5f, 0.5f);
        } else {
            stack.scale(1f, 1f, 1f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(HuskyEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}