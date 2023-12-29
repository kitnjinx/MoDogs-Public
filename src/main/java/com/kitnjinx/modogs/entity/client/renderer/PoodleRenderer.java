package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.PoodleModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.PoodleCollarLayer;
import com.kitnjinx.modogs.entity.custom.PoodleEntity;
import com.kitnjinx.modogs.entity.variant.PoodleVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class PoodleRenderer extends GeoEntityRenderer<PoodleEntity> {

    public static final Map<PoodleVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(PoodleVariant.class), (var) -> {
                var.put(PoodleVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/poodle/poodle_black.png"));
                var.put(PoodleVariant.BROWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/poodle/poodle_brown.png"));
                var.put(PoodleVariant.WHITE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/poodle/poodle_white.png"));
                var.put(PoodleVariant.SILVER,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/poodle/poodle_silver.png"));
                var.put(PoodleVariant.CREAM,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/poodle/poodle_cream.png"));
            });

    public PoodleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PoodleModel());

        addLayer(new PoodleCollarLayer(this));

        this.shadowRadius = 0.4f;
    }

    @Override
    public ResourceLocation getTextureLocation(PoodleEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(PoodleEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~18 in
        if(animatable.isBaby()) {
            stack.scale(0.4f, 0.4f, 0.4f);
        } else {
            stack.scale(0.8f, 0.8f, 0.8f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
