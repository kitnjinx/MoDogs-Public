package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.AmericanFoxhoundModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.AmericanFoxhoundCollarLayer;
import com.kitnjinx.modogs.entity.custom.AmericanFoxhoundEntity;
import com.kitnjinx.modogs.entity.variant.AmericanFoxhoundVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class AmericanFoxhoundRenderer extends GeoEntityRenderer<AmericanFoxhoundEntity> {

    public static final Map<AmericanFoxhoundVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(AmericanFoxhoundVariant.class), (var) -> {
                var.put(AmericanFoxhoundVariant.LIGHT,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/american_foxhound/american_foxhound_light.png"));
                var.put(AmericanFoxhoundVariant.MEDIUM,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/american_foxhound/american_foxhound_medium.png"));
                var.put(AmericanFoxhoundVariant.DARK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/american_foxhound/american_foxhound_dark.png"));
            });

    public AmericanFoxhoundRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AmericanFoxhoundModel());

        addLayer(new AmericanFoxhoundCollarLayer(this));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(AmericanFoxhoundEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(AmericanFoxhoundEntity animatable, float partialTicks, PoseStack stack,
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
