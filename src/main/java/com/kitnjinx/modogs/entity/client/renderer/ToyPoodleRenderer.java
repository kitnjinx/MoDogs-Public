package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.ToyPoodleModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.ToyPoodleCollarLayer;
import com.kitnjinx.modogs.entity.custom.ToyPoodleEntity;
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

public class ToyPoodleRenderer extends GeoEntityRenderer<ToyPoodleEntity> {

    public static final Map<PoodleVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(PoodleVariant.class), (var) -> {
                var.put(PoodleVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/poodle/poodle_black.png"));
                var.put(PoodleVariant.BROWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/poodle/toy_poodle_brown.png"));
                var.put(PoodleVariant.WHITE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/poodle/poodle_white.png"));
                var.put(PoodleVariant.SILVER,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/poodle/poodle_silver.png"));
                var.put(PoodleVariant.CREAM,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/poodle/poodle_cream.png"));
            });

    public ToyPoodleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ToyPoodleModel());

        addLayer(new ToyPoodleCollarLayer(this));

        this.shadowRadius = 0.25f;
    }

    @Override
    public ResourceLocation getTextureLocation(ToyPoodleEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(ToyPoodleEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~10 inches
        if(animatable.isBaby()) {
            stack.scale(0.25f, 0.25f, 0.25f);
        } else {
            stack.scale(0.5f, 0.5f, 0.5f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
