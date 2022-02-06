import io.github.mutsuhiro6.util.ULID
import io.github.mutsuhiro6.util.MonotonicULID

object ULIDExample {

  def main(args: Array[String]): Unit = {

    println("===Simple Usage of ULID===")
    // You can get ULID as so,
    // it's the same as [[java.util.UUID]] usage.
    val ulid0: ULID = ULID.randomULID
    printlnWithClassName(ulid0)
    // or
    val ulid1: ULID = ULID()
    printlnWithClassName(ulid1)
    // String representation of the ULID;
    val ulidStr: String = ulid1.toString
    printlnWithClassName(ulidStr)
    // Create ULID object from String.
    val ulidFromStr: ULID = ULID.of(ulidStr)
    printlnWithClassName(ulidFromStr)
    // Generate ULID object from specified timestamp.
    val timestamp = 0L
    val ulidAtZeroEpoch = ULID(timestamp)
    printlnWithClassName(ulidAtZeroEpoch)

    println("===Monotonic ULID Generation===")
    // Monotonic factory is also provided
    (0 until 5) map (_ =>
      MonotonicULID.randomULID
    ) foreach (printlnWithClassName)
  }

  def printlnWithClassName(obj: AnyRef): Unit = {
    println(s"${obj} (${obj.getClass.getCanonicalName})")
  }
}
