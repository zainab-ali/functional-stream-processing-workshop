import fs2.*

/** The following stream outputs no values */
Stream.empty

/** This stream is infinite */
val ones = Stream(1).repeat

/** What happens when it is compiled and run? */
ones.compile.count


/** An infinite empty stream contains no values */
val empties = Stream.empty.repeat

/** What happens when it is compiled and run? */
empties.compile.count

/** A finite stream can be constructed from an infinite stream by composing operators. */
Stream(1).repeat
  .take(2)
  .compile
  .count
