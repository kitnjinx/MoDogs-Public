package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.BullTerrierModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.BullTerrierCollarLayer;
import com.kitnjinx.modogs.entity.client.renderer.layer.BullTerrierTargetLayer;
import com.kitnjinx.modogs.entity.custom.BullTerrierEntity;
import com.kitnjinx.modogs.entity.custom.BulldogEntity;
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

public class BullTerrierRenderer extends GeoEntityRenderer<BullTerrierEntity> {

    public static final Map<BullTerrierVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(BullTerrierVariant.class), (var) -> {
                var.put(BullTerrierVariant.WHITE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bull_terrier/bull_terrier_white.png"));
                var.put(BullTerrierVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bull_terrier/bull_terrier_black.png"));
                var.put(BullTerrierVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bull_terrier/bull_terrier_red.png"));
                var.put(BullTerrierVariant.WHITE_BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bull_terrier/bull_terrier_white_black.png"));
                var.put(BullTerrierVariant.WHITE_RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bull_terrier/bull_terrier_white_red.png"));
            });

    public BullTerrierRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BullTerrierModel());

        addRenderLayer(new BullTerrierCollarLayer(this));
        addRenderLayer(new BullTerrierTargetLayer(this));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(BullTerrierEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, BullTerrierEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~22 inches
        if(animatable.isBaby()) {
            stack.scale(0.5f, 0.5f, 0.5f);
        } else {
            stack.scale(1f, 1f, 1f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(BullTerrierEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}