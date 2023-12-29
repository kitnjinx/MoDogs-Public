package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.PugModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.PugCollarLayer;
import com.kitnjinx.modogs.entity.custom.PugEntity;
import com.kitnjinx.modogs.entity.variant.PugVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class PugRenderer extends GeoEntityRenderer<PugEntity> {

    public static final Map<PugVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(PugVariant.class), (var) -> {
                var.put(PugVariant.FAWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/pug/pug_fawn.png"));
                var.put(PugVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/pug/pug_black.png"));
            });

    public PugRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PugModel());

        addLayer(new PugCollarLayer(this));

        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(PugEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(PugEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~12 inches
        if(animatable.isBaby()) {
            stack.scale(0.3f, 0.3f, 0.3f);
        } else {
            stack.scale(0.6f, 0.6f, 0.6f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
