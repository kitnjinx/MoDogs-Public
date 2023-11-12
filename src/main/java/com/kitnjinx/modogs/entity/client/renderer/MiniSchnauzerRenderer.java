package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.MiniSchnauzerModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.MiniSchnauzerCollarLayer;
import com.kitnjinx.modogs.entity.custom.MiniSchnauzerEntity;
import com.kitnjinx.modogs.entity.variant.MiniSchnauzerVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class MiniSchnauzerRenderer extends GeoEntityRenderer<MiniSchnauzerEntity> {

    public static final Map<MiniSchnauzerVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(MiniSchnauzerVariant.class), (var) -> {
                var.put(MiniSchnauzerVariant.PEPPER_SALT,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/schnauzer/schnauzer_pepper_salt.png"));
                var.put(MiniSchnauzerVariant.BLACK_SILVER,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/schnauzer/mini_schnauzer_black_silver.png"));
                var.put(MiniSchnauzerVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/schnauzer/schnauzer_black.png"));
            });

    public MiniSchnauzerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MiniSchnauzerModel());

        addLayer(new MiniSchnauzerCollarLayer(this));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(MiniSchnauzerEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(MiniSchnauzerEntity animatable, float partialTicks, PoseStack stack,
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
