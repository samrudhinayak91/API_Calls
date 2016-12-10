import akka.actor.Actor
import akka.stream.ActorMaterializer
import java.io._
import java.nio.file.{Files, Paths}

import scala.sys.process.Process

object AnalyzerActor {
  class ComparisonActor extends Actor {
    implicit val materializer = ActorMaterializer()
    def receive = {
      case repoString: String => {
        val visitor = analyseRepo(repoString);
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



    // Write statistics to file
    def writeToFile (repoString : String, visitor : Visitor) = {

      val fdir = repoString + "/Analysis";

      // Check if the file exists or not. Delete if it exists
      if(Files.exists(Paths.get(fdir)))
      {
        var newVersion=  Process(Seq("rm","-r", fdir))!!;
      }
      var ret =  Process(Seq("mkdir", fdir))!!;
      val temp_write_file = new PrintWriter(new File(repoString + "/Analysis" +"/output.txt"))


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