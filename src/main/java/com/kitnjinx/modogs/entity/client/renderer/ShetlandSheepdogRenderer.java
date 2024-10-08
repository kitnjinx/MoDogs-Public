package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.ShetlandSheepdogModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.ShetlandSheepdogMerleLayer;
import com.kitnjinx.modogs.entity.client.renderer.layer.ShetlandSheepdogWhiteLayer;
import com.kitnjinx.modogs.entity.client.renderer.layer.collar.ShetlandSheepdogCollarLayer;
import com.kitnjinx.modogs.entity.custom.ShetlandSheepdogEntity;
import com.kitnjinx.modogs.entity.variant.ShetlandSheepdogVariant;
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

public class ShetlandSheepdogRenderer extends GeoEntityRenderer<ShetlandSheepdogEntity> {

    public static final Map<ShetlandSheepdogVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(ShetlandSheepdogVariant.class), (var) -> {
                var.put(ShetlandSheepdogVariant.SABLE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/shetland_sheepdog_sable.png"));
                var.put(ShetlandSheepdogVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/shetland_sheepdog_black.png"));
                var.put(ShetlandSheepdogVariant.BLACK_TAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/shetland_sheepdog_black_tan.png"));
            });

    public ShetlandSheepdogRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ShetlandSheepdogModel());

        addRenderLayer(new ShetlandSheepdogMerleLayer(this));
        addRenderLayer(new ShetlandSheepdogWhiteLayer(this));
        addRenderLayer(new ShetlandSheepdogCollarLayer(this));

        this.shadowRadius = 0.35f;
    }

    @Override
    public ResourceLocation getTextureLocation(ShetlandSheepdogEntity instance) {
        if (instance.getVariant() == ShetlandSheepdogVariant.BLACK_TAN) {
            return LOCATION_BY_VARIANT.get(ShetlandSheepdogVariant.BLACK);
        } else {
            return LOCATION_BY_VARIANT.get(instance.getVariant());
        }
    }

    @Override
    public void preRender(PoseStack stack, ShetlandSheepdogEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~14 in
        if(animatable.isBaby()) {
            withScale(0.35f, 0.35f);
        } else {
            withScale(0.7f, 0.7f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(ShetlandSheepdogEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
