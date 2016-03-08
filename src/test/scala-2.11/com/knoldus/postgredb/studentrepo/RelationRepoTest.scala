package com.knoldus.postgredb.studentrepo

import com.knoldus.postgredb.studentconnection.H2DBComponent
import org.scalatest.FunSuite
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}

class RelationRepoTest extends FunSuite with RelationApp with H2DBComponent with ScalaFutures{

  implicit val defaultPatience=PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))

  test("Add a new Relation (StudentID-SubjectID) ") {
    val response = create(2,2)
    whenReady(response) { res=>
      assert(res==1)

    }
  }


  test("Update a Relation Record for Student ID ") {
    val response = updateStudent(1,2)
    whenReady(response) { res =>
      assert(res === 2)
    }
  }
  test("Update a Relation Record for Subject ID ") {
    val response = updateSubject(2,2)
    whenReady(response) { res =>
      assert(res === 1)
    }
  }

  test("Delete an Existing Relation Record with Student ID ") {
    val response = deleteStudent(1)
    whenReady(response) { res =>
      assert(res === 2)
    }
  }

  test("Delete an Existing Relation Record with Subject ID ") {
    val response = deleteStudent(1)
    whenReady(response) { res =>
      assert(res === 2)
    }
  }

  test("Populate all Elements from Database") {
    val Subjects = getRelations()
    whenReady(Subjects) { listOfSubjects =>
      assert(listOfSubjects === List(Relation(1,1), Relation(1,2), Relation(2,1)))
    }
  }

  test("Get Subject by Students"){
    val response = getSubjects(1)
    whenReady(response) { res =>
      assert(res === Vector((1,Some(1)), (1,Some(2))))
    }

  }

  test("Get Student by Subject"){
    val response = getStudents(2)
    whenReady(response) { res =>
      assert(res === Vector((2,Some(1))))
    }

  }

  test("Get Invalid Subject by Students"){
    val response = getSubjects(5)
    whenReady(response) { res =>
      assert(res === Vector())
    }

  }

  test("Get Invalid Student by Subject"){
    val response = getStudents(5)
    whenReady(response) { res =>
      assert(res === Vector())
    }

  }

}
