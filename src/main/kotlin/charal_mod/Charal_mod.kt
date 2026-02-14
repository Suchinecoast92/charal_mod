package charal_mod

import net.fabricmc.api.ModInitializer
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
		logger.info("Hello Fabric world!")
	}
}