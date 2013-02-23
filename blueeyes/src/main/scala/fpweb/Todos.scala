package fpweb

import akka.dispatch.Future
import blueeyes.BlueEyesServiceBuilder
import blueeyes.BlueEyesServer
import blueeyes.core.http.{HttpStatusCode => Status, _}
import blueeyes.core.http.HttpStatusCodes._
import blueeyes.core.http.MimeTypes._
import blueeyes.core.http.HttpHeaders._
import blueeyes.core.http.HttpMethods._
import blueeyes.core.data._
import blueeyes.core.data.BijectionsChunkString._
import blueeyes.core.data.BijectionsChunkJson._
import blueeyes.core.data.BijectionsChunkFutureJson._
import blueeyes.core.service._
import blueeyes.json._
import blueeyes.json.JsonAST._
import java.util.concurrent.atomic.AtomicInteger

trait TodosService extends BlueEyesServiceBuilder {
  
  val idseq = new AtomicInteger(1)
  var todos = Map.empty[Int, Todo]
  
  val todosService = service("Todos", "0.1.0-SNAPSHOT") { context =>
    request {
      allowCors {
        browser ~
        path("/todos") {
          produce(application/json) {
            get { request: HttpRequest[ByteChunk] =>
              jval(Todo.toJsonArray(todos.values))
            }
          } ~
          jvalue {
            post { request: HttpRequest[Future[JValue]] =>
              request.content.map {
                _.flatMap(v => Todo.fromJson(v).map { t =>
                  val id = idseq.getAndIncrement
                  todos = todos + (id -> t.copy(id = id))
                  status(Created)
                } getOrElse status(NotFound))
              } getOrElse status(BadRequest)
            }
          }
        } ~
        path("/todos" / 'id) {
          produce(application/json) {
            get { request: HttpRequest[ByteChunk] =>
              todos.get(request.parameters('id).toInt).map { t =>
                jval(Todo.toJson(t))
              } getOrElse status(NotFound)
            }
          } ~
          accept(application/json) {
            put { request: HttpRequest[Future[JValue]] =>
              val id = request.parameters('id).toInt
              request.content.map {
                _.flatMap(v => Todo.fromJson(v).map { t=>
                  todos = todos + (id -> t.copy(id = id))
                  status(OK)
                } getOrElse status(BadRequest))
              } getOrElse status[ByteChunk](BadRequest)
            }
          } ~
          delete { request: HttpRequest[Future[JValue]] =>
            todos = todos - request.parameters('id).toInt
            status(OK)
          }
        }
      }
    }
  }
  
  def status[R](status: Status) = {
    Future(HttpResponse[R](status = status))
  }
  
  def chunk[A](content: A)(implicit b: Bijection[A, ByteChunk]) = {
    Future(HttpResponse[ByteChunk](content = Some(content)))
  }

  def jval(content: JValue) = {
    Future(HttpResponse[JValue](content = Some(content)))
  }
  
  def browser = {
    path("/favicon.ico") {
      get { request: HttpRequest[ByteChunk] =>
        status[ByteChunk](NotFound)
      }
    } ~
    path("(.*)") {
      options { request: HttpRequest[ByteChunk] =>
        chunk("HEAD GET POST PUT DELETE OPTIONS")
      }
    }
  }
  
  def allowCors(s: HttpService[ByteChunk, Future[HttpResponse[ByteChunk]]]) = {
    s.map(_.map(r => r.copy(headers = r.headers ++ HttpHeaders(
        `Access-Control-Allow-Origin`("http://localhost:7000") :: Nil))))
//        `Access-Control-Allow-Headers`(`X-Requested-With`, `Content-Type`, Accept) ::
//        `Access-Control-Allow-Methods`(HEAD, GET, PUT, POST, DELETE, OPTIONS) :: Nil))))
  }
}

case class Todo(id: Int, title: String, completed: Boolean)

object Todo {
  def toJson(t: Todo): JValue = JObject(
      JField("id", JInt(t.id)) ::
      JField("title", JString(t.title)) ::
      JField("completed", JBool(t.completed)) :: Nil)
      
  def toJsonArray(ts: Traversable[Todo]): JArray = JArray(ts.map(toJson).toList)
      
  def fromJson(v: JValue) = v match {
    case JObject(
        JField(_, JInt(_)) ::
        JField(_, JString(t)) ::
        JField(_, JBool(c)) :: Nil) => Some(Todo(0, t, c))
    case _ => None
  }
}

object TodosServer extends BlueEyesServer with TodosService