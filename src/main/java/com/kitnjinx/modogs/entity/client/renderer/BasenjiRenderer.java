package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.BasenjiModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.BasenjiCollarLayer;
import com.kitnjinx.modogs.entity.custom.BasenjiEntity;
import com.kitnjinx.modogs.entity.variant.BasenjiVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class BasenjiRenderer extends GeoEntityRenderer<BasenjiEntity> {

    public static final Map<BasenjiVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(BasenjiVariant.class), (var) -> {
                var.put(BasenjiVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/basenji/basenji_red.png"));
                var.put(BasenjiVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/basenji/basenji_black.png"));
                var.put(BasenjiVariant.TRICOLOR,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/basenji/basenji_tricolor.png"));
            });

    public BasenjiRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BasenjiModel());

        addLayer(new BasenjiCollarLayer(this));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(BasenjiEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(BasenjiEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~16 in
        if(animatable.isBaby()) {
            stack.scale(0.4f, 0.4f, 0.4f);
        } else {
            stack.scale(0.8f, 0.8f, 0.8f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
