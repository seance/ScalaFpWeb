package fpweb

import unfiltered.filter.Plan
import unfiltered.request._
import unfiltered.response._
import net.liftweb.json.Extraction._

case class Todo extends Plan {
  
  val todos = Map.empty[Int, (String, Boolean)]
  
  def intent = {
    case req @ Path("/todos") => req match {
      case GET(_) => Json(decompose(todos))
    } 
  }
  
  implicit val formats = net.liftweb.json.DefaultFormats
}  