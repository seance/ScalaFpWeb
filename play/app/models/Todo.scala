package models

import play.api.libs.json._

case class Todo(id: Int, title: String, completed: Boolean)

object Todo {
  
  implicit object Format extends Format[Todo] {
    def reads(json: JsValue): JsResult[Todo] = JsSuccess(Todo(
        (json \ "id").as[Int],
        (json \ "title").as[String],
        (json \ "completed").as[Boolean]))
        
    def writes(t: Todo) = Json.obj(
        "id" -> t.id,
        "title" -> t.title,
        "completed" -> t.completed)
  }
}