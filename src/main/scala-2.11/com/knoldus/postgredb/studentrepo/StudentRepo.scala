package com.knoldus.postgredb.studentrepo

import com.knoldus.postgredb.studentconnection.DBComponent
import com.knoldus.postgredb.studentconnection.PostgreSqlDBComponent
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

case class Student(sid:Int=0,name:String)

trait StudentRepo {
  this: DBComponent =>

  import driver.api._

  class StudentTable(tag: Tag) extends Table[Student](tag, "student") {
    val sid = column[Int]("sid", O.PrimaryKey, O.AutoInc)
    val name = column[String]("name", O.SqlType("VARCHAR(100)"))

    def * = (sid, name) <>(Student.tupled, Student.unapply)
  }

  val studInfo = TableQuery[StudentTable]

}

trait StudentApp extends StudentRepo{this:DBComponent=>

  import driver.api._

  //val db = Database.forConfig("postgresql")

  def create(str:String):Future[Int]={
   val stud=Student(1,str)
    db.run { studInfo.returning(studInfo.map(_.sid))+= stud }
  }

  def update(id:Int,str:String): Future[Int] ={
    val stud=Student(id,str)
    db.run { studInfo.filter(_.sid===id).update(stud) }
  }

  def delete(id: Int): Future[Int] = db.run { studInfo.filter(_.sid === id).delete }


  def getById(id: Int): Future[Option[Student]] = db.run { studInfo.filter(_.sid === id).result.headOption }


  def getStudents(): Future[List[Student]] = db.run { studInfo.to[List].result }

}

object StudentRepo extends StudentRepo with PostgreSqlDBComponent
