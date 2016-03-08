package com.knoldus.postgredb.studentrepo

import com.knoldus.postgredb.studentconnection.DBComponent
import com.knoldus.postgredb.studentconnection.PostgreSqlDBComponent
import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


case class Subject(subid:Int,subname:String)

trait SubjectRepo { this: DBComponent =>

  import driver.api._

  class SubjectTable(tag: Tag) extends Table[Subject](tag, "subject") {
    val subid = column[Int]("subid", O.PrimaryKey, O.AutoInc)
    val subname = column[String]("subname", O.SqlType("VARCHAR(100)"))

    def * = (subid, subname) <>(Subject.tupled, Subject.unapply)
  }

  val subInfo = TableQuery[SubjectTable]
}

  trait SubjectApp extends SubjectRepo{ this: DBComponent =>

    import driver.api._

   // val db = Database.forConfig("postgresql")

    def create(str:String):Future[Int]={
      val subj=Subject(1,str)
      db.run { subInfo.returning(subInfo.map(_.subid))+= subj }
    }

    def update(id:Int,str:String): Future[Int] ={
      val subj=Subject(id,str)
      db.run { subInfo.filter(_.subid===id).update(subj) }
    }

    def delete(id: Int): Future[Int] = db.run { subInfo.filter(_.subid === id).delete }


    def getById(id: Int): Future[Option[Subject]] = db.run { subInfo.filter(_.subid === id).result.headOption }


    def getSubjects(): Future[List[Subject]] = db.run { subInfo.to[List].result }


  }
object SubjectRepo extends SubjectRepo with PostgreSqlDBComponent
