package com.knoldus.postgredb.studentrepo

import com.knoldus.postgredb.studentconnection.{PostgreSqlDBComponent, DBComponent}
import slick.driver.PostgresDriver.api._
import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


case class Relation(sid:Int,subid:Int)



trait RelationRepo extends StudentRepo with SubjectRepo{
  this: DBComponent =>

  import driver.api._

  class RelationTable(tag: Tag) extends Table[Relation](tag, "relation") {
    val sid = column[Int]("sid")
    val subid = column[Int]("subid")


    def relfk1 = foreignKey("student_subject_fk1", sid, studInfo)(_.sid)

    def relfk2 = foreignKey("student_subject_fk2", subid, subInfo)(_.subid)

    def * = (sid, subid) <>(Relation.tupled, Relation.unapply)


  }
  val relInfo = TableQuery[RelationTable]

}

   trait RelationApp extends RelationRepo{this:DBComponent=>
     import driver.api._

     def create(sid:Int,subid:Int):Future[Int]= {
       //db.run { relInfo.returning(relInfo.map(_.sid))+= Relation(sid,subid,1) }
       db.run { relInfo+= Relation(sid,subid) }
     }
     def deleteStudent(id: Int): Future[Int] = {
       db.run {
         relInfo.filter(_.sid === id).delete
       }
     }
     def deleteSubject(id: Int): Future[Int] = {
       db.run {
         relInfo.filter(_.subid === id).delete
       }
     }
     def updateStudent(sid:Int,subid:Int): Future[Int] = {
       db.run { relInfo.filter(_.sid ===sid).update(Relation(sid,subid)) }
     }

     def updateSubject(sid:Int,subid:Int): Future[Int] = {
       db.run { relInfo.filter(_.subid ===subid).update(Relation(sid,subid)) }
     }


     def getRelations(): Future[List[Relation]] = db.run { relInfo.to[List].result }

     def getSubjects(id:Int)={
       val leftOuterJoin = for {
         (c, s) <- subInfo.filter(_.subid===id) joinLeft relInfo on (_.subid === _.subid)
       } yield (c.subid, s.map(_.sid))
       db.run {
           leftOuterJoin.result
       }
     }

     def getStudents(id:Int)={
       val leftOuterJoin = for {
         (c, s) <- studInfo.filter(_.sid===id) joinLeft relInfo on (_.sid === _.sid)
       } yield (c.sid, s.map(_.subid))
       db.run {
         leftOuterJoin.result
       }
     }

}
object RelationRepo extends RelationRepo with PostgreSqlDBComponent




