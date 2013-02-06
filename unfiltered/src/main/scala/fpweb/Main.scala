package fpweb

object Main {
  def main(args: Array[String]) = {
    unfiltered.jetty.Http.local(8000).filter(CorsPlan).run()
  }
}