import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.nio.file.{Files, Paths}
import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Success, Try}


object Main {
  val mapper = JsonMapper.builder()
    .addModule(DefaultScalaModule)
    .build()

  def main(args: Array[String]): Unit = {

    val fileName = "D:\\code\\scala\\de17\\lab01\\src\\main\\resources\\u.data"
    val file = "D:\\code\\scala\\de17\\lab01\\target\\lab01.json"
    val myItemId = 237

    val source = scala.io.Source.fromFile(fileName)
    val errs = ListBuffer.empty[String]
    val data = source.getLines.map(_.split("\t"))
      .toSeq
      .map(mapData(_, errs))
      .filter(_.isDefined)
      .map(_.get)

    val histFilm = getRating(data.filter(_.itemId == myItemId))
    val histAll = getRating(data)

   val dto =  HistDto(histFilm.map(_._2), histAll.map(_._2))

    val str = mapper.writer().writeValueAsString(dto)
    Files.write(Paths.get(file), str.getBytes("UTF-8"))
  }

  private def mapData(arr: Array[String], errs: ListBuffer[String]) = {
    Try {
      Data(arr(0).toInt, arr(1).toInt, arr(2).toInt, arr(3).toInt)
    } match {
      case Success(v) => Some(v)
      case Failure(e) => errs += e.getMessage
        None
    }
  }

  private def getRating(data: Seq[Data]) = {
    data.groupBy(_.rating).map { case (rating, list) =>
      val count = list.size
      rating -> count
    }.toSeq.sortBy { case (rating, _) => rating }
  }
}