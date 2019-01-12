package swords

final case class V2[A](x: A, y: A) {

  def +(rhs: V2[A])(implicit num: Numeric[A]): V2[A] =
    V2(num.plus(x, rhs.x), num.plus(y, rhs.y))

  def -(rhs: V2[A])(implicit num: Numeric[A]): V2[A] =
    V2(num.minus(x, rhs.x), num.minus(y, rhs.y))

  def *(rhs: V2[A])(implicit num: Numeric[A]): V2[A] =
    V2(num.times(x, rhs.x), num.times(y, rhs.y))

  def max(implicit ord: Ordering[A]): A =
    ord.max(x, y)

  def maxDistance(rhs: V2[A])(implicit num: Numeric[A], ord: Ordering[A]): A =
    (this - rhs).map(num.abs).max

  def map[B](f: A => B): V2[B] =
    V2(f(x), f(y))

}
