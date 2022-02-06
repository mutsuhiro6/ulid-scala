package io.github.mutsuhiro6.util

import java.util.concurrent.atomic.AtomicReference

/** Monotonical factory of ULID.
  * ==Overview==
  * The main use may be generating a random ULID string monotonically, as so
  * {{{
  * scala> val ulid = ULID.randomULID.toString
  * val ulid: String = 01FTJ5FZE7KVDKV46Y5XK23QNW
  * }}}
  * To simply generate a monotonical ULID instance.
  * {{{
  * scala> val ulid = ULID()
  * val ulid: com.github.mutsuhiro6.util.ulid.ULID = 01FTJ4YSZVCGN9NB1ZJDPPJ6NK
  * }}}
  * You can also create a monotonical ULID instance from specific timestamp.
  * {{{
  * val ulid = ULID(1643435103204L)
  * val ulid: com.github.mutsuhiro6.util.ulid.ULID = 01FTJ5V4Z4RH95533VSPW147DA
  * }}}
  */
object MonotonicULID:

  /** Hold previous data of ULID. */
  private lazy val previousULID = AtomicReference[ULID](ULID.randomULID)

  /** Generate a ULID monotonically from specific timestamp. */
  def apply(timestamp: Long = timestamp()): ULID =
    previousULID.updateAndGet(ulid =>
      if ulid.timestamp != timestamp then ULID(timestamp)
      else increment(ulid)
    )

  /** Generate a ULID monotonically. */
  def randomULID: ULID = apply()

  /** Increment ULID with carrying. */
  private def increment(ulid: ULID): ULID =
    if ulid.leastSigBits != ~0L then
      ULID(ulid.mostSigBits, ulid.leastSigBits + 1)
    else if (ulid.mostSigBits & 0xffffL) != 0xffffL then
      ULID(ulid.mostSigBits + 1, 0)
    else throw new ArithmeticException("Randomness part overflowed.")

  private def setPreviousULID(ulid: ULID): Unit = previousULID.set(ulid)
