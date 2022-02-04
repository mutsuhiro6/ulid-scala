## ulid-scala3

ULID generator implemented with Scala 3.

### Usage

```scala
scala> import com.github.mutsuhiro6.util.ULID
                                                                                                                     
scala> ULID.randomULID
val res0: com.github.mutsuhiro6.util.ULID = 01FV2JEC4TJYKQT6JMF0EY7ZDM
                                                                                                                     
scala> ULID()
val res1: com.github.mutsuhiro6.util.ULID = 01FV2JEZNHE2CF8A85MRBVQ2PN
                                                                                                                     
scala> ULID.of("01FV2JEZNHE2CF8A85MRBVQ2PN")
val res2: com.github.mutsuhiro6.util.ULID = 01FV2JEZNHE2CF8A85MRBVQ2PN
                                                                                                                     
scala> ULID(0L)
val res3: com.github.mutsuhiro6.util.ULID = 0000000000A7BVK22FD695NXVS
```

### Monotonic ULID Factory

See [example code](example/scala_3/src/main/scala/ULIDExample.scala)

```
===Monotonic ULID Generation===
01FV2JQG9V5BEFBENE7RXE5VVE (com.github.mutsuhiro6.util.ULID)
01FV2JQG9YTZNFXQ9H4E033548 (com.github.mutsuhiro6.util.ULID)
01FV2JQG9YTZNFXQ9H4E033549 (com.github.mutsuhiro6.util.ULID)
01FV2JQG9YTZNFXQ9H4E03354A (com.github.mutsuhiro6.util.ULID)
01FV2JQG9YTZNFXQ9H4E03354B (com.github.mutsuhiro6.util.ULID)
```