package charal_mod

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.item.CreativeModeTabs
import org.slf4j.LoggerFactory
import software.bernie.geckolib.GeckoLib

object Charal_mod : ModInitializer {
	private val logger = LoggerFactory.getLogger("charal_mod")

	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		GeckoLib.initialize()
		ModItems.registerAll()
		ModEntities.registerAll()
		registerItemGroups()
		registerNaturalSpawns()
		registerArmorSetBonus()
		logger.info("Hello Fabric world!")
	}

	private fun registerItemGroups() {
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register { entries ->
			entries.accept(ModItems.CHARAL_FISH_SPAWN_EGG)
		}
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register { entries ->
			entries.accept(ModItems.RAW_CHARAL)
			entries.accept(ModItems.COOKED_CHARAL)
			entries.accept(ModItems.GOLD_CHARAL)
		}
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register { entries ->
			entries.accept(ModItems.CHARAL_BUCKET)
			entries.accept(ModItems.CHARAL_PICKAXE)
			entries.accept(ModItems.CHARAL_SHOVEL)
		}
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register { entries ->
			entries.accept(ModItems.CHARAL_SWORD)
			entries.accept(ModItems.CHARAL_HELMET)
			entries.accept(ModItems.CHARAL_CHESTPLATE)
			entries.accept(ModItems.CHARAL_LEGGINGS)
			entries.accept(ModItems.CHARAL_BOOTS)
		}
	}

	private fun registerNaturalSpawns() {
		BiomeModifications.addSpawn(
			BiomeSelectors.foundInOverworld(),
			MobCategory.WATER_AMBIENT,
			ModEntities.CHARAL_FISH,
			25,
			5,
			20
		)
	}

	private fun registerArmorSetBonus() {
		ServerTickEvents.END_WORLD_TICK.register(ServerTickEvents.EndWorldTick { world ->
			for (player in world.players()) {
				applyCharalArmorBonus(player)
			}
		})
	}

	private fun applyCharalArmorBonus(player: ServerPlayer) {
		val head = player.getItemBySlot(EquipmentSlot.HEAD)
		val chest = player.getItemBySlot(EquipmentSlot.CHEST)
		val legs = player.getItemBySlot(EquipmentSlot.LEGS)
		val feet = player.getItemBySlot(EquipmentSlot.FEET)

		val hasFullSet =
			head.item == ModItems.CHARAL_HELMET &&
			chest.item == ModItems.CHARAL_CHESTPLATE &&
			legs.item == ModItems.CHARAL_LEGGINGS &&
			feet.item == ModItems.CHARAL_BOOTS

		if (hasFullSet && player.isInWater) {
			// Bonus de set completo: ligera velocidad y respiración acuática dentro del agua.
			// Duraciones cortas para que se renueven suavemente cada tick mientras se cumpla la condición.
			player.addEffect(MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 0))
			player.addEffect(MobEffectInstance(MobEffects.WATER_BREATHING, 10, 0))

			val level = player.level()
			if (level is ServerLevel) {
				level.sendParticles(
					ParticleTypes.BUBBLE,
					player.x,
					player.y + 0.5,
					player.z,
					3,
					0.3,
					0.5,
					0.3,
					0.01
				)
			}
		}
	}
}