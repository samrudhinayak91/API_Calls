
import akka.actor.Actor
import akka.stream.ActorMaterializer
import java.io._

import scala.sys.process.Process

object AnalyzerActor {

  class ComparisonActor extends Actor {
    implicit val materializer = ActorMaterializer()
    def receive = {
      case repoString: String => {
        var visitor=new Visitor()
        var inputFile= new File(repoString)
        TestParser.listFilesForFolder(inputFile,visitor)
        val fdir = repoString + "/Analysis";
        var newVersion=  Process(Seq("mkdir", fdir))!!;
        val temp_write_file = new PrintWriter(new File(repoString + "/Analysis" +"/output.txt"))
        temp_write_file.write("Operators " + visitor.getoperators().toString() + "\n")
        //println("HERE  " +visitor.getoperators())
        temp_write_file.close()
        println("Closed and written")
      }
    }
  }
}