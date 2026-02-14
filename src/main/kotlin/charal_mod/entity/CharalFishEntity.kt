package charal_mod.entity

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.animal.WaterAnimal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal
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
) : WaterAnimal(type, level), GeoEntity {

    private val animationCache: AnimatableInstanceCache = SingletonAnimatableInstanceCache(this)

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

    override fun registerGoals() {
        super.registerGoals()
        this.goalSelector.addGoal(1, RandomSwimmingGoal(this, 1.4, 40))
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

    companion object {
        private val SWIM_ANIMATION: RawAnimation =
            RawAnimation.begin().thenLoop("swim")
    }
}
