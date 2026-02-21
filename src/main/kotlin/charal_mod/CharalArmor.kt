package charal_mod

import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level

/**
 * Armadura de charal comestible: se equipa y protege como una armadura normal,
 * pero también se puede "morder" en la mano para recuperar algo de hambre a
 * cambio de durabilidad.
 */
class CharalArmorItem(
    material: ArmorMaterial,
    type: Type,
    properties: Properties,
    private val nutrition: Int,
    private val saturation: Float
) : ArmorItem(material, type, properties) {

    override fun getUseAnimation(stack: ItemStack): UseAnim = UseAnim.EAT

    override fun getUseDuration(stack: ItemStack): Int = 32

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(hand)

        return if (player.canEat(false)) {
            // Si el jugador tiene hambre, priorizamos comer la pieza en lugar de equiparla.
            player.startUsingItem(hand)
            InteractionResultHolder.consume(stack)
        } else {
            // Si no tiene hambre, se comporta como una armadura normal (equipar al hacer clic derecho).
            super.use(level, player, hand)
        }
    }

    override fun finishUsingItem(stack: ItemStack, level: Level, entity: LivingEntity): ItemStack {
        if (entity is Player) {
            if (!level.isClientSide) {
                entity.foodData.eat(nutrition, saturation)
            }

            // Daño manual: cada bocado consume ~1/10 de la durabilidad máxima
            // para que la pieza se acabe en pocas mordidas aunque tenga buena durabilidad.
            if (!entity.isCreative) {
                val currentDamage = stack.damageValue
                val maxDamage = stack.maxDamage

                if (maxDamage > 0) {
                    val damagePerBite = (maxDamage / 10).coerceAtLeast(1)
                    val newDamage = currentDamage + damagePerBite

                    if (newDamage >= maxDamage) {
                        stack.shrink(1)
                    } else {
                        stack.damageValue = newDamage
                    }
                }
            }
        }

        return stack
    }
}
