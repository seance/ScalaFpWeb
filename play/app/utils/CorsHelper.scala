package utils

import play.api.mvc._

trait CorsHelper { self: Controller =>
  def CorsAction(f: => Result): Action[AnyContent] =
    CorsAction(_ => f)
  
  def CorsAction(f: Request[AnyContent] => Result): Action[AnyContent] =
    CorsAction(BodyParsers.parse.anyContent)(f)
    
  def CorsAction[A](p: BodyParser[A])(f: Request[A] => Result) = Action(p) { req =>
    f(req).withHeaders(
        "Access-Control-Allow-Origin" -> "http://localhost:7000",
        "Access-Control-Allow-Headers" -> "Origin, X-Requested-With, Content-Type, Accept",
        "Access-Control-Allow-Methods" -> "HEAD, GET, PUT, POST, DELETE, OPTIONS")
  }
}