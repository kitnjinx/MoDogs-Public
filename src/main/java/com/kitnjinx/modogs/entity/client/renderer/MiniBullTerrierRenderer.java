package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.MiniBullTerrierModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.MiniBullTerrierWhiteLayer;
import com.kitnjinx.modogs.entity.client.renderer.layer.collar.MiniBullTerrierCollarLayer;
import com.kitnjinx.modogs.entity.custom.MiniBullTerrierEntity;
import com.kitnjinx.modogs.entity.variant.BullTerrierVariant;
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

public class MiniBullTerrierRenderer extends GeoEntityRenderer<MiniBullTerrierEntity> {

    public static final Map<BullTerrierVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(BullTerrierVariant.class), (var) -> {
                var.put(BullTerrierVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bull_terrier/bull_terrier_black.png"));
                var.put(BullTerrierVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bull_terrier/mini_bull_terrier_red.png"));
            });

    public MiniBullTerrierRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MiniBullTerrierModel());

        addRenderLayer(new MiniBullTerrierWhiteLayer(this));
        addRenderLayer(new MiniBullTerrierCollarLayer(this));

        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(MiniBullTerrierEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, MiniBullTerrierEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~12 inches
        if(animatable.isBaby()) {
            withScale(0.3f, 0.3f);
        } else {
            withScale(0.6f, 0.6f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(MiniBullTerrierEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}