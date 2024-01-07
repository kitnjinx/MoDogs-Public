package com.kitnjinx.modogs.entity.client.renderer;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.DalmatianModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.DalmatianSpotLayer;
import com.kitnjinx.modogs.entity.client.renderer.layer.collar.DalmatianCollarLayer;
import com.kitnjinx.modogs.entity.custom.DalmatianEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;

public class DalmatianRenderer extends GeoEntityRenderer<DalmatianEntity> {

    public static final ResourceLocation WHITE_BASE_LOCATION = new ResourceLocation(MoDogs.MOD_ID, "textures/entity/dalmatian/dalmatian_white.png");

    public DalmatianRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DalmatianModel());

        addRenderLayer(new DalmatianSpotLayer(this));
        addRenderLayer(new DalmatianCollarLayer(this));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(@NotNull DalmatianEntity instance) {
        return WHITE_BASE_LOCATION;
    }

    @Override
    public void preRender(PoseStack stack, DalmatianEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~22 inches
        if(animatable.isBaby()) {
            withScale(0.5f, 0.5f);
        } else {
            withScale(1f, 1f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(DalmatianEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}