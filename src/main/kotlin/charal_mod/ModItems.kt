package charal_mod

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.MobBucketItem
import net.minecraft.world.item.SpawnEggItem
import net.minecraft.world.item.Tier
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.material.Fluids

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

    private val CHARAL_TOOL_TIER: Tier = object : Tier {
        override fun getUses(): Int = 40

        override fun getSpeed(): Float = 1.5f

        override fun getAttackDamageBonus(): Float = 0.0f

        override fun getLevel(): Int = 0

        override fun getEnchantmentValue(): Int = 5

        override fun getRepairIngredient(): Ingredient = Ingredient.EMPTY
    }

    private val CHARAL_ARMOR_MATERIAL: ArmorMaterial = object : ArmorMaterial {
        // Durabilidades explícitas por pieza (todas por debajo del cuero, pero suficientes para muchos bocados).
        private val durabilityByType = mapOf(
            ArmorItem.Type.BOOTS to 45,      // cuero: 65
            ArmorItem.Type.LEGGINGS to 55,   // cuero: 75
            ArmorItem.Type.CHESTPLATE to 60, // cuero: 80
            ArmorItem.Type.HELMET to 40      // cuero: 55
        )

        private val defenseByType = mapOf(
            ArmorItem.Type.BOOTS to 1,
            ArmorItem.Type.LEGGINGS to 1,
            ArmorItem.Type.CHESTPLATE to 2,
            ArmorItem.Type.HELMET to 1
        )

        override fun getDurabilityForType(type: ArmorItem.Type): Int = durabilityByType.getValue(type)

        override fun getDefenseForType(type: ArmorItem.Type): Int = defenseByType.getValue(type)

        override fun getEnchantmentValue(): Int = 8

        override fun getEquipSound() = SoundEvents.ARMOR_EQUIP_LEATHER

        override fun getRepairIngredient(): Ingredient = Ingredient.of(RAW_CHARAL)

        override fun getName(): String = "charal_mod:armor"

        override fun getToughness(): Float = 0.0f

        override fun getKnockbackResistance(): Float = 0.0f
    }

    val CHARAL_BUCKET: Item = registerItem(
        "charal_bucket",
        MobBucketItem(
            ModEntities.CHARAL_FISH,
            Fluids.WATER,
            SoundEvents.BUCKET_EMPTY_FISH,
            Item.Properties()
                .stacksTo(1)
                .craftRemainder(Items.BUCKET)
        )
    )

    val CHARAL_HELMET: Item = registerItem(
        "charal_helmet",
        CharalArmorItem(
            CHARAL_ARMOR_MATERIAL,
            ArmorItem.Type.HELMET,
            Item.Properties(),
            3,
            0.6f
        )
    )

    val CHARAL_CHESTPLATE: Item = registerItem(
        "charal_chestplate",
        CharalArmorItem(
            CHARAL_ARMOR_MATERIAL,
            ArmorItem.Type.CHESTPLATE,
            Item.Properties(),
            3,
            0.6f
        )
    )

    val CHARAL_LEGGINGS: Item = registerItem(
        "charal_leggings",
        CharalArmorItem(
            CHARAL_ARMOR_MATERIAL,
            ArmorItem.Type.LEGGINGS,
            Item.Properties(),
            3,
            0.6f
        )
    )

    val CHARAL_BOOTS: Item = registerItem(
        "charal_boots",
        CharalArmorItem(
            CHARAL_ARMOR_MATERIAL,
            ArmorItem.Type.BOOTS,
            Item.Properties(),
            3,
            0.6f
        )
    )

    val CHARAL_SWORD: Item = registerItem(
        "charal_sword",
        CharalSwordItem(
            CHARAL_TOOL_TIER,
            1,
            -2.4f,
            Item.Properties(),
            3,
            0.6f
        )
    )

    val CHARAL_PICKAXE: Item = registerItem(
        "charal_pickaxe",
        CharalPickaxeItem(
            CHARAL_TOOL_TIER,
            0,
            -2.8f,
            Item.Properties(),
            3,
            0.6f
        )
    )

    val CHARAL_SHOVEL: Item = registerItem(
        "charal_shovel",
        CharalShovelItem(
            CHARAL_TOOL_TIER,
            0.5f,
            -3.0f,
            Item.Properties(),
            3,
            0.6f
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

    val GOLD_CHARAL: Item = registerItem(
        "gold_charal",
        Item(
            Item.Properties().food(
                FoodProperties.Builder()
                    .nutrition(6)
                    .saturationMod(1.2f)
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
