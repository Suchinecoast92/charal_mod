package charal_mod.entity

import charal_mod.ModItems
import net.minecraft.nbt.CompoundTag
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.animal.Bucketable
import net.minecraft.world.entity.animal.WaterAnimal
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal
import net.minecraft.world.entity.ai.navigation.PathNavigation
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import software.bernie.geckolib.animatable.GeoEntity
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache
import software.bernie.geckolib.core.animation.AnimatableManager
import software.bernie.geckolib.core.animation.AnimationController
import software.bernie.geckolib.core.animation.RawAnimation
import software.bernie.geckolib.core.`object`.PlayState

class CharalFishEntity(
    type: EntityType<out CharalFishEntity>,
    level: Level
) : WaterAnimal(type, level), GeoEntity, Bucketable {

    private val animationCache: AnimatableInstanceCache = SingletonAnimatableInstanceCache(this)

    private var fromBucket: Boolean = false

    init {
        // Control de movimiento suave para criaturas acuáticas (similar a los peces vanilla)
        // Valores de velocidad más bajos ayudan a evitar giros demasiado cerrados en círculo
        this.moveControl = SmoothSwimmingMoveControl(this, 85, 10, 0.02f, 0.1f, true)
    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache = animationCache

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        controllers.add(
            AnimationController(
                this,
                "swim_controller",
                0
            ) { state ->
                state.setAndContinue(SWIM_ANIMATION)
                PlayState.CONTINUE
            }
        )
    }

    override fun createNavigation(level: Level): PathNavigation {
        // Navegación ligada al agua para que busque caminos dentro del agua como un pez
        return WaterBoundPathNavigation(this, level)
    }

    override fun registerGoals() {
        super.registerGoals()
        // Velocidad algo más baja y cambio de destino más frecuente para trayectorias menos circulares
        this.goalSelector.addGoal(1, RandomSwimmingGoal(this, 1.0, 20))
        this.goalSelector.addGoal(2, AvoidEntityGoal(this, Player::class.java, 4.0f, 1.0, 1.6))
        this.goalSelector.addGoal(3, LookAtPlayerGoal(this, Player::class.java, 4.0f))
    }

    override fun aiStep() {
        super.aiStep()

        // Si está fuera del agua y en el suelo, se "ahoga" como un pez vanilla
        if (!this.isInWater && this.onGround() && !this.level().isClientSide) {
            // Pequeño salto aleatorio para simular que se agita
            this.setDeltaMovement(
                this.deltaMovement.x + (this.random.nextDouble() * 2.0 - 1.0) * 0.05,
                this.deltaMovement.y + 0.4,
                this.deltaMovement.z + (this.random.nextDouble() * 2.0 - 1.0) * 0.05
            )
            this.setOnGround(false)
            this.hasImpulse = true
            // Daño por secarse fuera del agua
            this.hurt(this.damageSources().dryOut(), 1.0f)
        }
    }

    // Bucketable implementation (firmas Mojang)

    override fun fromBucket(): Boolean = fromBucket

    override fun setFromBucket(fromBucket: Boolean) {
        this.fromBucket = fromBucket
    }

    override fun saveToBucketTag(stack: ItemStack) {
        // No guardamos datos extra por ahora (como nombre personalizado).
    }

    override fun loadFromBucketTag(nbt: CompoundTag) {
        // Marcamos que esta entidad proviene de un cubo para que pueda
        // usarse en lógica de persistencia si es necesario.
        this.fromBucket = true
    }

    override fun getBucketItemStack(): ItemStack = ItemStack(ModItems.CHARAL_BUCKET)

    override fun getPickupSound(): SoundEvent = SoundEvents.BUCKET_FILL_FISH

    override fun mobInteract(player: Player, hand: InteractionHand): InteractionResult {
        val stack = player.getItemInHand(hand)

        if (stack.`is`(Items.WATER_BUCKET) && this.isAlive) {
            this.playSound(SoundEvents.BUCKET_FILL_FISH, 1.0f, 1.0f)

            if (!this.level().isClientSide) {
                stack.shrink(1)

                val filledBucket = this.getBucketItemStack()
                if (!player.addItem(filledBucket)) {
                    player.drop(filledBucket, false)
                }

                this.discard()
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide)
        }

        return super.mobInteract(player, hand)
    }

    companion object {
        private val SWIM_ANIMATION: RawAnimation =
            RawAnimation.begin().thenLoop("swim")
    }
}
