package benchmark.ulid

import com.chatwork.scala.ulid.{ULID => ChatworkULID}
import com.github.mutsuhiro6.util.ulid.{ULID => M6ULID}
import de.huxhorn.sulky.ulid.{ULID => SulkyULID}
import net.petitviolet.ulid4s.{ULID => PetitvioletULID}
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import wvlet.airframe.ulid.{ULID => AirframeULID}

import java.util.UUID
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.SampleTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class ULIDGen :

  @Benchmark
  def javaUUID =
    UUID.randomUUID.toString

  @Benchmark
  def airframeULID =
    AirframeULID.newULIDString
  
  @Benchmark
  def petitvioletULID =
    PetitvioletULID.generate
  
  @Benchmark
  def chatworkULID =
    ChatworkULID.generate().asString
  
  val sulkyULIDGen = new SulkyULID
  @Benchmark
  def sulkyULID =
    sulkyULIDGen.nextULID

  @Benchmark
  def m6ULID =
    M6ULID.randomULID.toString
