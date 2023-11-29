package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.MastiffModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.MastiffCollarLayer;
import com.kitnjinx.modogs.entity.custom.MastiffEntity;
import com.kitnjinx.modogs.entity.custom.MastiffEntity;
import com.kitnjinx.modogs.entity.variant.MastiffVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class MastiffRenderer extends GeoEntityRenderer<MastiffEntity> {

    public static final Map<MastiffVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(MastiffVariant.class), (var) -> {
                var.put(MastiffVariant.FAWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/mastiff/mastiff_fawn.png"));
                var.put(MastiffVariant.APRICOT,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/mastiff/mastiff_apricot.png"));
            });

    public MastiffRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MastiffModel());

        addLayer(new MastiffCollarLayer(this));

        this.shadowRadius = 0.65f;
    }

    @Override
    public ResourceLocation getTextureLocation(MastiffEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(MastiffEntity animatable, float partialTicks, PoseStack stack,
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
