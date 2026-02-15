package charal_mod

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.minecraft.world.entity.MobCategory
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
		registerNaturalSpawns()
		logger.info("Hello Fabric world!")
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