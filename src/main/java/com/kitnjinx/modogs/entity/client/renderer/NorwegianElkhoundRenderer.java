package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.NorwegianElkhoundModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.NorwegianElkhoundCollarLayer;
import com.kitnjinx.modogs.entity.custom.NorwegianElkhoundEntity;
import com.kitnjinx.modogs.entity.variant.ShadeVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class NorwegianElkhoundRenderer extends GeoEntityRenderer<NorwegianElkhoundEntity> {

    public static final Map<ShadeVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(ShadeVariant.class), (var) -> {
                var.put(ShadeVariant.LIGHT,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/norwegian_elkhound/norwegian_elkhound_light.png"));
                var.put(ShadeVariant.MEDIUM,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/norwegian_elkhound/norwegian_elkhound_medium.png"));
                var.put(ShadeVariant.DARK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/norwegian_elkhound/norwegian_elkhound_dark.png"));
            });

    public NorwegianElkhoundRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new NorwegianElkhoundModel());

        addLayer(new NorwegianElkhoundCollarLayer(this));

        this.shadowRadius = 0.45f;
    }

    @Override
    public ResourceLocation getTextureLocation(NorwegianElkhoundEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(NorwegianElkhoundEntity animatable, float partialTicks, PoseStack stack,
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
