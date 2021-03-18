import java.io._

import scala.io.Source._
import org.apache.hadoop.conf._
import org.apache.hadoop.fs._

object Main extends App {

  private val conf = new Configuration()
  private val hdfsCoreSitePath = new Path("core-site.xml")
  private val hdfsHDFSSitePath = new Path("hdfs-site.xml")
  conf.addResource(hdfsCoreSitePath)
  conf.addResource(hdfsHDFSSitePath)
  private val fileSystem = FileSystem.get(conf)

  val lsStage = fileSystem.listStatus(new Path("/stage"))

  for (i <- lsStage) {
    val lsPartition = fileSystem.listStatus(i.getPath)
    var strExt = ""
    val filePath = "/ods/" + i.getPath.getName
    createFolder(filePath)

    for (j <- lsPartition)
      if (j.isFile) strExt += readFile(j.getPath)

    createFile(filePath + "/part-0000.csv", strExt)
    removeFile(i.getPath)
  }

  fileSystem.close()

  def readFile(filePath: Path): String = {
    fromInputStream(fileSystem.open(filePath)).mkString("")
  }

  def createFolder(folderPath: String): Unit = {
    val path = new Path(folderPath)
    if (!fileSystem.exists(path)) fileSystem.mkdirs(path)
  }

  def createFile(filePath: String, content: String): Unit = {
    val path = new Path(filePath)
    val dataOutputStream = fileSystem.create(path)
    val bw = new BufferedWriter(new OutputStreamWriter(dataOutputStream, "UTF-8"))
    bw.write(content)
    bw.close
  }

  def removeFile(filePath: Path) = {
    fileSystem.delete(filePath, true)
  }
}
