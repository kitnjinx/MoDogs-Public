package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.GreatDaneModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.GreatDaneCollarLayer;
import com.kitnjinx.modogs.entity.custom.GreatDaneEntity;
import com.kitnjinx.modogs.entity.variant.GreatDaneVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class GreatDaneRenderer extends GeoEntityRenderer<GreatDaneEntity> {

    public static final Map<GreatDaneVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(GreatDaneVariant.class), (var) -> {
                var.put(GreatDaneVariant.FAWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/great_dane/great_dane_fawn.png"));
                var.put(GreatDaneVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/great_dane/great_dane_black.png"));
                var.put(GreatDaneVariant.BLUE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/great_dane/great_dane_blue.png"));
            });

    public GreatDaneRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GreatDaneModel());

        addLayer(new GreatDaneCollarLayer(this));

        this.shadowRadius = 0.65f;
    }

    @Override
    public ResourceLocation getTextureLocation(GreatDaneEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(GreatDaneEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~30 in
        if(animatable.isBaby()) {
            stack.scale(0.7f, 0.7f, 0.7f);
        } else {
            stack.scale(1.4f, 1.4f, 1.4f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
