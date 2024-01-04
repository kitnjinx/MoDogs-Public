package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.MiniPinscherModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.MiniPinscherCollarLayer;
import com.kitnjinx.modogs.entity.custom.MiniPinscherEntity;
import com.kitnjinx.modogs.entity.custom.MiniSchnauzerEntity;
import com.kitnjinx.modogs.entity.variant.MiniPinscherVariant;
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

public class MiniPinscherRenderer extends GeoEntityRenderer<MiniPinscherEntity> {

    public static final Map<MiniPinscherVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(MiniPinscherVariant.class), (var) -> {
                var.put(MiniPinscherVariant.BLACK_TAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/mini_pinscher/mini_pinscher_black_tan.png"));
                var.put(MiniPinscherVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/mini_pinscher/mini_pinscher_red.png"));
                var.put(MiniPinscherVariant.CHOCOLATE_TAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/mini_pinscher/mini_pinscher_chocolate_tan.png"));
            });

    public MiniPinscherRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MiniPinscherModel());

        addRenderLayer(new MiniPinscherCollarLayer(this));

        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(MiniPinscherEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, MiniPinscherEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~12 inches
        if(animatable.isBaby()) {
            stack.scale(0.3f, 0.3f, 0.3f);
        } else {
            stack.scale(0.6f, 0.6f, 0.6f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(MiniPinscherEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
