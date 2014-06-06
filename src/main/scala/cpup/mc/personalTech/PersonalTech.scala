package cpup.mc.personalTech

import cpup.mc.lib.CPupMod
import cpw.mods.fml.common.{SidedProxy, Mod}
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.Mod.EventHandler

@Mod(modid = Ref.modID, modLanguage = "scala")
object PersonalTech extends CPupMod[Ref.type] {
	override def ref = Ref

	@SidedProxy(clientSide = "cpup.mc.personalTech.client.ClientProxy", serverSide = "cpup.mc.personalTech.CommonProxy")
	var proxy: CommonProxy = null

	@EventHandler
	override def preInit(e: FMLPreInitializationEvent) {
		super.preInit(e)
		proxy.registerEvents
	}
}