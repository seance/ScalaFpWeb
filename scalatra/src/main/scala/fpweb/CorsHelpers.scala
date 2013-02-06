package fpweb

import org.scalatra._

trait CorsHelpers extends ScalatraBase {
  val cors = Map(
      "Access-Control-Allow-Origin" -> "http://localhost:7000",
      "Access-Control-Allow-Headers" -> "Origin, X-Requested-With, Content-Type, Accept",
      "Access-Control-Allow-Methods" -> "HEAD, GET, PUT, POST, DELETE, OPTIONS")
  
  options("*") {
    contentType = null
    Ok(headers = Map("Allow" -> "HEAD, GET, PUT, POST, DELETE, OPTIONS") ++ cors)
  }
  
  get("/favicon.ico") {
    NotFound(headers = cors)
  }
}