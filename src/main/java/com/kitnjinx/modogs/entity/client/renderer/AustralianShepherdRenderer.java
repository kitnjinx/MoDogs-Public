package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.AustralianShepherdModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.AustralianShepherdCollarLayer;
import com.kitnjinx.modogs.entity.custom.AustralianShepherdEntity;
import com.kitnjinx.modogs.entity.variant.AustralianShepherdVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

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

        addLayer(new AustralianShepherdCollarLayer(this));

        this.shadowRadius = 0.45f;
    }

    @Override
    public ResourceLocation getTextureLocation(AustralianShepherdEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(AustralianShepherdEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~20 in
        if(animatable.isBaby()) {
            stack.scale(0.4f, 0.4f, 0.4f);
        } else {
            stack.scale(0.9f, 0.9f, 0.9f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
