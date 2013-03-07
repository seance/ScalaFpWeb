package controllers

import utils._
import models._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import java.util.concurrent.atomic.AtomicInteger

object Todos extends Controller with JsonHelper with CorsHelper {
    
  val idseq = new AtomicInteger(1)
  var todos = Map.empty[Int, Todo]
  
  def list = CorsAction {
    Ok.json(todos.values)
  }
  
  def create = CorsAction(parse.json) { req =>
    val id = idseq.getAndIncrement
    todos = todos + (id -> req.body.as[Todo].copy(id = id))
    Created
  }
  
  def get(id: Int) = CorsAction {
    todos.get(id).map(Ok.json(_)) getOrElse NotFound
  }
  
  def update(id: Int) = CorsAction(parse.json) { req =>
    todos = todos + (id -> req.body.as[Todo].copy(id = id))
    Ok
  }
  
  def delete(id: Int) = CorsAction {
    todos = todos - id
    Ok
  }
}