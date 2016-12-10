import AnalyzerActor.ComparisonActor
import akka.actor.{Actor, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.stream.ActorMaterializer
import akka.util.ByteString
import play.api.libs.json.Json

import sys.process._
import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by samrudhinayak on 10/31/16.
  */
object DownloaderActor{

  class ClonerActor extends Actor{
    implicit val materializer = ActorMaterializer()
    implicit val system = ActorSystem("SimpleActor")
    val actors = system.actorOf(Props[ComparisonActor], "ComparisonActor")
    //receive string to URL
    def receive = {
      case projectsURL: String => {
        import system.dispatcher
        implicit val system = ActorSystem("SimpleActor")
        val actors = system.actorOf(Props[ComparisonActor], "ComparisonActor")
        val projectsCloneURL = scala.collection.mutable.MutableList[String]()
        val projectsURLs = scala.collection.mutable.MutableList[String]()
        projectsURLs += projectsURL
        var cloneURLtmp : String = null
        var projectFullName : String = null
        var cloneGitHubStr : String = null
        var j=0
        //iterate through each path in URL
        for (path <- projectsURLs) {
          val responseFuture2 = Http().singleRequest(HttpRequest(uri = path))
          val response2 = Await.result(responseFuture2, Duration.Inf)
          val p2 = response2.entity.dataBytes.runFold(ByteString.empty)(_ ++ _).map(_.utf8String)
          for(m2<-p2)
          {
          //parse through json object
            val projectJS = Json.parse(m2)
            projectFullName=projectJS.\("full_name").toString()
            projectFullName = projectFullName.replace("\"", "");
            //get clone_url from json object
            cloneURLtmp = projectJS.\("clone_url").toString()
            cloneURLtmp = cloneURLtmp.replace("\"", "");

            cloneGitHubStr = "git clone " + cloneURLtmp + " repo_projects/" + projectFullName
            val Array(n1, n2, _*) = projectFullName.split("/")
            var repostring = "repo_projects/" + n1
            //clone into given directory
            parsing(repostring, cloneGitHubStr)   
          }
        }
      }
      case i:Int => println("Integer : " +i)

    }


    def parsing(repostring: String, cloneGitHubStr: String) = {
      //run command to clone application
      val yy =  cloneGitHubStr !!;

      println("repoString in download parsing = " + repostring)
      //send the location of the cloned application to the AnalyzerActor
      actors ! repostring
    }
  }

}
