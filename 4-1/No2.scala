
import scala.io.StdIn
import scala.util.Random

object No2 extends App {

  val players = Map("1" -> "刘备", "2" -> "关羽", "3" -> "张飞")
  val choices = Map("1" -> "石头", "2" -> "剪刀", "3" -> "布")

  var playerA: User = _
  var playerB: Computer = _
  var times = 0

  play()

  def init(): Unit = {
    println("--------welcome--------")
    playerA = new User("游客", 0)
    println("请选择对战角色：（1.刘备 2.关羽 3.张飞）")
    val key = StdIn.readLine()
    var role: String = null
    if (key.toInt > 3) {
      println(s"$key 无效，默认选择关羽")
      role = players("2")
    } else {
      role = players(key)
    }


    playerB = new Computer(role, 0)
  }

  def judge(a: String, b: String): Unit = {
    val fair = a == b
    if (fair) {
      playerA.score += 1
      playerA.fairs += 1
      playerB.score += 1
      playerB.fairs += 1
      println("结果，和局！下次继续努力！")
    } else {
      val win = (a == "1" && b == "2") || (a == "2" && b == "3") || (a == "3" && b == "1")
      if (win) {
        // 玩家赢
        playerA.score += 2
        playerA.wins += 1
        playerB.fails += 1
        println("恭喜！你赢啦！")
      } else {
        // 玩家输
        playerB.score += 2
        playerB.wins += 1
        playerA.fails += 1
        println("抱歉！你输了！")
      }
    }
  }

  def showResults(): Unit = {
    println(s"${playerB.name}\tVS\t${playerA.name}")
    println(s"对战次数${times}次\n\n")
    println("姓名\t得分\t胜局\t和局\t负局")
    println(s"${playerA.name}\t${playerA.score}\t${playerA.wins}\t${playerA.fairs}\t${playerA.fails}")
    println(s"${playerB.name}\t${playerB.score}\t${playerB.wins}\t${playerB.fairs}\t${playerB.fails}")
  }

  def play(): Unit = {
    init()
    println(s"你选择了与${playerB.name}对战\n要开始么？y/n")
    var next = true
    var s = StdIn.readLine()
    do {
      s match {
        case "y" =>
          println("请出拳！1.石头 2.剪刀 3.布")
          var a = playerA.showFist()
          println(s"${playerB.name}出拳！")
          var b = playerB.showFist()
          judge(a, b)
          times += 1
          println("是否开始下一轮？（y/n）")
          s = StdIn.readLine()
        case "n" =>
          next = false
          println("退出游戏！")
          showResults()
        case _ =>
          println("是否开始下一轮？（y/n）")
          s = StdIn.readLine()
      }
    } while (next)
  }

  abstract class Player() {
    var wins = 0
    var fails = 0
    var fairs = 0

    def showFist(): String
  }

  class Computer(var name: String, var score: Int) extends Player {
    def showFist(): String = {
      val b = Random.nextInt(3) + 1
      println(s"${playerB.name}出拳：${choices(b.toString)}")
      b.toString
    }
  }

  class User(var name: String, var score: Int) extends Player {
    def showFist(): String = {
      var a = StdIn.readLine()
      if (!choices.keySet.contains(a)) {
        println("输入不符合规范，默认出布！")
        a = "3"
      }
      println(s"你出拳：${choices(a)}")
      a
    }
  }

}