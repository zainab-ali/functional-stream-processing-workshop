package cheatsheet

final case class Section(title: String, methods: List[String])
final case class Part(sections: List[Section])

final case class CheatSheet(
    parts: List[Part],
    omittedMethodCalls: List[String],
    addedMethodCalls: List[String]
)

object CheatSheet {

  def default: CheatSheet = {
    val basics = List(
      "s.debug",
      "s.through(f)",
      "s.covary[F]",
      "s.covaryOutput[O2]"
    )

    val constructors = List(
      "Stream.apply(os)",
      "Stream.emit(o)",
      "Stream.emits(os)",
      "Stream.empty",
      "Stream.unit",
      "Stream.eval(fo)",
      "Stream.exec(action)",
      "Stream.repeatEval(fo)",
      "Stream.unfold(s)(f)",
      "Stream.iterate(start)(f)"
    )
    val compile = List(
      "s.toList",
      "s.toVector",
      "s.compile.toVector",
      "s.compile.count",
      "s.compile.toList",
      "s.compile.string",
      "s.compile.onlyOrError",
      "s.compile.last",
      "s.compile.drain",
      "s.compile.foldMonoid"
    )

    val collections = List(
      "s.map(f)",
      "s.flatMap(f)",
      "s.head",
      "s.tail",
      "s.take(n)",
      "s.drop(n)",
      "s.filter(p)",
      "s.collect(pf)",
      "s.find(f)",
      "s.exists(p)",
      "s.takeWhile(p)",
      "s.takeRight(n)",
      "s.cons(c)",
      "s.last",
      "s.repeat",
      "s.zipWithIndex"
    )

    val iteration = List(
      "s.scan(z)(f)",
      "s.fold(z)(f)",
      "s.reduce(f)",
      "s.groupAdjacentBy(f)",
      "s.zipWithPrevious",
      "s.sliding(n)",
      "s.mapAccumulate(init)(f)"
    )

    val combining = List(
      "s.append(s2)",
      "s.onComplete(s2)",
      "s.zip(that)",
      "s.zipWith(that)(f)",
      "s.zipLeft(that)",
      "s.interleave(that)",
      "s.either(that)",
      "s.flatten",
      "s.ifEmpty(fallback)"
    )

    val effects = List(
      "s.evalMap(f)",
      "s.evalTap(f)",
      "s.evalFilter(f)",
      "s.evalScan(z)(f)",
      "s.evalMapAccumulate(s)(f)",
      "s.drain"
    )

    val errors = List(
      "s.handleErrorWith(h)",
      "s.attempt",
      "s.rethrow",
      "Stream.raiseError(e)"
    )

    val bracketing = List(
      "Stream.bracket(acquire)(release)",
      "s.onFinalize(f)",
      "Stream.resource(r)",
      "Stream.bracketCase(acquire)(release)",
      "s.onFinalizeCase(f)"
    )
    val concurrency = List(
      "s.mapAsync(maxConcurrent)(f)",
      "s.mapAsyncUnordered(maxConcurrent)(f)",
      "s.concurrently(that)",
      "s.merge(that)",
      "s.parJoin(maxOpen)",
      "s.parZip(that)"
    )

    val chunking = List(
      "s.chunks",
      "s.chunkN(n)",
      "s.chunkMin(n)",
      "s.chunkLimit(n)",
      "s.unchunks",
      "s.repartition(f)",
      "s.rechunkRandomly"
    )

    val time = List(
      "s.metered(rate)",
      "Stream.fixedRate(period)",
      "Stream.every(d)",
      "Stream.awakeEvery(period)",
      "Stream.sleep(d)",
      "s.delayBy(d)",
      "Stream.duration",
      "s.interruptAfter(duration)",
      "s.timeout(timeout)",
      "s.timeoutOnPull(timeout)"
    )

    val part1 = Part(
      List(
        Section("Basics", basics),
        Section("Constructors", constructors),
        Section("Compile", compile)
      )
    )
    val part2 = Part(
      List(
        Section("Iteration", iteration),
        Section("Collections", collections),
        Section("Combining", combining)
      )
    )

    val part3 = Part(
      List(
        Section("Effects", effects),
        Section("Errors", errors),
        Section("Bracketing", bracketing)
      )
    )

    val part4 = Part(
      List(
        Section("Concurrency", concurrency),
        Section("Chunking", chunking),
        Section("Time", time)
      )
    )

    CheatSheet(
      List(part1, part2, part3, part4),
      omittedMethodCalls = omittedMethodCalls,
      addedMethodCalls = Nil
    )
  }
  private def omittedMethodCalls = List(
    "s.groupWithin(chunkSize, timeout)",
    "s.compile",
    "Stream.fromPublisher(chunkSize)(subscribe)",
    "s.evalFold(z)(f)",
    "s.++(s2)",
    "s.as(o2)",
    "s.attempts(delays)",
    "s.broadcastThrough(pipes)",
    "s.buffer(n)",
    "s.bufferAll",
    "s.bufferBy(f)",
    "s.changes",
    "s.changesBy(f)",
    "s.chunkAll",
    "s.collectFirst(pf)",
    "s.collectWhile(pf)",
    "s.conflateChunks(chunkLimit)",
    "s.conflate(chunkLimit, zero)(f)",
    "s.conflate1(chunkLimit)(f)",
    "s.conflateSemigroup(chunkLimit)",
    "s.conflateMap(chunkLimit)(f)",
    "s.consChunk(c)",
    "s.cons1(o)",
    "s.covaryAll[F2,O2]",
    "s.debounce(d)",
    "s.meteredStartImmediately(rate)",
    "s.spaced(delay)",
    "s.debugChunks",
    "s.keepAlive(maxIdle, heartbeat)",
    "s.delete(p)",
    "s.dropLast",
    "s.dropLastIf(p)",
    "s.dropRight(n)",
    "s.dropThrough(p)",
    "s.dropWhile(p)",
    "s.enqueueUnterminated(queue)",
    "s.enqueueUnterminated(queue)",
    "s.enqueueUnterminatedChunks(queue)",
    "s.enqueueUnterminatedChunks(queue)",
    "s.enqueueNoneTerminated(queue)",
    "s.enqueueNoneTerminated(queue)",
    "s.enqueueNoneTerminatedChunks(queue)",
    "s.enqueueNoneTerminatedChunks(queue)",
    "s.ensure(e)(p)",
    "s.evalMapChunk(f)",
    "s.evalMapFilter(f)",
    "s.evalTap(f, F)",
    "s.evalTapChunk(f)",
    "s.filterNot(p)",
    "s.evalFilterAsync(maxConcurrent)(f)",
    "s.evalFilterNot(f)",
    "s.evalFilterNotAsync(maxConcurrent)(f)",
    "s.filterWithPrevious(f)",
    "s.>>(s2)",
    "s.fold1(f)",
    "s.foldMap(f)",
    "s.foldMonoid",
    "s.forall(p)",
    "s.foreach(f)",
    "s.groupAdjacentByLimit(limit)(f)",
    "s.handleErrorWith(h)",
    "s.hold(initial)",
    "s.holdOption",
    "s.hold1",
    "s.holdResource(initial)",
    "s.hold1Resource",
    "s.holdOptionResource",
    "s.ifEmptyEmit(o)",
    "s.interleaveAll(that)",
    "s.interruptWhen(haltWhenTrue)",
    "s.interruptWhen(haltWhenTrue)",
    "s.interruptWhen(haltWhenTrue)",
    "s.interruptWhen(haltOnSignal)",
    "s.interruptScope",
    "s.intersperse(separator)",
    "s.lastOr(fallback)",
    "s.limit(n)",
    "s.mapChunks(f)",
    "s.mask",
    "s.switchMap(f)",
    "s.mergeHaltBoth(that)",
    "s.mergeHaltL(that)",
    "s.mergeHaltR(that)",
    "s.interleaveOrdered(that)",
    "s.noneTerminate",
    "s.onFinalizeWeak(f)",
    "s.onFinalizeCaseWeak(f)",
    "s.parEvalMap(maxConcurrent)(f)",
    "s.parEvalMapUnbounded(f)",
    "s.parEvalMapUnordered(maxConcurrent)(f)",
    "s.parEvalMapUnorderedUnbounded(f)",
    "s.parZipWith(that)(f)",
    "s.pauseWhen(pauseWhenTrue)",
    "s.pauseWhen(pauseWhenTrue)",
    "s.prefetch",
    "s.prefetchN(n)",
    "s.printlns",
    "s.rechunkRandomlyWithSeed(minFactor, maxFactor)(seed)",
    "s.reduceSemigroup",
    "s.repeatN(n)",
    "s.scan1(f)",
    "s.scanChunks(init)(f)",
    "s.scanChunksOpt(init)(f)",
    "s.scanMap(f)",
    "s.scanMonoid",
    "s.scope",
    "s.sliding(size, step)",
    "s.spawn",
    "s.split(f)",
    "s.subscribe(subscriber)",
    "s.takeThrough(p)",
    "s.through2(s2)(f)",
    "s.timeoutOnPullTo(timeout, onTimeout)",
    "s.timeoutOnPullWith(timeout)(f)",
    "s.toPublisher",
    "s.toPublisherResource",
    "s.translate(u)",
    "s.translateInterruptible(u)",
    "s.unchunk",
    "s.withFilter(f)",
    "s.zipAll(that)(pad1, pad2)",
    "s.zipAllWith(that)(pad1, pad2)(f)",
    "s.zipRight(that)",
    "s.zipWithNext",
    "s.zipWithPreviousAndNext",
    "s.zipWithScan(z)(f)",
    "s.zipWithScan1(z)(f)",
    "s.covary[F2]",
    "s.observe(p)",
    "s.observeAsync(maxQueued)(pipe)",
    "s.observeEither(left, right)",
    "s.pull",
    "s.repeatPull(f)",
    "s.unitary",
    "s.unNone",
    "s.unNoneTerminate",
    "s.parJoinUnbounded",
    "s.apply[F]",
    "s.to(c)",
    "s.covaryId[F]",
    "s.lift",
    "s.to(c)",
    "s.toList",
    "s.toVector",
    "s.unsafeToPublisher",
    "Stream.attemptEval(fo)",
    "Stream.awakeDelay(period)",
    "Stream.awakeEvery(period, dampen)",
    "Stream.bracketWeak(acquire)(release)",
    "Stream.bracketCaseWeak(acquire)(release)",
    "Stream.bracketFull(acquire)(release)",
    "Stream.bracketFullWeak(acquire)(release)",
    "Stream.chunk(os)",
    "Stream.constant(o)",
    "Stream.eval_(fa)",
    "Stream.evalUnChunk(fo)",
    "Stream.evals(fo)",
    "Stream.evalSeq(fo)",
    "Stream.fixedDelay(period)",
    "Stream.fixedRate(period, dampen)",
    "Stream.fixedRateStartImmediately(period)",
    "Stream.fixedRateStartImmediately(period, dampen)",
    "Stream.fromOption",
    "Stream.fromEither",
    "Stream.fromIterator",
    "Stream.fromBlockingIterator",
    "Stream.fromQueueUnterminated(queue)",
    "Stream.fromQueueUnterminated(queue, limit)",
    "Stream.fromQueueUnterminatedChunk(queue)",
    "Stream.fromQueueUnterminatedChunk(queue, limit)",
    "Stream.fromQueueNoneTerminated(queue)",
    "Stream.fromQueueNoneTerminated(queue, limit)",
    "Stream.fromQueueNoneTerminatedChunk(queue)",
    "Stream.fromQueueNoneTerminatedChunk(queue, limit)",
    "Stream.fromPublisher",
    "Stream.foldable(os)",
    "Stream.force(f)",
    "Stream.iterable(os)",
    "Stream.iterateEval(start)(f)",
    "Stream.never",
    "Stream.range(start, stopExclusive)",
    "Stream.range(start, stopExclusive, step)",
    "Stream.ranges(start, stopExclusive, size)",
    "Stream.resourceK",
    "Stream.resourceWeak(r)",
    "Stream.resourceWeakK",
    "Stream.fromAutoCloseable(fo)",
    "Stream.fromAutoCloseableWeak(fo)",
    "Stream.retry(fo, delay, nextDelay, maxAttempts)",
    "Stream.sleep_(d)",
    "Stream.supervise(fa)",
    "Stream.suspend(s)",
    "Stream.unfoldChunk(s)(f)",
    "Stream.unfoldEval(s)(f)",
    "Stream.unfoldChunkEval(s)(f)",
    "Stream.unfoldLoop(start)(f)",
    "Stream.unfoldChunkLoop(start)(f)",
    "Stream.unfoldLoopEval(start)(f)",
    "Stream.unfoldChunkLoopEval(start)(f)",
    "Stream.parZip(left, right)",
    "s.compile.fold(init)(f)",
    "s.compile.foldSemigroup",
    "s.compile.foldChunks(init)(f)",
    "s.compile.lastOrError",
    "s.compile.to(collector)",
    "s.compile.resource"
  )

}
