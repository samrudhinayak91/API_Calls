import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestKit}
import org.junit.{Assert, Test}

/**
  * Created by ahmedmetwally on 12/9/16.
  */

class StatiticsCorrectnessTest extends TestKit(ActorSystem("TestActor")) {
  val actorRef = TestActorRef[AnalyzerActor.ComparisonActor]
  val actor = actorRef.underlyingActor

  @Test
  // Checks the correctness of the extracted statistics
  def testAnalyseRepo {
    val vis = actor.analyseRepo("../../datasets/square/okio")
    var summer = vis.getSummer()
    var ops = vis.getoperators()
    var writ = (ops / summer) * 100

    println("summer = " + summer.toInt)
    println("ops = " + ops.toInt)
    println("writ = " + writ.round)

    Assert.assertEquals(summer.toInt, 7138)
    Assert.assertEquals(ops.toInt, 2670)
    Assert.assertEquals(writ.round, 37)
  }
}