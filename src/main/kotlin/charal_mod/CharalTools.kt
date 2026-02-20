package charal_mod

import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.*
import net.minecraft.world.level.Level

/**
 * Herramientas de charal comestibles: se pueden usar como herramientas normales,
 * pero al mantener clic derecho se "muerden" y restauran algo de hambre a cambio de durabilidad.
 *
 * Implementamos el comportamiento de comida directamente en cada tipo de herramienta para
 * reutilizar toda la lógica de SwordItem/PickaxeItem/ShovelItem sin pelear con DiggerItem.
 */

private fun canStartEating(player: Player): Boolean = player.canEat(false)

private fun applyCharalBite(
    stack: ItemStack,
    level: Level,
    entity: LivingEntity,
    nutrition: Int,
    saturation: Float
) {
    if (entity is Player) {
        if (!level.isClientSide) {
            entity.foodData.eat(nutrition, saturation)
        }

        // Un "bocado" daña un punto de durabilidad
        stack.hurtAndBreak(1, entity) { e ->
            e.broadcastBreakEvent(EquipmentSlot.MAINHAND)
        }
    }
}

class CharalSwordItem(
    tier: Tier,
    private val attackDamageModifier: Int,
    attackSpeedModifier: Float,
    properties: Item.Properties,
    private val nutrition: Int,
    private val saturation: Float
) : SwordItem(tier, attackDamageModifier, attackSpeedModifier, properties) {

    override fun getUseAnimation(stack: ItemStack): UseAnim = UseAnim.EAT

    override fun getUseDuration(stack: ItemStack): Int = 32

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(hand)

        return if (canStartEating(player)) {
            player.startUsingItem(hand)
            InteractionResultHolder.consume(stack)
        } else {
            InteractionResultHolder.fail(stack)
        }
    }

    override fun finishUsingItem(stack: ItemStack, level: Level, entity: LivingEntity): ItemStack {
        applyCharalBite(stack, level, entity, nutrition, saturation)
        return stack
    }
}

class CharalPickaxeItem(
    tier: Tier,
    private val attackDamageModifier: Int,
    attackSpeedModifier: Float,
    properties: Item.Properties,
    private val nutrition: Int,
    private val saturation: Float
) : PickaxeItem(tier, attackDamageModifier, attackSpeedModifier, properties) {

    override fun getUseAnimation(stack: ItemStack): UseAnim = UseAnim.EAT

    override fun getUseDuration(stack: ItemStack): Int = 32

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(hand)

        return if (canStartEating(player)) {
            player.startUsingItem(hand)
            InteractionResultHolder.consume(stack)
        } else {
            InteractionResultHolder.fail(stack)
        }
    }

    override fun finishUsingItem(stack: ItemStack, level: Level, entity: LivingEntity): ItemStack {
        applyCharalBite(stack, level, entity, nutrition, saturation)
        return stack
    }
}

class CharalShovelItem(
    tier: Tier,
    private val attackDamageModifier: Float,
    attackSpeedModifier: Float,
    properties: Item.Properties,
    private val nutrition: Int,
    private val saturation: Float
) : ShovelItem(tier, attackDamageModifier, attackSpeedModifier, properties) {

    override fun getUseAnimation(stack: ItemStack): UseAnim = UseAnim.EAT

    override fun getUseDuration(stack: ItemStack): Int = 32

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(hand)

        return if (canStartEating(player)) {
            player.startUsingItem(hand)
            InteractionResultHolder.consume(stack)
        } else {
            InteractionResultHolder.fail(stack)
        }
    }

    override fun finishUsingItem(stack: ItemStack, level: Level, entity: LivingEntity): ItemStack {
        applyCharalBite(stack, level, entity, nutrition, saturation)
        return stack
    }
}
