case class UserInfo(
                     userName: String,
                     location: String,
                     startTime: Int,
                     duration: Int
                   )

object No3 {
  val userInfoLst: List[UserInfo] = List(
    UserInfo("userA", "locationA", 8, 60),
    UserInfo("userA", "locationA", 9, 60),
    UserInfo("userB", "locationB", 11, 80),
    UserInfo("userB", "locationB", 10, 60)
  )

  def main(args: Array[String]): Unit = {
    val groupBy: Map[String, List[UserInfo]] = userInfoLst.groupBy(
      user => user.userName + ":" + user.location
    )

    val sort: Map[String, List[UserInfo]] = groupBy.mapValues(
      _.sortBy(_.startTime)
    )

    var firstTime = 0
    val sum: Map[String, Int] = sort.mapValues(lst => {
      firstTime = lst.head.startTime
      lst.map(_.duration).sum
    })

    sum.foreach {
      case (k, v) => println(s"$k,$firstTime,$v")
    }
  }
}
