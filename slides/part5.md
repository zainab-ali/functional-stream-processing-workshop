
class: center, middle

# Supercharge your streams
## Zainab Ali
### Part 5: Simulation game

???

Start time: 16:30

---
class: middle
# Part 5: Simulation game
 - Stream processing in game engines
 - Error handling, logging, and metrics
 - Your own experiments

???

TODO: Have a recap before this.

---
class: center, middle
# Live demo

---

# Stream processing in game engines
 - A render loop draws to the screen.
 - A simulation loop updates game state over time.
 - An action loop handles player input.


---
# The render loop
 - Renders draws every 20 milliseconds
 - Calls a designer-specified `render` function.
 - Uses doodle to draw pictures.

# The simulation loop
 - Updates game state.
 - Whenever the designer wants.
 
# The action loop
 - Reads player input from `stdin`
 - Starts designer-specified state updates.
 
---

# Exercise

Implement the `run` function.

???

---
class: center, middle

# What can raise errors?
## How should the `GameApp` handle them?

???

Start time: 17:00

---
class: center, middle

# Exercise

Log messages to a `game.log` file

Start from the logger defined in part 4.

???

---
class: center, middle

# Exercise

Record metrics to a `metrics.log` file.

 - Record the number of inputs.
 - Record the average time of an action stream.

???

Start time: 17:00
