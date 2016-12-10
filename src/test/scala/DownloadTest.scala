import java.nio.file.{Files, Paths}

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestKit}
import org.junit.{Assert, Test}

/**
  * Created by ahmedmetwally on 12/9/16.
  */


class DownloadTest extends TestKit(ActorSystem("TestActor")) {
  val actorRef = TestActorRef[DownloaderActor.ClonerActor]
  val actor = actorRef.underlyingActor

  @Test
  // Checks the correctness of the extracted statistics
  def testAnalyseRepo: Unit = {
    actor.parsing("../../datasets/BenjaminSchiller", "git clone https://github.com/BenjaminSchiller/DNA.git ../../datasets/BenjaminSchiller/DNA")
    Assert.assertEquals(Files.exists(Paths.get("../../datasets/BenjaminSchiller/DNA")), true)

  }
}