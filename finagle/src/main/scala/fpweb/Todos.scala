package fpweb

import com.twitter.util._
import com.twitter.finagle.Service
import com.twitter.finagle.http._
import com.twitter.finagle.http.path._
import com.twitter.finagle.builder._
import org.jboss.netty.handler.codec.http.HttpMethod._
import org.jboss.netty.handler.codec.http.HttpResponseStatus
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import org.jboss.netty.buffer.ChannelBuffers._
import org.jboss.netty.util.CharsetUtil._
import net.liftweb.json._
import net.liftweb.json.Serialization._
import net.liftweb.json.Extraction._
import java.net.InetSocketAddress
import java.util.concurrent.atomic.AtomicInteger

object Todos extends Service[Request, Response] {
  
  val idseq = new AtomicInteger(1)
  var todos = Map.empty[Int, Todo]
  
  def apply(request: Request) = {
    Path(request.path) match {
      case Root / "todos" => request.method match {
        case GET => 
          json(todos.values)
        case POST =>
          val id = idseq.getAndIncrement
          todos = todos + (id -> read[Todo](request.contentString).copy(id = id))
          status(CREATED)
      }
      case Root / "todos" / id => request.method match {
        case GET =>
          todos.get(id.toInt).map(json).getOrElse(status(NOT_FOUND))
        case PUT =>
          todos = todos + (id.toInt -> read[Todo](request.contentString).copy(id = id.toInt))
          status(OK)
        case DELETE =>
          todos = todos - id.toInt
          status(OK)
      } 
    }
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
}

object `package` {
    
  def status(code: HttpResponseStatus) = {
    val response = Response()
    response.setStatus(code)
    Future.value(response)
  }
  
  def json(v: Any)(implicit f: Formats) = {
    val response = Response()
    response.setContentTypeJson
    response.setContent(copiedBuffer(compact(render(decompose(v))), UTF_8))
    Future.value(response)
  }
}

case class Todo(id: Int, title: String, completed: Boolean)

object Main extends App {
  ServerBuilder()
  	.codec(RichHttp[Request](Http()))
  	.bindTo(new InetSocketAddress(8080))
  	.name("finagle-todo")
  	.build(Todos)
}