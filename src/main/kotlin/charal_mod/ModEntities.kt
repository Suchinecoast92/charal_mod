package charal_mod

import charal_mod.entity.CharalFishEntity
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityDimensions
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.ai.attributes.Attributes

object ModEntities {
    private const val MOD_ID: String = "charal_mod"

    val CHARAL_FISH: EntityType<CharalFishEntity> = Registry.register(
        BuiltInRegistries.ENTITY_TYPE,
        ResourceLocation(MOD_ID, "charal_fish"),
        FabricEntityTypeBuilder.createMob<CharalFishEntity>()
            .spawnGroup(MobCategory.WATER_AMBIENT)
            .entityFactory(::CharalFishEntity)
            .dimensions(EntityDimensions.scalable(0.3f, 0.3f))
            .trackRangeChunks(4)
            .build()
    )

    fun registerAll() {
        FabricDefaultAttributeRegistry.register(
            CHARAL_FISH,
            Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0.35)
        )
    }
}
