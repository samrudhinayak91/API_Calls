import DownloaderActor.ClonerActor
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.stream.ActorMaterializer
import akka.util.ByteString
import play.api.libs.json.Json

import sys.process._
import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by samrudhinayak on 11/8/16.
  */
class Request{

  def fetchKeyword(keyword:String) = {
      implicit val system = ActorSystem("MainActor")
      implicit val materializer = ActorMaterializer()

      //  User specify the language and the keyword
      // Java is hardcoded here as a language
      // Keyword is taken from the GUI
      val lang = "java";
      println("Language= " + lang)
      println("keyword= " + keyword)

      //build the search path from which we can look for applications
      var searchPath = "https://api.github.com/legacy/repos/search/" + keyword + "?language=" + lang
      println("searchPath=" + searchPath)

      //create actor to which messages are to be sent
      val actors = system.actorOf(Props[ClonerActor], "ComplexActor")

      //akka Http request to get details of list of repos containing applications matching the language and keyword
      val responseFuture = Http().singleRequest(HttpRequest(uri = searchPath))
      import system.dispatcher

      //wait for the request to fetch results, time duration of the wait will be for as long as the request takes to fetch results
      val response = Await.result(responseFuture, Duration.Inf)

      //convert request to list of strings
      val p = response.entity.dataBytes.runFold(ByteString.empty)(_ ++ _).map(_.utf8String)
      var i = 0;

      //iterate through the list to get each of the URLs
      for (m <- p) {
        i += 1
        //Convert to Json object
        val obj = Json.parse(m)
        var tempstr: String = null
        var username: String = null
        var name: String = null

        //find first n applications matching the keywords
        // we hardcoded n=5
        for (a <- 0 to 6) {
          //build tempstring with the URL off each application along with their username and name
          username = obj.\("repositories")(a).\("username").toString()
          name = obj.\("repositories")(a).\("name").toString()
          username = username.replace("\"", "");
          name = name.replace("\"", "");
          tempstr = "https://api.github.com/repos/" + username + "/" + name
          val projectsURL = "https://api.github.com/repos/" + username + "/" + name
          //send this as a string to the ClonerActor within the DownloaderActor
          actors ! tempstr
        }
      }
    }
  }

