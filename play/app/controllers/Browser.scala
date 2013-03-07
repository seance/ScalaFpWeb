package controllers

import utils._
import play.api.mvc._

object Browser extends Controller with CorsHelper {
  def favicon = CorsAction {
    NotFound
  }
  def options(any: String) = CorsAction {
    Ok.withHeaders("Allow" -> "HEAD, GET, PUT, POST, DELETE, OPTIONS")
  }
}