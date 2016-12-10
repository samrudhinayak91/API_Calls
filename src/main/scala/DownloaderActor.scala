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
            val projectJS = Json.parse(m2)
            projectFullName=projectJS.\("full_name").toString()
            projectFullName = projectFullName.replace("\"", "");
            cloneURLtmp = projectJS.\("clone_url").toString()
            cloneURLtmp = cloneURLtmp.replace("\"", "");

            cloneGitHubStr = "git clone " + cloneURLtmp + " repo_projects/" + projectFullName
            val Array(n1, n2, _*) = projectFullName.split("/")
            var repostring = "repo_projects/" + n1
            parsing(repostring, cloneGitHubStr)





//            //get tag url which is used to store versions
//            var tag_url = projectJS.\("tags_url").toString().replace("\"", "");
//            val responseFuture3 = Http().singleRequest(HttpRequest(uri = tag_url))
//            val response3 = Await.result(responseFuture3, Duration.Inf)
//            val temptag = response3.entity.dataBytes.runFold(ByteString.empty)(_ ++ _).map(_.utf8String)
//            for(s<-temptag)
//              {
//                val jtemp = Json.parse(s)
//                val tempstr = s.toString.length
//                //if tag url is empty and has only the [] then skip that file, else send it to the function that clones the application
//                if(tempstr != 0) {
//                  projectsCloneURL += cloneURLtmp
//                  cloneGitHubStr = "git clone " + cloneURLtmp + " repo_projects/" + projectFullName
//                  val Array(n1, n2, _*) = projectFullName.split("/")
//                  var repostring = "repo_projects/" + n1
//                  parsing(repostring, cloneGitHubStr)
//                }
//              }
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
