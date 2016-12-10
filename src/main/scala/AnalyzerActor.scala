import akka.actor.Actor
import akka.stream.ActorMaterializer
import java.io._
import scala.sys.process.Process

object AnalyzerActor {
  class ComparisonActor extends Actor {
    implicit val materializer = ActorMaterializer()
    def receive = {
      case repoString: String => {
        println("before analysing in reposString")
        val visitor = analyseRepo(repoString);
        println("analysing in reposString")
        writeToFile (repoString : String, visitor : Visitor);
      }
    }

    // Create AST of the repo
    def analyseRepo (repoString : String): Visitor = {
      var visitor = new Visitor ()
      var inputFile = new File (repoString)
      TestParser.listFilesForFolder (inputFile, visitor)
      return visitor;
    }

//    val fdir = repoString + "/Analysis";
//    var newVersion=  Process(Seq("mkdir", fdir))!!;
//    val temp_write_file = new PrintWriter(new File(repoString + "/Analysis" +"/output.txt"))




    // Write statistics to file
    def writeToFile (repoString : String, visitor : Visitor) = {

      val fdir = repoString + "/Analysis";
      var newVersion=  Process(Seq("mkdir", fdir))!!;
      val temp_write_file = new PrintWriter(new File(repoString + "/Analysis" +"/output.txt"))


//      val temp_write_file = new PrintWriter (new File (repoString + "_statistics.txt") )
      temp_write_file.write ("Methods invoked : " + visitor.getSummer.toString + "\n")
      temp_write_file.write ("Methods from java.util, java.io and java.lang : " + visitor.getoperators ().toString () + "\n")
      var summer = visitor.getSummer ()
      var ops = visitor.getoperators ()
      var writ = (ops / summer) * 100
      temp_write_file.write ("Percentage of method invoked from the packages are : " + writ + "% \n")
      temp_write_file.close ()
      println ("Written and closed")
    }

  }
}