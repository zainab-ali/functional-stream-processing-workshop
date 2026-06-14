import fs2.*

val stream = Stream.range(0, 10)

stream.take(5)

stream.take(5).toList

Stream.emits(List.range(0, 10))

val numbersStream = Stream.range(0, Int.MaxValue)

// numbersStream.compile.count

// numbersStream.debug().compile.count
// // 0
// // 1
// // ..
// // 2147483646
