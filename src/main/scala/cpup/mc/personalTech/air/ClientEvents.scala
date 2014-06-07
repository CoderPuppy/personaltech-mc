package cpup.mc.personalTech.air

import net.minecraft.client.settings.KeyBinding
import cpup.mc.personalTech.PersonalTech
import org.lwjgl.input.{Mouse, Keyboard}
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.client.event.{MouseEvent, RenderGameOverlayEvent}
import net.minecraft.client.renderer.{OpenGlHelper, Tessellator}
import net.minecraft.client.Minecraft
import cpw.mods.fml.client.registry.ClientRegistry
import cpw.mods.fml.common.gameevent.{InputEvent, TickEvent}
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import net.minecraft.client.gui.{ScaledResolution, Gui}

class ClientEvents {
	def mod = PersonalTech
	val choosePowerKeyBind = new KeyBinding(s"key.${mod.ref.modID}:air.choosePower", Keyboard.KEY_V, s"key.categories.${mod.ref.modID}")
	ClientRegistry.registerKeyBinding(choosePowerKeyBind)
	var wasChoosePowerDown = false
	val icons = new ResourceLocation("textures/gui/icons.png")
	var cursorX = 0
	var cursorY = 0
	var playerYaw = 0f
	var playerYawHead = 0f
	var playerPitch = 0f

	@SubscribeEvent
	def renderOverlay(e: RenderGameOverlayEvent) {
//		mod.logger.debug("is = {}, get = {}", choosePowerKeyBind.isPressed, choosePowerKeyBind.getIsKeyPressed)
		if(choosePowerKeyBind.getIsKeyPressed) {
			val tess = Tessellator.instance
			val mc = Minecraft.getMinecraft
			val screenWidth = e.resolution.getScaledWidth
			val screenHeight = e.resolution.getScaledHeight

			val data = PlayerData.get(mc.thePlayer).getOrElse(new PlayerData)

			tess.startDrawingQuads
			tess.setColorOpaque(0, 0, 0)
			val width = 2 + mc.fontRenderer.getStringWidth("Disabled") + 2 + mc.fontRenderer.getStringWidth("Attack") + 2 + mc.fontRenderer.getStringWidth("Harvest") + 2
			val height = 50
//			println("s.w, s.h", screenWidth, screenHeight, width, height)
			val x = (screenWidth - width) / 2
			val y = (screenHeight - height) / 2
			tess.addVertex(x,         y,          0)
			tess.addVertex(x,         y + height, 0)
			tess.addVertex(x + width, y + height, 0)
			tess.addVertex(x + width, y,          0)
			tess.draw
			var modeX = x + 2
			def drawOption(name: String, mode: AirMode) {
				if(data.mode == mode) {
					tess.startDrawingQuads
					tess.setColorOpaque(40, 40, 40)
					val width = mc.fontRenderer.getStringWidth(name)
					val height = 10
					tess.addVertex(modeX,         y,          0)
					tess.addVertex(modeX,         y + height, 0)
					tess.addVertex(modeX + width, y + height, 0)
					tess.addVertex(modeX + width, y,          0)
					tess.draw
				}
				mc.fontRenderer.drawStringWithShadow(name, modeX, y + 2, 16777215)
				modeX += mc.fontRenderer.getStringWidth(name) + 2
			}
			drawOption("Disabled", AirMode.Disabled)
			drawOption("Attack", data.attackMode)
			drawOption("Harvest", data.harvestMode)

			mc.renderEngine.bindTexture(icons)
			GL11.glEnable(GL11.GL_BLEND)
			OpenGlHelper.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR, 1, 0)
			drawTexturedModalRect(x + width / 2 + cursorX - 7, y + height / 2 + cursorY - 7, 0, 0, 16, 16)
			OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0)
			GL11.glDisable(GL11.GL_BLEND)
		}
	}

	@SubscribeEvent
	def renderTick(e: TickEvent.RenderTickEvent) {
		if(choosePowerKeyBind.getIsKeyPressed) {
			val mc = Minecraft.getMinecraft
			val dx = Mouse.getDX
			val dy = Mouse.getDY
//			println(dx, dy)
			cursorX += dx / 2
			cursorY += -dy / 2
			checkCursor
			mc.mouseHelper.deltaX = 0
			mc.mouseHelper.deltaY = 0
			mc.thePlayer.rotationYaw = playerYaw
			mc.thePlayer.prevRotationYaw = playerYaw
			mc.thePlayer.rotationYawHead = playerYawHead
			mc.thePlayer.prevRotationYawHead = playerYawHead
			mc.thePlayer.rotationPitch = playerPitch
			mc.thePlayer.prevRotationPitch = playerPitch
		}
	}

	def checkCursor {
		val mc = Minecraft.getMinecraft
		val scaledRes = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight)
		val screenWidth = scaledRes.getScaledWidth
		val screenHeight = scaledRes.getScaledHeight
		val data = PlayerData.get(mc.thePlayer).getOrElse(new PlayerData)

		val width = 2 + mc.fontRenderer.getStringWidth("Disabled") + 2 + mc.fontRenderer.getStringWidth("Attack") + 2 + mc.fontRenderer.getStringWidth("Harvest") + 2
		val height = 50
		val x = (screenWidth - width) / 2
		val y = (screenHeight - height) / 2
		var modeX = -width / 2 - 2
		val modeY = -height / 2 - 2
		def checkOption(name: String, mode: AirMode) {
			val nameWidth = mc.fontRenderer.getStringWidth(name)
			val nameHeight = 10
			if(cursorX >= modeX && cursorX <= modeX + nameWidth && cursorY >= modeY && cursorY <= modeY + nameHeight) {
				data.mode = mode
			}
			modeX +=  + 2
		}
		checkOption("Disabled", AirMode.Disabled)
		checkOption("Attack", data.attackMode)
		checkOption("Harvest", data.harvestMode)
	}

	@SubscribeEvent
	def mouse(e: MouseEvent) {
		if(choosePowerKeyBind.getIsKeyPressed) {
//			cursorX += e.dx / 2
//			cursorY += -e.dy / 2
		}
	}

	def drawTexturedModalRect(par1: Int, par2: Int, par3: Int, par4: Int, par5: Int, par6: Int) {
		val f: Float = 0.00390625F
		val f1: Float = 0.00390625F
		val tessellator: Tessellator = Tessellator.instance
		tessellator.startDrawingQuads
		tessellator.addVertexWithUV((par1 + 0).asInstanceOf[Double], (par2 + par6).asInstanceOf[Double], 1, ((par3 + 0).asInstanceOf[Float] * f).asInstanceOf[Double], ((par4 + par6).asInstanceOf[Float] * f1).asInstanceOf[Double])
		tessellator.addVertexWithUV((par1 + par5).asInstanceOf[Double], (par2 + par6).asInstanceOf[Double], 1, ((par3 + par5).asInstanceOf[Float] * f).asInstanceOf[Double], ((par4 + par6).asInstanceOf[Float] * f1).asInstanceOf[Double])
		tessellator.addVertexWithUV((par1 + par5).asInstanceOf[Double], (par2 + 0).asInstanceOf[Double], 1, ((par3 + par5).asInstanceOf[Float] * f).asInstanceOf[Double], ((par4 + 0).asInstanceOf[Float] * f1).asInstanceOf[Double])
		tessellator.addVertexWithUV((par1 + 0).asInstanceOf[Double], (par2 + 0).asInstanceOf[Double], 1, ((par3 + 0).asInstanceOf[Float] * f).asInstanceOf[Double], ((par4 + 0).asInstanceOf[Float] * f1).asInstanceOf[Double])
		tessellator.draw
	}

	@SubscribeEvent
	def keyEvent(e: InputEvent.KeyInputEvent) {
		if(choosePowerKeyBind.getIsKeyPressed && !wasChoosePowerDown) {
			val mc = Minecraft.getMinecraft
			cursorX = 0
			cursorY = 0
			playerYaw = mc.thePlayer.rotationYaw
			playerYawHead = mc.thePlayer.rotationYawHead
			playerPitch = mc.thePlayer.rotationPitch
		}
		wasChoosePowerDown = choosePowerKeyBind.getIsKeyPressed
	}
}