def isEven(i: Int): Boolean = i % 2 == 0

def countNumbers(max: Int, condition: Int => Boolean): Int =
  List.range(0, max).filter(condition).size

countNumbers(5, isEven)

assert(countNumbers(5, isEven) == 3)
assert(List.range(0, 5) == List(0, 1, 2, 3, 4))
assert(List(0, 1, 2, 3, 4).filter(isEven) == List(0, 2, 4))
assert(List(0, 2, 4).size == 3)

def countNumbers(until: Int, condition: Int => Boolean): Int = {
  var count: Int = 0
  var i: Int = 0
  while (i < until) {
    if (condition(i)) count = count + 1
    i = i + 1
  }
  count
}

def countNumbers(max: Int, condition: Int => Boolean): Int = {
  def go(count: Int, i: Int): Int = {
    if (i < max)
      if (condition(i)) then go(count + 1, i + 1) else go(count, i + 1)
    else
      count
  }
  go(0, 0)
}

def countNumbers(max: Int, condition: Int => Boolean): Int =
  fs2.Stream.range(0, max).filter(condition).compile.count.toInt