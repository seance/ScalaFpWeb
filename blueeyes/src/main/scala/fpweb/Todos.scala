package fpweb

import akka.dispatch.Future
import blueeyes.BlueEyesServiceBuilder
import blueeyes.BlueEyesServer
import blueeyes.core.http.HttpRequest
import blueeyes.core.http.HttpResponse
import blueeyes.core.http.HttpStatus
import blueeyes.core.data.ByteChunk
import blueeyes.core.data.BijectionsChunkString._

trait TodosService extends BlueEyesServiceBuilder {
    
  val todosService = service("Todos", "0.1.0-SNAPSHOT") { context =>
    request {
      path("/todos") {
        get { request: HttpRequest[ByteChunk] =>
          Future {
	        HttpResponse[ByteChunk](content = Some("GET /todos"))
          }
        } ~
        post { request: HttpRequest[ByteChunk] =>
          Future {
            HttpResponse[ByteChunk](content = Some("POST /todos"))
          }
        }
      } ~
      path("/todos" / 'id) {
        get { request: HttpRequest[ByteChunk] =>
          Future {
	        HttpResponse[ByteChunk](content = Some("GET /todos/%s" format parameter('id)))
          }
        }
      }
    }
  }
}

object TodosServer extends BlueEyesServer with TodosService