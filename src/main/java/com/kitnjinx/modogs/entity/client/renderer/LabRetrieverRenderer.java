package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.LabRetrieverModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.LabRetrieverCollarLayer;
import com.kitnjinx.modogs.entity.custom.LabRetrieverEntity;
import com.kitnjinx.modogs.entity.variant.LabRetrieverVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class LabRetrieverRenderer extends GeoEntityRenderer<LabRetrieverEntity> {

    public static final Map<LabRetrieverVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(LabRetrieverVariant.class), (var) -> {
                var.put(LabRetrieverVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/lab_retriever/lab_retriever_black.png"));
                var.put(LabRetrieverVariant.CHOCOLATE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/lab_retriever/lab_retriever_chocolate.png"));
                var.put(LabRetrieverVariant.YELLOW,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/lab_retriever/lab_retriever_yellow.png"));
            });

    public LabRetrieverRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LabRetrieverModel());

        addLayer(new LabRetrieverCollarLayer(this));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(LabRetrieverEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(LabRetrieverEntity animatable, float partialTicks, PoseStack stack,
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
