package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.PembrokeCorgiModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.PembrokeCorgiCollarLayer;
import com.kitnjinx.modogs.entity.custom.MiniPinscherEntity;
import com.kitnjinx.modogs.entity.custom.PembrokeCorgiEntity;
import com.kitnjinx.modogs.entity.variant.PembrokeCorgiVariant;
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

public class PembrokeCorgiRenderer extends GeoEntityRenderer<PembrokeCorgiEntity> {

    public static final Map<PembrokeCorgiVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(PembrokeCorgiVariant.class), (var) -> {
                var.put(PembrokeCorgiVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/pembroke_corgi/pembroke_corgi_red.png"));
                var.put(PembrokeCorgiVariant.BLACK_TAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/pembroke_corgi/pembroke_corgi_black_tan.png"));
                var.put(PembrokeCorgiVariant.FAWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/pembroke_corgi/pembroke_corgi_fawn.png"));
                var.put(PembrokeCorgiVariant.SABLE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/pembroke_corgi/pembroke_corgi_sable.png"));
            });

    public PembrokeCorgiRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PembrokeCorgiModel());

        addRenderLayer(new PembrokeCorgiCollarLayer(this));

        this.shadowRadius = 0.35f;
    }

    @Override
    public ResourceLocation getTextureLocation(PembrokeCorgiEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, PembrokeCorgiEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~12 inches
        if(animatable.isBaby()) {
            stack.scale(0.4f, 0.4f, 0.4f);
        } else {
            stack.scale(0.8f, 0.8f, 0.8f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(PembrokeCorgiEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
