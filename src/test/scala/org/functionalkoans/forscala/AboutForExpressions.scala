package org.functionalkoans.forscala

import support.KoanSuite

class AboutForExpressions extends KoanSuite {

  koan("For loops can be simple") {
    val someNumbers = Range(0, 10)
    var sum = 0
    for (i <- someNumbers)
      sum += i

    sum should equal(45)
    sum should equal(Range(0, 10).sum)
    sum should equal(Range(0, 10).reduce(_ + _))
    sum should equal(Range(0, 10).foldLeft(0)(_ + _))
    sum should equal(Range(0, 10).foldRight(0)(_ + _))
  }

  koan("For loops can contain additional logic") {
    val someNumbers = Range(0, 10)
    var sum = 0
    // sum only the even numbers
    for (i <- someNumbers)
      if (i % 2 == 0) sum += i

    sum should equal(20)
    sum should equal(Range(0, 10, 2).sum)
    sum should equal(Range(0, 10).filter(_ % 2 == 0).sum)
    sum should equal(Range(0, 10).groupBy(_ % 2 == 0).get(true).get.sum) // safer
    sum should equal(Range(0, 10).groupBy(_ % 2 == 0)(true).sum) // items may not be present - not safe!
  }
  
  koan("For expressions can nest, with later generators varying more rapidly than earlier ones") {
    val xValues = Range(1, 5)
    val yValues = Range(1, 3)
    val coordinates = for {
      x <- xValues
      y <- yValues
    } yield (x, y)

    coordinates.isInstanceOf[Seq[Tuple2[Int, Int]]] should be(true)
    coordinates.size should be(4 * 2)
    coordinates(0) should be((1, 1))
    coordinates(4) should be(3, 1)
    coordinates(5) should be(3, 2)
    coordinates(6) should be(4, 1)
  }
}
