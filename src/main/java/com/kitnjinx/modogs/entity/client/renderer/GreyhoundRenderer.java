package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.GreyhoundModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.GreyhoundCollarLayer;
import com.kitnjinx.modogs.entity.custom.GreyhoundEntity;
import com.kitnjinx.modogs.entity.variant.GreyhoundVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class GreyhoundRenderer extends GeoEntityRenderer<GreyhoundEntity> {

    public static final Map<GreyhoundVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(GreyhoundVariant.class), (var) -> {
                var.put(GreyhoundVariant.WHITE_BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/greyhound/greyhound_white_black.png"));
                var.put(GreyhoundVariant.WHITE_RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/greyhound/greyhound_white_red.png"));
                var.put(GreyhoundVariant.WHITE_BLUE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/greyhound/greyhound_white_blue.png"));
                var.put(GreyhoundVariant.WHITE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/greyhound/greyhound_white.png"));
                var.put(GreyhoundVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/greyhound/greyhound_black.png"));
                var.put(GreyhoundVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/greyhound/greyhound_red.png"));
                var.put(GreyhoundVariant.BLUE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/greyhound/greyhound_blue.png"));

            });

    public GreyhoundRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GreyhoundModel());

        addLayer(new GreyhoundCollarLayer(this));

        this.shadowRadius = 0.6f;
    }

    @Override
    public ResourceLocation getTextureLocation(GreyhoundEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(GreyhoundEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~28 in
        if(animatable.isBaby()) {
            stack.scale(0.65f, 0.65f, 0.65f);
        } else {
            stack.scale(1.3f, 1.3f, 1.3f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
