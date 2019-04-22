import annotation.tailrec

object Test {
  def foo(x: => Int) = 1
  def bar(x: Int => Int) = 1

  @tailrec def rec1: Int =
    foo(rec1) // error: not in tail position

  @tailrec def rec2: Int =
    bar(_ => rec2) // error: not in tail position

  @tailrec def rec3: Int =
    1 + (try ??? catch {
      case _: Throwable =>
        rec3 // error: not in tail position
    })

  @tailrec def rec4: Unit = {
    def local = rec4 // error: not in tail position
  }

  @tailrec def rec5: Int = {
    val x = {
      rec5 // error: not in tail position
      1
    }
    x
  }
}

object Test4649 {
  @tailrec
  def lazyFilter[E](s: Stream[E], p: E => Boolean): Stream[E] = s match {
    case h #:: t =>
      if (p(h)) h #:: lazyFilter(t, p) // error: not in tail position
      else lazyFilter(t, p)
    case _ =>
      Stream.empty
  }
}
