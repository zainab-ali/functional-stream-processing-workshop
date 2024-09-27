
class: center, middle

# Supercharge your streams
## Zainab Ali
### Part 4: Streams at scale

---
class: middle
# Part 4: Streams at scale
 - Scaling challenges in event-driven systems
 - Designing reliable systems
 - Backpressure strategies: queues and channels
 - 🍵 10 minute break

???

TODO: Have a recap before this.

---

# Scaling challenges
## [Job switching](https://www.youtube.com/watch?v=K3axU2b0dDk)
???

Start time: 15:00

---
class: center, middle

# Is this familiar?

???

Ask for examples of load.

---
class: middle

# Examples
 - Button mashing in a game.
 - Spikes in traffic to a webserver.
 - Video streaming in a browser.

---
class: center, middle

# Analysis
## [Job switching](https://www.youtube.com/watch?v=K3axU2b0dDk)

???

 - 00:20 — Ethel and Lucy can easily wrap the candy on time.
 - 00:26 - Ethel doesn't have enough to do.
 - 00:36 - Ethel loses the first candy. She prepares an area on the table to store unwrapped candy.
 - 00:44 - Ethel realizes that she will not be able to wrap the stored candy. She purposefully eats one.
 - 00:44 - Ethel and Lucy place the stored candy in their hats. 
 - 01:41 - The supervisor checks on the process. She doesn't see the stored candy and concludes that the candy is being wrapped well.
 - 01:45 - In response, the candy production rate is sped up.

---
class: middle

# Bad design
 - Lucy doesn't control the rate of work.
 - Lucy has no pre-defined overflow strategy.
 - The supervisor has poor visiblity.

---
class: middle
# Good design

 - Well-defined response to load.
 - Buffer strategy.
 - Predictable behaviour on error.
 
???

Start time: 15:25

---
class: middle
# Push systems
 - Elements are "pushed" from a producer to a consumer.
 - Elements may be buffered.
 - Require backpressure.
   - Consumer communicates rate to the producer.
   
# Examples
 - Job switching
 - TCP
 - Browser Websocket API

---
class: middle

# Pull systems
- No concept of consumer and producer.
- Similar to function call and return.
- Elements are pulled by the caller.
- The callee may block.
- Pulling is not polling.

# Examples
 - fs2

---
class: center, middle
# Why are pull systems better?

---
class: middle

# Pull system advantages

 - Easy to reason about
 - No backpressure required
 - Well-defined error handling
 - Well-defined termination

---
class: middle

# A problem
 - Pull systems are easy to reason about.
 - Real world systems are push.
 - Interfacing between push and pull is needed.

???

Backpressure strategies

Start time: 15:50

---
class: center, middle
# Live coding
## `Channel.sc`

---
class: center, middle
# Exercise
## `Logger.test.scala`

---
class: center, middle
# What have you learned?

---
class: center, middle

# Coffee break

???

End time: 16:20

---
class: center, middle

# Thank you!
 - Newsletter: [buttondown.email/zainab](https://buttondown.email/zainab)
 - Email: zainab@duskimpression.com
 - LinkedIn: [zainab-ali-fp](https://uk.linkedin.com/in/zainab-ali-fp)
 - GitHub: zainab-ali

---
class: center, middle

# I hope you enjoyed it!
 - Please leave a review
 - Recommend this training
