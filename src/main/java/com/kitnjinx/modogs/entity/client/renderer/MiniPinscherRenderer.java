package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.MiniPinscherModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.MiniPinscherCollarLayer;
import com.kitnjinx.modogs.entity.custom.MiniPinscherEntity;
import com.kitnjinx.modogs.entity.variant.MiniPinscherVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

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

        addLayer(new MiniPinscherCollarLayer(this));

        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(MiniPinscherEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(MiniPinscherEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~12 inches
        if(animatable.isBaby()) {
            stack.scale(0.3f, 0.3f, 0.3f);
        } else {
            stack.scale(0.6f, 0.6f, 0.6f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
