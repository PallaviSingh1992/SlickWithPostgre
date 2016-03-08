package com.knoldus.postgredb.studentrepo

import com.knoldus.postgredb.studentconnection.H2DBComponent
import org.scalatest.FunSuite
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}

class SubjectRepoTest extends FunSuite with SubjectApp with H2DBComponent with ScalaFutures{

  implicit val defaultPatience=PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))

  test("Add a new Subject ") {
    val response = create("German")
    whenReady(response) { subid =>
      assert(subid === 3)
    }
  }
  test("Update a Subject Record  ") {
    val response = update(2,"English")
    whenReady(response) { res =>
      assert(res === 1)
    }
  }

  test("Update a Invalid Subject Record  ") {
    val response = update(5,"SST")
    whenReady(response) { res =>
      assert(res === 0)
    }
  }

  test("Delete an Existing Subject Record  ") {
    val response = delete(1)
    whenReady(response) { res =>
      assert(res === 1)
    }
  }
  test("Delete an Invalid Subject Record  ") {
    val response = delete(5)
    whenReady(response) { res =>
      assert(res === 0)
    }
  }

  test("Populate all Elements from Database") {
    val Subjects = getSubjects()
    whenReady(Subjects) { listOfSubjects =>
      assert(listOfSubjects === List(Subject( 1,"English"), Subject(2,"Hindi")))
    }
  }

  test("Get a Subjects ") {
    val response = getById(1)
    whenReady(response) { list =>
      assert(list.get === Subject(1,"English"))
    }
  }

  test("Get Invalid Subjects ") {
    val response = getById(9)
    whenReady(response) { list =>
      assert(list ==None )
    }
  }





}
