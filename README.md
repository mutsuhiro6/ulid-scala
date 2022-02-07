[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.mutsuhiro6/ulid-scala3_3/badge.svg?syle=flat-square&gav=true)](https://maven-badges.herokuapp.com/maven-central/io.github.mutsuhiro6/ulid-scala3_3)

# ulid-scala3

ULID generator implemented with Scala 3.

ULID is the lexicographically sortable identifier it will be very useful for the primary keys on databases (my thoght) or so on.

See details about the specifications of ULID on [ulid/spec](https://github.com/ulid/spec).

## Usage

### Dependency

For Scala 3,

```scala
libraryDependencies += "io.github.mutsuhiro6" %% "ulid-scala3" % "<version>"
```

This library only supports Scala 2.13.7,

```scala
libraryDependencies += "io.github.mutsuhiro6" % "ulid-scala3_3" % "<version>"
scalacOptions ++= Seq("-Ytasty-reader")
```

### Simple ULID generation

You can generate ULID in the same way as `java.util.UUID`.

```scala
scala> import com.github.mutsuhiro6.util.ULID
                                                                                                                     
scala> ULID.randomULID
val res0: com.github.mutsuhiro6.util.ULID = 01FV2JEC4TJYKQT6JMF0EY7ZDM
```

It also provides some useful methods that comply with the UUID specification.

```scala
scala> ULID()
val res1: com.github.mutsuhiro6.util.ULID = 01FV2JEZNHE2CF8A85MRBVQ2PN
                                                                                                                     
scala> ULID.of("01FV2JEZNHE2CF8A85MRBVQ2PN")
val res2: com.github.mutsuhiro6.util.ULID = 01FV2JEZNHE2CF8A85MRBVQ2PN
                                                                                                                     
scala> ULID(0L)
val res3: com.github.mutsuhiro6.util.ULID = 0000000000A7BVK22FD695NXVS
```

## Monotonical ULID generation

Same as above for simple use.

```scala
scala> import com.github.mutsuhiro6.util.ULID
                                                                                                                     
scala> MonotonicalULID.randomULID
val res0: com.github.mutsuhiro6.util.ULID = 01FV2JEC4TJYKQT6JMF0EY7ZDM
```

See [example code](example/scala_3/src/main/scala/ULIDExample.scala) to confirm the monotonical behavior.

```
===Monotonic ULID Generation===
01FV2JQG9V5BEFBENE7RXE5VVE (com.github.mutsuhiro6.util.ULID)
01FV2JQG9YTZNFXQ9H4E033548 (com.github.mutsuhiro6.util.ULID)
01FV2JQG9YTZNFXQ9H4E033549 (com.github.mutsuhiro6.util.ULID)
01FV2JQG9YTZNFXQ9H4E03354A (com.github.mutsuhiro6.util.ULID)
01FV2JQG9YTZNFXQ9H4E03354B (com.github.mutsuhiro6.util.ULID)
```
