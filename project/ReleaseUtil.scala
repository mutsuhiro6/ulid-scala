import sbt._
import scala.sys.process.Process
import scala.util.Try

object ReleaseUtil {

  def getLatestTagOnCurrentBranch: String = {
    Process("git describe --abbrev=0 --tags").lineStream.head
  }

  def getCurrentBranchName: String = {
    Process("git rev-parse --abbrev-ref HEAD").lineStream.head
  }

  def getLatestCommitHash: String = {
    Process("git rev-parse HEAD").lineStream.head
  }

  def getLatestCommitTimestamp: String = {
    Process(
      "git log -1 --date=format:'%Y%m%d-%H%M%S' --format=%cd"
    ).lineStream.head
  }

  def isConformedSemVerSpec(verTag: String): Boolean = {
    val dotSplitVerTag = verTag.split("\\.")
    if (dotSplitVerTag.length != 3) false
    else {
      dotSplitVerTag.forall { seg =>
        // https://semver.org/
        // Check if verTag conform to Semantic Versioning Spec.
        val parsedSeg = Try(seg.toInt)
        // see semverspec 2
        parsedSeg.isSuccess && // integer format
        parsedSeg.get >= 0 && // non-negative
        parsedSeg.get.toString().equals(seg) // not contains leading zero
      }
    }
  }
}
