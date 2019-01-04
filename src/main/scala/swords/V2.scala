package swords

final case class V2[A](x: A, y: A) {

  def +(rhs: V2[A])(implicit num: Numeric[A]): V2[A] =
    V2(num.plus(x, rhs.x), num.plus(y, rhs.y))

  def *(rhs: V2[A])(implicit num: Numeric[A]): V2[A] =
    V2(num.times(x, rhs.x), num.times(y, rhs.y))

}
