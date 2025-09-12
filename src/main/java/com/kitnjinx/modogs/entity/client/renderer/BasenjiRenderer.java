package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.BasenjiModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.collar.BasenjiCollarLayer;
import com.kitnjinx.modogs.entity.custom.BasenjiEntity;
import com.kitnjinx.modogs.entity.variant.BasenjiVariant;
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

public class BasenjiRenderer extends GeoEntityRenderer<BasenjiEntity> {

    public static final Map<BasenjiVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(BasenjiVariant.class), (var) -> {
                var.put(BasenjiVariant.RED,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/basenji/basenji_red.png"));
                var.put(BasenjiVariant.BLACK,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/basenji/basenji_black.png"));
                var.put(BasenjiVariant.TRICOLOR,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/basenji/basenji_tricolor.png"));
            });

    public BasenjiRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BasenjiModel());

        addRenderLayer(new BasenjiCollarLayer(this));

        this.shadowRadius = 0.4f;
    }

    @Override
    public ResourceLocation getTextureLocation(BasenjiEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack poseStack, BasenjiEntity animatable, BakedGeoModel model, @org.jetbrains.annotations.Nullable MultiBufferSource bufferSource, @org.jetbrains.annotations.Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {

        // Height ~16 in
        if(animatable.isBaby()) {
            withScale(0.4f, 0.4f);
        } else {
            withScale(0.8f, 0.8f);
        }

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public RenderType getRenderType(BasenjiEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
