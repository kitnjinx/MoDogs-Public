package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.BorderCollieModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.BorderCollieCollarLayer;
import com.kitnjinx.modogs.entity.client.renderer.layer.BorderCollieMerleLayer;
import com.kitnjinx.modogs.entity.client.renderer.layer.BorderCollieStripeLayer;
import com.kitnjinx.modogs.entity.custom.BloodhoundEntity;
import com.kitnjinx.modogs.entity.custom.BorderCollieEntity;
import com.kitnjinx.modogs.entity.variant.BorderCollieVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;
import java.util.Map;

public class BorderCollieRenderer extends GeoEntityRenderer<BorderCollieEntity> {

    public static final Map<BorderCollieVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(BorderCollieVariant.class), (var) -> {
                var.put(BorderCollieVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/border_collie/border_collie_black.png"));
                var.put(BorderCollieVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/border_collie/border_collie_red.png"));
                var.put(BorderCollieVariant.LILAC,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/border_collie/border_collie_lilac.png"));
            });

    public BorderCollieRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BorderCollieModel());

        addRenderLayer(new BorderCollieMerleLayer(this));
        addRenderLayer(new BorderCollieStripeLayer(this));
        addRenderLayer(new BorderCollieCollarLayer(this));

        this.shadowRadius = 0.45f;
    }

    @Override
    public ResourceLocation getTextureLocation(BorderCollieEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, BorderCollieEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~20 in
        if(animatable.isBaby()) {
            withScale(0.4f, 0.4f);
        } else {
            withScale(0.9f, 0.9f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(BorderCollieEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
