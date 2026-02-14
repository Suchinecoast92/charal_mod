package charal_mod

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry

object Charal_modClient : ClientModInitializer {
	override fun onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		EntityRendererRegistry.register(ModEntities.CHARAL_FISH) { context ->
			CharalFishRenderer(context)
		}
	}
}