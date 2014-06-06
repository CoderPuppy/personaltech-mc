package cpup.mc.personalTech.air.client

import net.minecraftforge.common.IExtendedEntityProperties
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.entity.Entity
import net.minecraft.world.World
import net.minecraft.entity.player.EntityPlayer
import cpup.mc.lib.util.EntityUtil
import cpup.mc.personalTech.PersonalTech

class PlayerData extends IExtendedEntityProperties {
	var mode: AirMode = AirMode.Disabled
	var attackMode = AirMode.Attack(2)
	var harvestMode = AirMode.Harvest("pickaxe", 0)

	override def init(entity: Entity, world: World) {}

	override def loadNBTData(compound: NBTTagCompound) {
		attackMode = AirMode.Attack(compound.getInteger("damage"))
		harvestMode = AirMode.Harvest(Option(compound.getString("tool")).getOrElse("pickaxe"), compound.getInteger("level"))
		compound.getString("mode") match {
			case "attack" => attackMode
			case "harvest" => harvestMode
			case _ => AirMode.Disabled
		}
	}

	override def saveNBTData(compound: NBTTagCompound) {
		compound.setInteger("damage", attackMode.dmg)
		compound.setString("tool", harvestMode.tool)
		compound.setInteger("level", harvestMode.level)
		compound.setString("mode", mode match {
			case `attackMode` => "attack"
			case `harvestMode` => "harvest"
			case _ => "disabled"
		})
	}
}
object PlayerData {
	def mod = PersonalTech
	def get(player: EntityPlayer) = EntityUtil.getExtendedData[PlayerData](player, s"${mod.ref.modID}:air", new PlayerData)
}