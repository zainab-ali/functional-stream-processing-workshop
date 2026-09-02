import fs2.*

Stream.empty

// Stream(1).repeat.compile.count

// Stream.empty.repeat.compile.count

Stream(1).repeat
  .take(2)
  .compile
  .count