package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.WhippetModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.WhippetWhiteLayer;
import com.kitnjinx.modogs.entity.client.renderer.layer.collar.WhippetCollarLayer;
import com.kitnjinx.modogs.entity.custom.WhippetEntity;
import com.kitnjinx.modogs.entity.variant.WhippetVariant;
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

public class WhippetRenderer extends GeoEntityRenderer<WhippetEntity> {

    public static final Map<WhippetVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(WhippetVariant.class), (var) -> {
                var.put(WhippetVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/whippet/whippet_red.png"));
                var.put(WhippetVariant.BLUE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/whippet/whippet_blue.png"));
                var.put(WhippetVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/whippet/whippet_black.png"));
            });

    public WhippetRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new WhippetModel());

        addRenderLayer(new WhippetWhiteLayer(this));
        addRenderLayer(new WhippetCollarLayer(this));

        this.shadowRadius = 0.45f;
    }

    @Override
    public ResourceLocation getTextureLocation(WhippetEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, WhippetEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~20 in
        if(animatable.isBaby()) {
            withScale(0.4f, 0.4f);
        } else {
            withScale(0.9f, 0.9f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(WhippetEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
