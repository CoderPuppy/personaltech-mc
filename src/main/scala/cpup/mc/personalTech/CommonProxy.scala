package cpup.mc.personalTech

import cpup.mc.lib.CPupCommonProxy
import cpw.mods.fml.common.FMLCommonHandler
import net.minecraftforge.common.MinecraftForge

class CommonProxy extends CPupCommonProxy[PersonalTech.type] {
	def registerEvents {
		val airCommonEvents = new air.CommonEvents
		FMLCommonHandler.instance().bus().register(airCommonEvents)
		MinecraftForge.EVENT_BUS.register(airCommonEvents)
	}
}
