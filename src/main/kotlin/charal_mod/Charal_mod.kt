package charal_mod

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
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
}