package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.BeagleModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.BeagleCollarLayer;
import com.kitnjinx.modogs.entity.custom.BeagleEntity;
import com.kitnjinx.modogs.entity.variant.BeagleVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class BeagleRenderer extends GeoEntityRenderer<BeagleEntity> {

    public static final Map<BeagleVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(BeagleVariant.class), (var) -> {
                var.put(BeagleVariant.BLACK_TAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/beagle/beagle_black_tan.png"));
                var.put(BeagleVariant.TAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/beagle/beagle_tan.png"));
                var.put(BeagleVariant.DARK_EARS,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/beagle/beagle_dark_ears.png"));
            });

    public BeagleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BeagleModel());

        addLayer(new BeagleCollarLayer(this));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(BeagleEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(BeagleEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~14 in
        if(animatable.isBaby()) {
            stack.scale(0.35f, 0.35f, 0.35f);
        } else {
            stack.scale(0.7f, 0.7f, 0.7f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
