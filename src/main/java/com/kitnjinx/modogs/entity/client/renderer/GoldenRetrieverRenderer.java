package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.GoldenRetrieverModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.GoldenRetrieverCollarLayer;
import com.kitnjinx.modogs.entity.custom.GoldenRetrieverEntity;
import com.kitnjinx.modogs.entity.variant.GoldenRetrieverVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class GoldenRetrieverRenderer extends GeoEntityRenderer<GoldenRetrieverEntity> {

    public static final Map<GoldenRetrieverVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(GoldenRetrieverVariant.class), (var) -> {
                var.put(GoldenRetrieverVariant.LIGHT,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/golden_retriever/golden_retriever_light.png"));
                var.put(GoldenRetrieverVariant.MEDIUM,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/golden_retriever/golden_retriever_medium.png"));
                var.put(GoldenRetrieverVariant.DARK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/golden_retriever/golden_retriever_dark.png"));
            });

    public GoldenRetrieverRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GoldenRetrieverModel());

        addLayer(new GoldenRetrieverCollarLayer(this));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(GoldenRetrieverEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(GoldenRetrieverEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~23 inches
        if(animatable.isBaby()) {
            stack.scale(0.525f, 0.525f, 0.525f);
        } else {
            stack.scale(1.05f, 1.05f, 1.05f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
