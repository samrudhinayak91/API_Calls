import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.junit.{Assert, Test}

/**
  * Created by ahmedmetwally on 12/9/16.
  */
class RequestTest extends TestKit(ActorSystem("TestActor")) {

  @Test
  def test1 {
    Assert.assertEquals(1, 1)
  }


}
