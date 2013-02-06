package fpweb

import unfiltered.filter.Plan
import unfiltered.request._
import unfiltered.response._
import net.liftweb.json.Extraction._
import net.liftweb.json.JsonAST._
import net.liftweb.json.Serialization._
import net.liftweb.json.DefaultFormats
import net.liftweb.json.Formats
import net.liftweb.json.Serializer
import java.util.concurrent.atomic.AtomicInteger

object TodosPlan extends Plan {
  
  val idseq = new AtomicInteger(1)
  var todos = Map.empty[Int, Todo]
  
  def intent = {
    case req @ Path("/todos") => req match {
	  case GET(_) =>
	    Json(decompose(todos.values))
	  case POST(_) => synchronized {
	    val id = idseq.getAndIncrement
	    todos = todos + (id -> read[Todo](Body.string(req)).copy(id = id))
	    Created
	  }
    }
    case req @ Path(Seg("todos" :: id :: Nil)) => req match {
	  case GET(_) =>
	    todos.get(id.toInt).map(decompose).map(Json(_)) getOrElse NotFound
	  case PUT(_) =>
	    todos = todos + (id.toInt -> read[Todo](Body.string(req)).copy(id = id.toInt))
	    Ok
	  case DELETE(_) => synchronized {
	    todos = todos - id.toInt
	    Ok
	  }
	}
	case _ => BadRequest
  }
  
  implicit val formats: Formats = DefaultFormats + new Serializer[Todo] {
    def serialize(implicit format: Formats) = {
      case t: Todo => JObject(
          JField("id", JInt(t.id)) ::
          JField("title", JString(t.title)) ::
          JField("completed", JBool(t.completed)) :: Nil)
    }
    def deserialize(implicit format: Formats) = {
      case (_, JObject(
          JField(_, JString(t)) ::
          JField(_, JBool(c)) :: _)) => Todo(0, t, c) 
    }
  }
  
  case class Todo(id: Int, title: String, completed: Boolean)
}  