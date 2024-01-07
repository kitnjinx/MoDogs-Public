package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.AustralianShepherdModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.collar.AustralianShepherdCollarLayer;
import com.kitnjinx.modogs.entity.custom.AustralianShepherdEntity;
import com.kitnjinx.modogs.entity.variant.AustralianShepherdVariant;
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

public class AustralianShepherdRenderer extends GeoEntityRenderer<AustralianShepherdEntity> {

    public static final Map<AustralianShepherdVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(AustralianShepherdVariant.class), (var) -> {
                var.put(AustralianShepherdVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/australian_shepherd/australian_shepherd_black.png"));
                var.put(AustralianShepherdVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/australian_shepherd/australian_shepherd_red.png"));
                var.put(AustralianShepherdVariant.BLUE_MERLE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/australian_shepherd/australian_shepherd_blue_merle.png"));
                var.put(AustralianShepherdVariant.RED_MERLE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/australian_shepherd/australian_shepherd_red_merle.png"));
            });

    public AustralianShepherdRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AustralianShepherdModel());

        addRenderLayer(new AustralianShepherdCollarLayer(this));

        this.shadowRadius = 0.45f;
    }

    @Override
    public ResourceLocation getTextureLocation(AustralianShepherdEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, AustralianShepherdEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
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
    public RenderType getRenderType(AustralianShepherdEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
