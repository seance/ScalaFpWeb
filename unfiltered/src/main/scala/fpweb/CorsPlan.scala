package fpweb

import unfiltered.request._
import unfiltered.response._
import unfiltered.filter.Plan
import unfiltered.filter.Intent

/**
 * Provides CORS (Cross-Origin Resource Sharing) headers necessary for calling
 * the backend from the separately-served frontend, along with some basic HTTP
 * capabilities for extra requests made by real browsers.
 */
object CorsPlan extends Plan {
  
  def intent = {
    case req => corsIntent(req) ~> (browserIntent onPass todosIntent)(req)
  }
  
  val corsIntent = Intent {
    case req =>
      AllowOrigin("http://localhost:7000") ~>
      AllowHeaders("Origin, X-Requested-With, Content-Type, Accept") ~>
      AllowMethods("HEAD, GET, PUT, POST, DELETE, OPTIONS")
  }
  
  val browserIntent = Intent {
    case OPTIONS(_) =>
      Allow("HEAD, GET, PUT, POST, DELETE, OPTIONS") ~> Ok
    case Path("/favicon.ico") =>
      NotFound
  }
  
  val todosIntent = Intent(TodosPlan.intent)
  
  // Intent.complete composition
  // netty, cycle, async
  // async req.respond
  // kits
}

object AllowOrigin extends HeaderName("Access-Control-Allow-Origin")
object AllowHeaders extends HeaderName("Access-Control-Allow-Headers")
object AllowMethods extends HeaderName("Access-Control-Allow-Methods")
