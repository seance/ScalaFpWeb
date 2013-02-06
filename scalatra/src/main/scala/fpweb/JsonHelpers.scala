package fpweb

import net.liftweb.json._
import net.liftweb.json.Extraction._
import org.scalatra.ScalatraBase
  
trait JsonHelpers extends ScalatraBase {
  
  before() {
    contentType = "application/json; charset=utf-8"
  }
  
  implicit val formats: Formats  

  object Json {
    def apply(json: JValue, compacting: Boolean) = {
      val doc = render(json)
      if (compacting) compact(doc) else pretty(doc)
    }

    def apply(a: Any): Any = apply(decompose(a), compacting=true)
  }
}