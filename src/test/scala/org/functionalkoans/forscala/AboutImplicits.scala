package org.functionalkoans.forscala

import org.scalatest.matchers.ShouldMatchers
import language.implicitConversions
import support.KoanSuite

class AboutImplicits extends KoanSuite with ShouldMatchers {

  koan("""Implicits wrap around existing classes to provide extra functionality
           |   This is similar to \'monkey patching\' in Ruby, and Meta-Programming in Groovy.
           |   Creating a method isOdd for Int, which doesn't exist""") {

    class KoanIntWrapper(val original: Int) {
      def isOdd = original % 2 != 0
    }

    implicit def thisMethodNameIsIrrelevant(value: Int) = new KoanIntWrapper(value)

    19.isOdd should be(true)
    20.isOdd should be(false)
  }

  koan("""Implicits rules can be imported into your scope with an import""") {
    object MyPredef {

      class KoanIntWrapper(val original: Int) {
        def isOdd = original % 2 != 0

        def isEven = !isOdd
      }

      implicit def thisMethodNameIsIrrelevant(value: Int) = new KoanIntWrapper(value)
    }

    import MyPredef._
    //imported implicits come into effect within this scope
    19.isOdd should be(true)
    20.isOdd should be(false)
  }

  koan("""Implicits can be used to automatically convert one type to another""") {

    import java.math.BigInteger
    implicit def Int2BigIntegerConvert(value: Int): BigInteger = new BigInteger(value.toString)

    def add(a: BigInteger, b: BigInteger): BigInteger = a.add(b)

    add(3, 6) should be(BigInteger.valueOf(9))
  }

  koan("""Implicits can be used declare a value to be provided as a default as
          |   long as an implicit value is set with in the scope.  These are
          |   called implicit function parameters""") {

    def howMuchCanIMake_?(hours: Int)(implicit dollarsPerHour: BigDecimal) = dollarsPerHour * hours

    implicit var hourlyRate = BigDecimal(34.00)

    howMuchCanIMake_?(30) should be(34.00 * 30)

    hourlyRate = BigDecimal(95.00)
    howMuchCanIMake_?(95) should be(95.00 * 95)
  }

  koan("""Implicit Function Parameters can contain a list of implicits""") {

    def howMuchCanIMake_?(hours: Int)(implicit amount: BigDecimal, currencyName: String) =
      (amount * hours).toString() + " " + currencyName

    implicit var hourlyRate = BigDecimal(34.00)
    implicit var currencyName = "Dollars"

    howMuchCanIMake_?(30) should be(s"${30 * 34.00} Dollars")

    hourlyRate = BigDecimal(95.00)
    currencyName = "Pounds"
    howMuchCanIMake_?(95) should be(s"${95 * 95.00} Pounds")
  }

  koan("""Default arguments though are preferred to Implicit Function Parameters""") {

    def howMuchCanIMake_?(hours: Int, amount: BigDecimal = 34, currencyName: String = "Dollars") =
      (amount * hours).toString() + " " + currencyName

    howMuchCanIMake_?(30) should be(s"${30 * 34} Dollars")

    howMuchCanIMake_?(95, 95) should be(s"${95 * 95} Dollars")
  }
}