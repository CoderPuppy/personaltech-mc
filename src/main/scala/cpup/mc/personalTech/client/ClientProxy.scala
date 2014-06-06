package cpup.mc.personalTech.client

import cpup.mc.personalTech.CommonProxy
import cpup.mc.personalTech.air
import cpw.mods.fml.common.FMLCommonHandler
import net.minecraftforge.common.MinecraftForge

class ClientProxy extends CommonProxy {
	override def registerEvents {
		super.registerEvents
		val airClientEvents = new air.ClientEvents
		FMLCommonHandler.instance().bus().register(airClientEvents)
		MinecraftForge.EVENT_BUS.register(airClientEvents)
	}
}
