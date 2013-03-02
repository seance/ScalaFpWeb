package code.lib

import code.model.Todo
import net.liftweb.http._
import net.liftweb.http.LiftRules.DispatchPF
import net.liftweb.http.rest._
import net.liftweb.common._
import net.liftweb.json.Extraction._
import java.util.concurrent.atomic.AtomicInteger

object Todos extends RestHelper with TodosHelper {
  
  val idseq = new AtomicInteger(1)
  var todos = Map.empty[Int, Todo]
  
  def todosPF: DispatchPF = {
    case JsonGet(Nil, _) =>
      decompose(todos.values)
      
    case JsonPost(Nil, (jv, _)) =>
      val id = idseq.getAndIncrement
      todos = todos + (id -> jv.extract[Todo].copy(id = id))
      created
      
    case JsonGet(id :: Nil, _) =>
      todos.get(id.toInt).map(decompose)
      
    case JsonPut(id :: Nil, (jv, _)) =>
      todos = todos + (id.toInt -> jv.extract[Todo].copy(id = id.toInt))
      ok
      
    case JsonDelete(id :: Nil, _) =>
      todos = todos - id.toInt
      ok
  }
  
  def corsPF(f: DispatchPF): DispatchPF = {
    f andThen { case r => r().map(r => r.copy(headers = r.headers ++ List(
        "Access-Control-Allow-Origin" -> "http://localhost:7000",
        "Access-Control-Allow-Headers" -> "Origin, X-Requested-With, Content-Type, Accept",
        "Access-Control-Allow-Methods" -> "HEAD, GET, PUT, POST, DELETE, OPTIONS")))
    }
  }
  
  def browserPF: DispatchPF = {
    case Get("favicon" :: Nil, _) =>
      notFound
      
    case req if req.options_? =>
      ok.copy(headers = List("Allow" -> "HEAD, GET, PUT, POST, DELETE, OPTIONS"))
  }

  serve(corsPF(browserPF))
  serve("todos" :: Nil prefix corsPF(todosPF))
}

trait TodosHelper {
  
  implicit def augmentReq(req: Req) = new {
    def options_? = req.requestType.method.toLowerCase == "options"
  }
  
  implicit def toJsonResponse(r: LiftResponse): JsonResponse = r match {
    case r: JsonResponse => r
  }

  def ok = JsonResponse("Ok")
  def created = JsonResponse("Created").copy(code = 201)
  def notFound = JsonResponse("Not Found").copy(code = 404)
}