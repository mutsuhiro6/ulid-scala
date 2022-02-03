package benchmark.ulid
import com.chatwork.scala.ulid.{ULID => ChatworkULID}
import de.huxhorn.sulky.ulid.{ULID => SulkyULID}
import net.petitviolet.ulid4s.{ULID => PetitvioletULID}
import com.github.mutsuhiro6.util.ulid.{ULID => M6ULID}
import com.github.mutsuhiro6.util.ulid.{MonotonicULID => M6MonoULID}
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
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class ULIDCompare:

  @Benchmark
  def airframeULID =
    AirframeULID.newULID < AirframeULID.newULID

  @Benchmark
  def chatworkULID =
    ChatworkULID.generate() < ChatworkULID.generate()

  val sulkyULIDGen = new SulkyULID
  @Benchmark
  def sulkyULID =
    sulkyULIDGen.nextValue compareTo sulkyULIDGen.nextValue

  @Benchmark
  def m6ULID =
    M6ULID.randomULID < M6ULID.randomULID
