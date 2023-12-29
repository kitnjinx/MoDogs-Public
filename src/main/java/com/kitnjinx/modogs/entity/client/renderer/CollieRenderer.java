package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.CollieModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.CollieCollarLayer;
import com.kitnjinx.modogs.entity.custom.CollieEntity;
import com.kitnjinx.modogs.entity.variant.CollieVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class CollieRenderer extends GeoEntityRenderer<CollieEntity> {

    public static final Map<CollieVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(CollieVariant.class), (var) -> {
                var.put(CollieVariant.SABLE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/collie/collie_sable.png"));
                var.put(CollieVariant.BLACK_TAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/collie/collie_black_tan.png"));
                var.put(CollieVariant.SABLE_MERLE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/collie/collie_sable_merle.png"));
                var.put(CollieVariant.BLUE_TAN_MERLE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/collie/collie_blue_tan_merle.png"));
            });

    public CollieRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CollieModel());

        addLayer(new CollieCollarLayer(this));

        this.shadowRadius = 0.525f;
    }

    @Override
    public ResourceLocation getTextureLocation(CollieEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(CollieEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~24 inches
        if(animatable.isBaby()) {
            stack.scale(0.55f, 0.55f, 0.55f);
        } else {
            stack.scale(1.1f, 1.1f, 1.1f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
