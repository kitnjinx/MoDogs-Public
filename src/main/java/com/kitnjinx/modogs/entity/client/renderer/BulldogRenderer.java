package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.BulldogModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.BulldogCollarLayer;
import com.kitnjinx.modogs.entity.custom.BulldogEntity;
import com.kitnjinx.modogs.entity.variant.BulldogVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class BulldogRenderer extends GeoEntityRenderer<BulldogEntity> {

    public static final Map<BulldogVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(BulldogVariant.class), (var) -> {
                var.put(BulldogVariant.RED_WHITE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bulldog/bulldog_red_white.png"));
                var.put(BulldogVariant.FAWN_WHITE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bulldog/bulldog_fawn_white.png"));
                var.put(BulldogVariant.WHITE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bulldog/bulldog_white.png"));
                var.put(BulldogVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bulldog/bulldog_red.png"));
                var.put(BulldogVariant.FAWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bulldog/bulldog_fawn.png"));
            });

    public BulldogRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BulldogModel());

        addLayer(new BulldogCollarLayer(this));

        this.shadowRadius = 0.375f;
    }

    @Override
    public ResourceLocation getTextureLocation(BulldogEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(BulldogEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~15 inches
        if(animatable.isBaby()) {
            stack.scale(0.375f, 0.375f, 0.375f);
        } else {
            stack.scale(0.75f, 0.75f, 0.75f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
