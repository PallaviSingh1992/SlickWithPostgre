package com.knoldus.postgredb.studentrepo

import org.scalatest.FunSuite
import com.knoldus.postgredb.studentconnection.H2DBComponent
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis,Seconds,Span}

class StudentRepoTest extends FunSuite with StudentApp with H2DBComponent with ScalaFutures {

  implicit val defaultPatience=PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))

  test("Add a new Student ") {
    val response = create("Neha")
    whenReady(response) { sid =>
      assert(sid === 3)
    }
  }
  test("Update a student Record  ") {
    val response = update(2,"Himani")
    whenReady(response) { res =>
      assert(res === 1)
    }
  }

  test("Update a Invalid Students ") {
    val response = update(3,"Rajat")
    whenReady(response) { result =>
      assert(result === 0)
    }
  }


  test("Delete an Existing Student Record  ") {
    val response = delete(1)
    whenReady(response) { res =>
      assert(res === 1)
    }
  }

  test("Delete a Invalid Student ") {
    val response = delete(11)
    whenReady(response) { stuId =>
      assert(stuId === 0)
    }
  }

  test("Get a Students ") {
    val response = getById(1)
    whenReady(response) { list =>
      assert(list.get === Student(1,"Pallavi"))
    }
  }

  test("Get a Invalid Students ") {
    val response = getById(11)
    whenReady(response) { list =>
      assert(list === None)
    }
  }

  test("Populate all Elements from Database") {
    val Students = getStudents()

    whenReady(Students) { listOfStudents =>
      assert(listOfStudents === List(Student( 1,"Pallavi"), Student(2,"Himani")))
    }
  }




}
