package cpup.mc.personalTech.air

import net.minecraft.client.settings.KeyBinding
import cpup.mc.personalTech.PersonalTech
import org.lwjgl.input.Keyboard
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.Display
import cpw.mods.fml.client.registry.ClientRegistry

class ClientEvents {
	def mod = PersonalTech
	val choosePowerKeyBind = new KeyBinding(s"key.${mod.ref.modID}:air.choosePower", Keyboard.KEY_V, s"key.categories.${mod.ref.modID}")
	ClientRegistry.registerKeyBinding(choosePowerKeyBind)

	@SubscribeEvent
	def renderOverlay(e: RenderGameOverlayEvent) {
		if(choosePowerKeyBind.getIsKeyPressed) {
			val tess = Tessellator.instance
			val mc = Minecraft.getMinecraft
			val screenWidth = Display.getWidth
			val screenHeight = Display.getHeight

			tess.setColorOpaque(255, 255, 255)
			tess.startDrawingQuads()
			val width = 50
			val height = 50
			val x = (screenWidth / 2) + (width / 2)
			val y = (screenHeight / 2) + (height / 2)
			tess.addVertex(x,         y,          0)
			tess.addVertex(x,         y + height, 0)
			tess.addVertex(x + width, y + height, 0)
			tess.addVertex(x + width, y,          0)
			tess.draw()
		}
	}
}