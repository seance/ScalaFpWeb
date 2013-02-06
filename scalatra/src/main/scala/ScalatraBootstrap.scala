import fpweb._
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    try {
      context.mount(new TodosServlet, "/*")
    } catch {
      case t: Throwable => t.printStackTrace()
    }
  }
}
