package cpup.mc.personalTech.air

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.event.entity.player.PlayerEvent

class CommonEvents {
	@SubscribeEvent
	def checkHarvest(e: PlayerEvent.HarvestCheck) {
		PlayerData.get(e.entityPlayer).foreach((data) => {
			data.mode match {
				case AirMode.Harvest(tool, level) =>
					if(tool == e.block.getHarvestTool(0) && level > e.block.getHarvestLevel(0)) {
						e.success = true
					}
				case _ =>
			}
		})
	}

	@SubscribeEvent
	def breakSpeed(e: PlayerEvent.BreakSpeed) {
		PlayerData.get(e.entityPlayer).foreach((data) => {
			data.mode match {
				case AirMode.Harvest(tool, level) =>
					if(tool == e.block.getHarvestTool(0) && level > e.block.getHarvestLevel(0)) {
						e.newSpeed += 8
					}
				case _ =>
			}
		})
	}

	@SubscribeEvent
	def hurt(e: LivingHurtEvent) {
		e.source.getEntity match {
			case player: EntityPlayer =>
				PlayerData.get(player).foreach((data) => {
					data.mode match {
						case AirMode.Attack(dmg) =>
							e.ammount += dmg
						case _ =>
					}
				})

			case _ =>
		}
	}
}