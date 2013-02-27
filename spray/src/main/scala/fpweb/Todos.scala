package fpweb

import spray.routing.SimpleRoutingApp
import spray.httpx.marshalling.Marshaller
import spray.httpx.unmarshalling.Unmarshaller
import spray.http._
import spray.http.StatusCodes._
import spray.http.MediaRanges._
import spray.http.ContentTypeRange._
import spray.http.ContentType._
import spray.json._
import spray.util.UTF8
import java.util.concurrent.atomic.AtomicInteger

object Todos extends App with SimpleRoutingApp with TodoJsonMarshalling {
  
  val idseq = new AtomicInteger(1)
  var todos = Map.empty[Int, Todo]
  
  startServer(interface = "localhost", port = 8080) {
    path("todos") {
      get {
        produce(instanceOf[Iterable[Todo]]) { complete => ctx =>
          complete(todos.values)
        }
      } ~
      post {
        entity(as[Todo]) { t =>
          val id = idseq.getAndIncrement
          todos = todos + (id -> t.copy(id = id))
          complete(Created)
        }
      }
    } ~
    path("todos" / IntNumber) { id =>
      get {
        todos.get(id) map { t =>
          produce(instanceOf[Todo]) { complete => ctx =>
            complete(t)
          }
        } getOrElse complete(NotFound)
      } ~
      put {
        entity(as[Todo]) { t =>
          todos = todos + (id -> t.copy(id = id))
          complete(OK)
        }
      } ~
      delete {
        todos = todos - id
        complete(OK)
      }
    }
  }
}

trait TodoJsonMarshalling extends DefaultJsonProtocol {
  
  implicit val format: JsonFormat[Todo] = jsonFormat3(Todo)
  
  implicit val tsm: Marshaller[Iterable[Todo]] = Marshaller.of[Iterable[Todo]](`application/json`) {(ts, contentType, ctx) =>
    ctx.marshalTo(HttpBody(contentType, ts.toJson.compactPrint.getBytes(UTF8)))
  }
  
  implicit val tm: Marshaller[Todo] = Marshaller.of[Todo](`application/json`) { (todo, contentType, ctx) =>
    ctx.marshalTo(HttpBody(contentType, todo.toJson.compactPrint.getBytes(UTF8)))
  }
  
  implicit val tum: Unmarshaller[Todo] = Unmarshaller[Todo](`application/*`) { 
    case HttpBody(contentType, buffer) =>
      new String(buffer, UTF8).asJson.convertTo[Todo]
  }
}

case class Todo(id: Int, title: String, completed: Boolean)

