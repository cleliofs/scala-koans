package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSuite

class SecretAgent private(val name: String) {
  def shoot(n: Int) {
    SecretAgent.decrementBullets(n)
  }
}

object SecretAgent {
  //This is encapsulated!
  var bullets: Int = 3000

  def apply(name: String) = new SecretAgent(name)

  private def decrementBullets(count: Int) {
    if (bullets - count <= 0) bullets = 0
    else bullets = bullets - count
  }
}

class Person (val name:String,  private val superheroName:String)  //The superhero name is private!

object Person {
  def showMeInnerSecret(x:Person) = x.superheroName
}


class AboutObjects extends KoanSuite {
  koan(
    """An object is a singleton. One object -- that's it. This object is a replacement of static in Java,
      | and is called upon much in the same way""") {

    object Greeting {
      def english = "Hi"

      def espanol = "Hola"

      def deutsch = "Hallo"

      def magyar = "Szia"
    }

    Greeting.english should be("Hi")
    Greeting.espanol should be("Hola")
    Greeting.deutsch should be("Hallo")
    Greeting.magyar should be("Szia")
  }

  koan( """Here is proof an object is a singleton, and not a static method in a class""") {
    object Greeting {
      def english = "Hi"

      def espanol = "Hola"

      def deutsch = "Hallo"

      def magyar = "Szia"
    }

    val x = Greeting
    val y = x

    x eq y should be(true) //Reminder, eq checks for reference
    x == y should be(true) //Reminder, == checks for reference

    val z = Greeting

    x eq z should be(true)
  }


  koan(
    """An object that has the same name as class is called a companion object,
      | it is used to contain factories for the class that it complements""") {

    class Movie(val name: String, val year: Short)

    object Movie {

      def apply(x: Short): Movie = {
        val m: Movie = academyAwardBestMoviesForYear(x).getOrElse {
          new Movie("None movie", 2030)
        }
        new Movie(m.name, m.year)
      }

      def academyAwardBestMoviesForYear(x: Short): Option[Movie] = {
        //These are match statement, more powerful than Java switch statements!
        x match {
          case 1930 => Some(new Movie("All Quiet On the Western Front", 1930))
          case 1931 => Some(new Movie("Cimarron", 1931))
          case 1932 => Some(new Movie("Grand Hotel", 1932))
          case _ => None
        }
      }
    }

    Movie.academyAwardBestMoviesForYear(1932).get.name should be("Grand Hotel")
    Movie(1932).name should be("Grand Hotel")
  }


  koan(
    """A companion object stores shared variables and values for every instantiated class to share.
      | (See SecretAgent class and companion object above).""") {


    val bond = SecretAgent("James Bond")
    val felix = SecretAgent("Felix Leitner")
    val jason = SecretAgent("Jason Bourne")
    val _99 = SecretAgent("99")
    val max = SecretAgent("Max Smart")

    bond.shoot(800)
    felix.shoot(200)
    jason.shoot(150)
    _99.shoot(150)
    max.shoot(200)

    SecretAgent.bullets should be(1500)
  }


  koan("A companion object can also see private values and variables of the instantiated objects") {

    val clark = new Person("Clark Kent", "Superman")
    val peter = new Person("Peter Parker", "Spiderman")
    val bruce = new Person("Bruce Wayne", "Batman")
    val diana = new Person("Diana Prince", "Wonder Woman")

    // does not compile - inaccessible
//    clark.superheroName should be ("Superman")
    Person.showMeInnerSecret(clark) should be ("Superman")
    Person.showMeInnerSecret(peter) should be ("Spiderman")
    Person.showMeInnerSecret(bruce) should be ("Batman")
    Person.showMeInnerSecret(diana) should be ("Wonder Woman")
  }
}
