object No1 {
  def main(args: Array[String]): Unit = {
    println(bears(100))

  }

  def bears(money: Int): Int = {
    val num = money / 2
    num + exchanger(num)
  }

  var capNum, emptyNum = 0

  def exchanger(beers: Int): Int = {
    capNum += beers
    emptyNum += beers

    if (capNum < 5 && emptyNum < 3) return 0

    var empty, cap = 0
    if (emptyNum >= 3) {
      empty = emptyNum / 3
      emptyNum %= 3
    }
    if (capNum >= 5) {
      cap = capNum / 5
      capNum %= 5
    }
    cap + empty + exchanger(cap + empty)
  }
}
