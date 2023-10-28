package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.ShetlandSheepdogModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.ShetlandSheepdogCollarLayer;
import com.kitnjinx.modogs.entity.custom.ShetlandSheepdogEntity;
import com.kitnjinx.modogs.entity.variant.ShetlandSheepdogVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class ShetlandSheepdogRenderer extends GeoEntityRenderer<ShetlandSheepdogEntity> {

    public static final Map<ShetlandSheepdogVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(ShetlandSheepdogVariant.class), (var) -> {
                var.put(ShetlandSheepdogVariant.SABLE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/shetland_sheepdog_sable.png"));
                var.put(ShetlandSheepdogVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/shetland_sheepdog_black.png"));
                var.put(ShetlandSheepdogVariant.BLACK_TAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/shetland_sheepdog_black_tan.png"));
                var.put(ShetlandSheepdogVariant.SABLE_MERLE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/shetland_sheepdog_sable_merle.png"));
                var.put(ShetlandSheepdogVariant.BLUE_MERLE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/shetland_sheepdog_blue_merle.png"));
                var.put(ShetlandSheepdogVariant.BLACK_TAN_MERLE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/shetland_sheepdog_black_tan_merle.png"));
            });

    public ShetlandSheepdogRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ShetlandSheepdogModel());

        addLayer(new ShetlandSheepdogCollarLayer(this));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(ShetlandSheepdogEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(ShetlandSheepdogEntity animatable, float partialTicks, PoseStack stack,
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
