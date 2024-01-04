package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.ShibaInuModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.ShibaInuCollarLayer;
import com.kitnjinx.modogs.entity.custom.ShetlandSheepdogEntity;
import com.kitnjinx.modogs.entity.custom.ShibaInuEntity;
import com.kitnjinx.modogs.entity.variant.ShibaInuVariant;
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

public class ShibaInuRenderer extends GeoEntityRenderer<ShibaInuEntity> {

    public static final Map<ShibaInuVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(ShibaInuVariant.class), (var) -> {
                var.put(ShibaInuVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shiba_inu/shiba_inu_red.png"));
                var.put(ShibaInuVariant.BLACK_TAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shiba_inu/shiba_inu_black_tan.png"));
                var.put(ShibaInuVariant.CREAM,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shiba_inu/shiba_inu_cream.png"));
                var.put(ShibaInuVariant.DARK_CREAM,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shiba_inu/shiba_inu_dark_cream.png"));
            });

    public ShibaInuRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ShibaInuModel());

        addRenderLayer(new ShibaInuCollarLayer(this));

        this.shadowRadius = 0.375f;
    }

    @Override
    public ResourceLocation getTextureLocation(ShibaInuEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, ShibaInuEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~15 inches
        if(animatable.isBaby()) {
            stack.scale(0.375f, 0.375f, 0.375f);
        } else {
            stack.scale(0.75f, 0.75f, 0.75f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(ShibaInuEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
