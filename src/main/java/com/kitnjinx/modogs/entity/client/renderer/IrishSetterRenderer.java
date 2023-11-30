package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.IrishSetterModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.IrishSetterCollarLayer;
import com.kitnjinx.modogs.entity.custom.IrishSetterEntity;
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

public class IrishSetterRenderer extends GeoEntityRenderer<IrishSetterEntity> {

    public static final Map<ShadeVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(ShadeVariant.class), (var) -> {
                var.put(ShadeVariant.LIGHT,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/irish_setter/irish_setter_light.png"));
                var.put(ShadeVariant.MEDIUM,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/irish_setter/irish_setter_medium.png"));
                var.put(ShadeVariant.DARK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/irish_setter/irish_setter_dark.png"));
            });

    public IrishSetterRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new IrishSetterModel());

        addLayer(new IrishSetterCollarLayer(this));

        this.shadowRadius = 0.575f;
    }

    @Override
    public ResourceLocation getTextureLocation(IrishSetterEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(IrishSetterEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~26 in
        if(animatable.isBaby()) {
            stack.scale(0.6f, 0.6f, 0.6f);
        } else {
            stack.scale(1.2f, 1.2f, 1.2f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
