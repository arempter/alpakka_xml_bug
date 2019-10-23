import XmlHandler.exctractMultideleteObjectsFlow
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import scala.util.Success

object HttpServer extends App {
  implicit val system = ActorSystem("testHttpEntity")
  implicit val mat = ActorMaterializer()
  implicit val ec = system.dispatcher

  def route =
    post {
      extractRequest { request =>
        println("New Request" + request.headers)

        onComplete(exctractMultideleteObjectsFlow(request.entity.dataBytes)) {
          case Success(_)=> complete("Uploaded")
          case _ => complete("failed")
        }
      }
    }

  val binding = Http().bindAndHandle(route, "0.0.0.0", 8123)

  binding.onComplete{
    case Success(b) => println("Server started at " + b.localAddress)
  }

}
