package charal_mod

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.SpawnEggItem

object ModItems {
    private const val MOD_ID: String = "charal_mod"

    val RAW_CHARAL: Item = registerItem(
        "raw_charal",
        Item(
            Item.Properties().food(
                FoodProperties.Builder()
                    .nutrition(1)
                    .saturationMod(0.1f)
                    .build()
            )
        )
    )

    val CHARAL_FISH_SPAWN_EGG: Item = registerItem(
        "charal_fish_spawn_egg",
        SpawnEggItem(
            ModEntities.CHARAL_FISH,
            0xC0C0C0,
            0x2F6DB5,
            Item.Properties()
        )
    )

    val COOKED_CHARAL: Item = registerItem(
        "cooked_charal",
        Item(
            Item.Properties().food(
                FoodProperties.Builder()
                    .nutrition(3)
                    .saturationMod(0.6f)
                    .build()
            )
        )
    )

    private fun registerItem(name: String, item: Item): Item {
        return Registry.register(BuiltInRegistries.ITEM, ResourceLocation(MOD_ID, name), item)
    }

    fun registerAll() {
        // Método de entrada para registrar ítems si hace falta lógica extra en el futuro.
    }
}
