
import akka.actor.Actor
import akka.stream.ActorMaterializer

import java.io._

object AnalyzerActor {

  class ComparisonActor extends Actor {
    implicit val materializer = ActorMaterializer()
    def receive = {
      case repoString: String => {
        var visitor=new Visitor()
        var inputFile= new File(repoString)
        TestParser.listFilesForFolder(inputFile,visitor)
        println("HERE  " +visitor.getoperators())
      }
    }
  }
}