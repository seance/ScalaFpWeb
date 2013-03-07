package fpweb

object Main {
  def main(args: Array[String]) = {
    unfiltered.jetty.Http.local(8080).filter(CorsPlan).run()
  }
}