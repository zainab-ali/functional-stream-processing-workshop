import aquascape.*
import aquascape.drawing.*
trait WorkshopAquascapeApp extends AquascapeApp {
  override def config: Config = super.config.scale(4)
  override def name: String = this.getClass.getCanonicalName().stripSuffix("$")
}
