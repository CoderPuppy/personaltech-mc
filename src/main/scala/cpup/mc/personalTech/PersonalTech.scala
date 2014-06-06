package cpup.mc.personalTech

import cpup.mc.lib.CPupMod
import cpw.mods.fml.common.Mod

@Mod(modid = Ref.modID, modLanguage = "scala")
object PersonalTech extends CPupMod[Ref.type] {
	override def ref = Ref
}