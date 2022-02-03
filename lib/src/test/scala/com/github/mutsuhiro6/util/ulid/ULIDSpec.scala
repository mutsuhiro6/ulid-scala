package com.github.mutsuhiro6.util.ulid

import org.scalatest._
import flatspec._
import matchers._
import com.github.mutsuhiro6.util.ulid.ULID

class ULIDSpec extends AnyFlatSpec with should.Matchers:

  "ULID" should "have string which length is 26." in {
    val ulid = ULID.randomULID.toString
    ulid.length should be(26)
  }

  it should "have the same bit info with generated from ULID string." in {
    val ulid01 = ULID.randomULID
    val ulidStr = ulid01.toString
    val ulid02 = ULID.fromString(ulidStr)
    ulid01 should be(ulid02)
  }

  it should "matches regular expression \"[0123456789ABCDEFGHJKMNPQRSTVWXYZ]{26}]\"." in {
    ULID.randomULID.toString
      .matches("[0123456789ABCDEFGHJKMNPQRSTVWXYZ]{26}") should be(true)
  }

  it should "return the correct timestamp." in {
    val timestamp = 0xffffffffffL
    val ulid = ULID(timestamp)
    ulid.timestamp should be(0xffffffffffL)
  }

  it should "reject too large timestamp 281474976710656 (2^48)." in {
    val tooLargeTimestamp = 281474976710656L
    a[IllegalArgumentException] should be thrownBy ULID(tooLargeTimestamp)
  }

  "ULID.fromString" should "reject too large ULID (\"8ZZZZZZZZZZZZZZZZZZZZZZZZZ\")." in {
    val tooLargeUlid = "8ZZZZZZZZZZZZZZZZZZZZZZZZZ"
    a[IllegalArgumentException] should be thrownBy ULID.fromString(tooLargeUlid)
  }

  it should "generate zero-filled String" in {
    val ulid = ULID(0, Array.fill[Byte](10)(0))
    ulid.toString should be("00000000000000000000000000")
  }

  it should "reject invalid length ULID like \"0123456789ABCDEFGHJKMNPQRSTVWXYZ\")" in {
    val tooLongUlid = "0123456789ABCDEFGHJKMNPQRSTVWXYZ"
    a[IllegalArgumentException] should be thrownBy ULID.fromString(tooLongUlid)
  }

  it should "reject invalid character like \"\\t\"" in {
    val invalidUlid = "6ABC:EJ\nF9\r12HGJS0021\t1234"
    a[IllegalArgumentException] should be thrownBy ULID.fromString(invalidUlid)
  }

  it should "reject negative timestamp value" in {
    val timestamp = -1643530852706L
    a[IllegalArgumentException] should be thrownBy ULID(timestamp)
  }

  "MonotonicULID" should "generate incremented ULID when called multiple times at the same timestamp." in {
    val timestamp = 1643530852706L
    val ulid1 = MonotonicULID(timestamp)
    val ulid2 = MonotonicULID(timestamp)
    // Both have the same timestamp.
    ulid2.timestamp should be(ulid1.timestamp)
    // Confirm increment.
    val isIncremented =
      (ulid2.leastSigBits - ulid1.leastSigBits == 1 && ulid2.mostSigBits == ulid1.mostSigBits) ||
        (ulid2.mostSigBits - ulid1.mostSigBits == 1 && ulid2.leastSigBits == ulid1.leastSigBits)
    isIncremented should be(true)
  }

  it should "throw ArithmetricException when incremented ULID will be overflowed." in {
    val ulid = ULID.fromString("01BX5ZZKBKZZZZZZZZZZZZZZZZ")
    val timestamp = ulid.timestamp
    MonotonicULID.setPreviousULID(ulid)
    a[ArithmeticException] should be thrownBy MonotonicULID(timestamp)
  }
