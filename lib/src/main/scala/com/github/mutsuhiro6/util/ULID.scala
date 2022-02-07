package io.github.mutsuhiro6.util

import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.concurrent.atomic.AtomicReference
import scala.collection.mutable

/** Default timestamp generator function. */
private val timestamp: () => Long = () => System.currentTimeMillis

/** Default random number (10 bytes) generator function. */
private val random: () => Array[Byte] =
  val defaultAlgorithm = "NativePRNGNonBlocking"
  val random =
    try SecureRandom.getInstance(defaultAlgorithm)
    catch
      case _: NoSuchAlgorithmException =>
        SecureRandom.getInstanceStrong
  () =>
    val randomBytes = Array.ofDim[Byte](10)
    random.nextBytes(randomBytes)
    randomBytes

/** ULID (Universally Unique Lexicographically Sortable Identifier) It is
  * defined by 128 bits.
  * @see
  *   https://github.com/ulid/spec
  * @constructor
  *   Create a new ULID with left-hand side 64 bits and right-hand side 64 bits.
  * @param mostSigBits
  *   Left-hand side 64 bits
  * @param leastSigBits
  *   Right-hand side 64 bits
  */
case class ULID(mostSigBits: Long, leastSigBits: Long)
    extends Ordered[ULID]
    with Serializable:

  val timestamp: Long = mostSigBits >>> 16

  private val ulidStr = ULID.base32Encode(mostSigBits, leastSigBits)

  override def toString: String = ulidStr

  override def compare(that: ULID): Int =
    val msbCompare = mostSigBits compare that.mostSigBits
    if msbCompare == 0 then leastSigBits compare that.leastSigBits
    else msbCompare

/** Provides functions to generate ULID instances.
  * ==Overview==
  * The main use may be generating a random ULID string, as so
  * {{{
  * scala> val ulid = ULID.randomULID.toString
  * val ulid: String = 01FTJ5FZE7KVDKV46Y5XK23QNW
  * }}}
  * To simply generate a ULID instance.
  * {{{
  * scala> val ulid = ULID()
  * val ulid: com.github.mutsuhiro6.util.ulid.ULID = 01FTJ4YSZVCGN9NB1ZJDPPJ6NK
  * }}}
  * You can also create a ULID object from specific timestamp.
  * {{{
  * val ulid = ULID(1643435103204L)
  * val ulid: com.github.mutsuhiro6.util.ulid.ULID = 01FTJ5V4Z4RH95533VSPW147DA
  * }}}
  * Also provides the conversion to a ULID object from a String.
  * {{{
  * val ulid = ULID.fromString("61VB9NT4CB0KG8ZP3384TBVBYS")
  * val ulid: com.github.mutsuhiro6.util.ulid.ULID = 61VB9NT4CB0KG8ZP3384TBVBYS
  * }}}
  */
object ULID:

  /** Returns a ULID object with timestamp and random byte array.
    * @param timestamp
    *   Timestamp (milli-order)
    * @param randomness
    *   Byte array with 10 length
    */
  def apply(
      timestamp: Long = timestamp(),
      randomness: Array[Byte] = random()
  ): ULID =
    require(
      randomness.length == 10,
      s"Randomness must be 80 bits (10 bytes), but got ${randomness.length} length."
    )
    require(
      timestamp <= 0xffffffffffffL, // 281474976710655
      s"Timestamp must be less than ${0xffffffffffffL} (48 bits), but got ${timestamp}."
    )
    require(
      timestamp >= 0,
      s"Timestamp must be positive, but got ${timestamp}."
    )
    val msb: Long =
      (timestamp & 0xffffffffffffL) << (64 - 48) | // 48 bits timestamp part
        (randomness(0) & 0xffL) << 8 |
        (randomness(1) & 0xffL)
    val lsb: Long = ((randomness(2) & 0xffL) << 56) |
      ((randomness(3) & 0xffL) << 48) |
      ((randomness(4) & 0xffL) << 40) |
      ((randomness(5) & 0xffL) << 32) |
      ((randomness(6) & 0xffL) << 24) |
      ((randomness(7) & 0xffL) << 16) |
      ((randomness(8) & 0xffL) << 8) |
      (randomness(9) & 0xffL)
    ULID(msb, lsb)

  /** Generate a random ULID */
  def randomULID: ULID = apply()

  /** Generate a ULID instance from String. */
  def of(ulid: String): ULID =
    require(
      ulid.length == 26,
      s"ULID should be 26 length, but got ${ulid} (length: ${ulid.length})."
    )
    require(
      ulid.head <= '7',
      s"The largest ULID is \"7ZZZZZZZZZZZZZZZZZZZZZZZZZ\", but got ${ulid}."
    )
    require(
      ulid.forall(c => crockfordsBase32Decoder(c) != -1),
      s"${ulid} contains some invalid charactor(s)."
    )
    base32Decode(ulid)

  /** Crockford's Base32 encoding map */
  private val encodeSymbols: Array[Char] = Array(
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
    'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X',
    'Y', 'Z'
  )

  /** Crockford's Base32 decofing map */
  private val decodeSymbols: Array[Byte] = Array[Byte](
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 9
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 19
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 29
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 39
    -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, // 49
    2, 3, 4, 5, 6, 7, 8, 9, -1, -1, // 59
    -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, // 69
    15, 16, 17, 1, 18, 19, 1, 20, 21, 0, // 79
    22, 23, 24, 25, 26, -1, 27, 28, 29, 30, // 89
    31, -1, -1, -1, -1, -1, -1, 10, 11, 12, // 99
    13, 14, 15, 16, 17, 1, 18, 19, 1, 20, // 109
    21, 0, 22, 23, 24, 25, 26, -1, 27, 28, // 119
    29, 30, 31, -1, -1, -1, -1, -1 // 127
  )

  /** Encodes a byte to char following the Crockford's Base 32 procedure. */
  private inline def crockfordsBase32Encoder: Int => Char =
    symbolValue => encodeSymbols(symbolValue)

  /** Decodes a char to byte following the Crockford's Base 32 procedure. */
  private inline def crockfordsBase32Decoder: Char => Byte =
    encodeSymbol => decodeSymbols(encodeSymbol)

  /** Encode ULID bytes to base 32. */
  private def base32Encode(
      mostSigBits: Long,
      leastSigBits: Long,
      encode: Int => Char = crockfordsBase32Encoder
  ): String =
    val encoded = mutable.StringBuilder(26)
    var msb = mostSigBits
    var lsb = leastSigBits
    for _ <- 0 until 26 do
      encoded += encode((lsb & 0x1fL).toInt)
      lsb >>>= 5
      lsb |= ((msb & 0x1fL) << (64 - 5))
      msb >>>= 5
    encoded.reverseInPlace.toString

  /** Decode a ULID String into a ULID object. */
  private def base32Decode(
      ulid: String,
      decode: Char => Byte = crockfordsBase32Decoder
  ): ULID =
    var msb = 0L
    var lsb = 0L
    val carrier = ~(~0L >>> 5) //0xF800...0L
    for i <- 0 until 26 do
      msb <<= 5
      msb |= ((lsb & carrier) >>> 59) // 64-5
      lsb <<= 5
      lsb |= decode(ulid.charAt(i))
    ULID(msb, lsb)
