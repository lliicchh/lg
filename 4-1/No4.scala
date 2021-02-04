import akka.actor.{Actor, ActorRef, ActorSystem, Props}


case class StartMessage(msg: String)

case class EndMessage(msg: String)

case class AMessage(msg: String)

case class BMessage(msg: String)

object No4 {

  class AActor extends Actor {
    var count = 0

    override def receive: Receive = {
      case BMessage(msg) => {
        count += 1
        println("q-> " + msg)
        sender ! AMessage(s"(h):厉害，看我佛山无影脚...第 $count 脚")
      }
      case StartMessage(msg) => {
        count += 1
        println("h-> " + msg)
        sender ! AMessage(s"(h):看我佛山无影脚")
      }
      case EndMessage(msg) => {
        println(s"q: $msg")
        context.stop(self)
      }
    }
  }

  class BActor(h: ActorRef) extends Actor {
    var count = 0

    override def receive: Receive = {
      case AMessage(msg) => {
        if (count > 9) {
          sender ! EndMessage("ok,stop!!!")
          context.stop(self)
        } else {
          count += 1
          println("h-> " + msg)
          sender ! BMessage(s"(q):well,看我降龙十八掌...第 $count 掌")
        }
      }
      case StartMessage(msg) => {
        println("h-> " + msg)
        h ! StartMessage("h:在下h,看招!!!")
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("SysDemo")
    val q = system.actorOf(Props[AActor], "A")
    val h = system.actorOf(Props(new BActor(q)), "B")
    h ! StartMessage("q:在下q,请赐教")
  }
}
