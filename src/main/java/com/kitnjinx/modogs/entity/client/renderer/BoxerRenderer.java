package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.BoxerModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.BoxerCollarLayer;
import com.kitnjinx.modogs.entity.custom.BoxerEntity;
import com.kitnjinx.modogs.entity.variant.BoxerVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import java.util.Map;

public class BoxerRenderer extends GeoEntityRenderer<BoxerEntity> {

    public static final Map<BoxerVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(BoxerVariant.class), (var) -> {
                var.put(BoxerVariant.MEDIUM,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/boxer/boxer_medium.png"));
                var.put(BoxerVariant.LIGHT,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/boxer/boxer_light.png"));
                var.put(BoxerVariant.DARK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/boxer/boxer_dark.png"));
                var.put(BoxerVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/boxer/boxer_black.png"));
            });

    public BoxerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BoxerModel());

        addLayer(new BoxerCollarLayer(this));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(BoxerEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(BoxerEntity animatable, float partialTicks, PoseStack stack,
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
