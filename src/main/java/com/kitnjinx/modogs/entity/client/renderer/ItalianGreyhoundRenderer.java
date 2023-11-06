package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.ItalianGreyhoundModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.ItalianGreyhoundCollarLayer;
import com.kitnjinx.modogs.entity.custom.ItalianGreyhoundEntity;
import com.kitnjinx.modogs.entity.variant.ItalianGreyhoundVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class ItalianGreyhoundRenderer extends GeoEntityRenderer<ItalianGreyhoundEntity> {

    public static final Map<ItalianGreyhoundVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(ItalianGreyhoundVariant.class), (var) -> {
                var.put(ItalianGreyhoundVariant.BLUE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/italian_greyhound/italian_greyhound_blue.png"));
                var.put(ItalianGreyhoundVariant.FAWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/italian_greyhound/italian_greyhound_fawn.png"));
                var.put(ItalianGreyhoundVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/italian_greyhound/italian_greyhound_black.png"));
                var.put(ItalianGreyhoundVariant.WHITE_BLUE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/italian_greyhound/italian_greyhound_white_blue.png"));
                var.put(ItalianGreyhoundVariant.WHITE_FAWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/italian_greyhound/italian_greyhound_white_fawn.png"));
                var.put(ItalianGreyhoundVariant.WHITE_BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/italian_greyhound/italian_greyhound_white_black.png"));
            });

    public ItalianGreyhoundRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ItalianGreyhoundModel());

        addLayer(new ItalianGreyhoundCollarLayer(this));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(ItalianGreyhoundEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(ItalianGreyhoundEntity animatable, float partialTicks, PoseStack stack,
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
