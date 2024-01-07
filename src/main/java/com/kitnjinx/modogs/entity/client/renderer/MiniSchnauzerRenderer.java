package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.MiniSchnauzerModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.collar.MiniSchnauzerCollarLayer;
import com.kitnjinx.modogs.entity.custom.MiniSchnauzerEntity;
import com.kitnjinx.modogs.entity.variant.MiniSchnauzerVariant;
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

        addRenderLayer(new MiniSchnauzerCollarLayer(this));

        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(MiniSchnauzerEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, MiniSchnauzerEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~12 inches
        if(animatable.isBaby()) {
            withScale(0.3f, 0.3f);
        } else {
            withScale(0.6f, 0.6f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(MiniSchnauzerEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
