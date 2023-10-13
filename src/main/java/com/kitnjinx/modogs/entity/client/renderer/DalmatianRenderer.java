package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.DalmatianModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.DalmatianCollarLayer;
import com.kitnjinx.modogs.entity.custom.DalmatianEntity;
import com.kitnjinx.modogs.entity.variant.DalmatianVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class DalmatianRenderer extends GeoEntityRenderer<DalmatianEntity> {

    public static final Map<DalmatianVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(DalmatianVariant.class), (var) -> {
                var.put(DalmatianVariant.BLACK1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/dalmatian/dalmatian_black1.png"));
                var.put(DalmatianVariant.BLACK2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/dalmatian/dalmatian_black2.png"));
                var.put(DalmatianVariant.BLACK3,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/dalmatian/dalmatian_black3.png"));
                var.put(DalmatianVariant.BROWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/dalmatian/dalmatian_brown.png"));
            });

    public DalmatianRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DalmatianModel());

        addLayer(new DalmatianCollarLayer(this));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(DalmatianEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(DalmatianEntity animatable, float partialTicks, PoseStack stack,
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