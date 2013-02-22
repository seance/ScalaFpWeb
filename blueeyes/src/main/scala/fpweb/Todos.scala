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

trait TodosService extends BlueEyesServiceBuilder {
  
  val todos = Map.empty[Int, String]
    
  val todosService = service("Todos", "0.1.0-SNAPSHOT") { context =>
    request {
      cors {
        browser ~
        path("/todos") {
          produce(application/json) {
            get { request: HttpRequest[ByteChunk] =>
              chunk(JArray(Nil): JValue)
            }
          } ~
          jvalue {
            post { request: HttpRequest[Future[JValue]] =>
              request.content.map {
                _.flatMap(chunkJson)
              } getOrElse {
                chunkJson(JNothing) // BadRequest
              }
            }
          }
        } ~
        path("/todos" / 'id) {
          get { request: HttpRequest[ByteChunk] =>
            chunk("GET /todos/%s" format request.parameters('id))  
          }
        }
      }
    }
  }
  
  def chunk[A](content: A, status: Status = OK)(implicit b: Bijection[A, ByteChunk]) = {
    Future(HttpResponse[ByteChunk](content = Some(content), status = status))
  }

  def chunkJson(content: JValue) = {
    Future(HttpResponse[JValue](content = Some(content)))
  }
  
  def browser = {
    path("/favicon.ico") {
      get { request: HttpRequest[ByteChunk] =>
        chunk("", NotFound)
      }
    } ~
    path("(.*)") {
      options { request: HttpRequest[ByteChunk] =>
        chunk("HEAD GET POST PUT DELETE OPTIONS")
      }
    }
  }
  
  def cors(s: HttpService[ByteChunk, Future[HttpResponse[ByteChunk]]]) = {
    s.map(_.map(_.copy(headers = HttpHeaders(
        `Access-Control-Allow-Origin`("http://localhost:7000") :: Nil))))
//        `Access-Control-Allow-Headers`(`X-Requested-With`, `Content-Type`, Accept) ::
//        `Access-Control-Allow-Methods`(HEAD, GET, PUT, POST, DELETE, OPTIONS) :: Nil))))
  }
}

object TodosServer extends BlueEyesServer with TodosService