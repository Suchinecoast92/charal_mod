package charal_mod

import charal_mod.entity.CharalFishEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.renderer.GeoEntityRenderer

class CharalFishRenderer(
    context: EntityRendererProvider.Context
) : GeoEntityRenderer<CharalFishEntity>(context, CharalFishModel()) {

    init {
        shadowRadius = 0.2f
    }

    override fun getTextureLocation(animatable: CharalFishEntity): ResourceLocation {
        return ResourceLocation("charal_mod", "textures/entity/charal_fish.png")
    }
}
