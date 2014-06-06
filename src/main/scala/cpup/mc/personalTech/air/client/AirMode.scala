package cpup.mc.personalTech.air.client

sealed trait AirMode {}

object AirMode {
	case object Disabled extends AirMode
	case class Attack(dmg: Int) extends AirMode
	case class Harvest(tool: String, level: Int) extends AirMode
}