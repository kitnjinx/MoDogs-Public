package com.kitnjinx.modogs.entity.client.renderer.layer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.custom.ShetlandSheepdogEntity;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import java.util.Map;

public class ShetlandSheepdogCollarLayer extends GeoLayerRenderer {
    public static final Map<CollarVariant, ResourceLocation> LOCATION_BY_COLOR =
            Util.make(Maps.newEnumMap(CollarVariant.class), (col) -> {
                col.put(CollarVariant.NONE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/collar/collar_none.png"));
                col.put(CollarVariant.WHITE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/collar/collar_white.png"));
                col.put(CollarVariant.LIGHT_GRAY,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/collar/collar_light_gray.png"));
                col.put(CollarVariant.GRAY,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/collar/collar_gray.png"));
                col.put(CollarVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/collar/collar_black.png"));
                col.put(CollarVariant.BROWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/collar/collar_brown.png"));
                col.put(CollarVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/collar/collar_red.png"));
                col.put(CollarVariant.ORANGE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/collar/collar_orange.png"));
                col.put(CollarVariant.YELLOW,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/collar/collar_yellow.png"));
                col.put(CollarVariant.LIME,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/collar/collar_lime.png"));
                col.put(CollarVariant.GREEN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/collar/collar_green.png"));
                col.put(CollarVariant.CYAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/collar/collar_cyan.png"));
                col.put(CollarVariant.LIGHT_BLUE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/collar/collar_light_blue.png"));
                col.put(CollarVariant.BLUE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/collar/collar_blue.png"));
                col.put(CollarVariant.PURPLE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/collar/collar_purple.png"));
                col.put(CollarVariant.MAGENTA,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/collar/collar_magenta.png"));
                col.put(CollarVariant.PINK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/collar/collar_pink.png"));
            });

    private static final ResourceLocation MODEL = new ResourceLocation(MoDogs.MOD_ID, "geo/shetland_sheepdog.geo.json");

    public ShetlandSheepdogCollarLayer(IGeoRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    public ResourceLocation getCollarLocation(ShetlandSheepdogEntity instance) {
        return LOCATION_BY_COLOR.get(instance.getCollar());
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Entity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType cameo =  RenderType.armorCutoutNoCull(getCollarLocation((ShetlandSheepdogEntity) entityLivingBaseIn));
        matrixStackIn.pushPose();

        this.getRenderer().render(this.getEntityModel().getModel(MODEL), entityLivingBaseIn, partialTicks, cameo, matrixStackIn, bufferIn,
                bufferIn.getBuffer(cameo), packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
        matrixStackIn.popPose();
    }
}