package fpweb

import org.scalatra._
import net.liftweb.json._
import net.liftweb.json.Serialization._
import java.util.concurrent.atomic.AtomicInteger

class TodosServlet extends ScalatraServlet with CorsHelpers with JsonHelpers {
  
  val idseq = new AtomicInteger(1)
  var todos = Map.empty[Int, Todo]
  
  get("/todos") {
    Ok(Json(todos.values), headers = cors)
  }
  
  post("/todos") {
    val id = idseq.getAndIncrement
    todos = todos + (id -> read[Todo](request.body).copy(id = id))
    Created(headers = cors)
  }
  
  get("/todos/:id") {
    todos.get(params("id").toInt)
    	.map(todo => Ok(Json(todo), headers = cors))
    	.getOrElse(NotFound(headers = cors))
  }
  
  put("/todos/:id") {
    val id = params("id").toInt
    todos = todos + (id -> read[Todo](request.body).copy(id = id))
    Ok(headers = cors)
  }
  
  delete("/todos/:id") {
    todos = todos - params("id").toInt
    Ok(headers = cors)
  }

  notFound {
    BadRequest(headers = cors)
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
