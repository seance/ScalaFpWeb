package utils

import play.api.mvc._
import play.api.libs.json._

trait JsonHelper { self: Controller =>
  // cannot extend AnyVal in trait 
  implicit class StatusAsJson(s: Status) {
    def json[A: Writes](content: A) = {
      s(Json.stringify(Json.toJson(content))).as("application/json; charset=utf-8")
    }
  }
}