# interchange

*Trying a new kind of R&D Days*

We are `CODE.STΛR`: we code everything `.*`, but we also try to train our skills and try a bit of everything `.*`.

This is a project for trying out a new format of R&D Days structure, focused a bit more on the long-term.


## Project Resources

* Trello board: https://trello.com/b/dpYsMTtq
* AWS Organisation: codestarnl (https://codestarnl.signin.aws.amazon.com/console)

## Current project idea

After brainstorming for a bit, this is what we came up with. Our rationale was to find something not too complex 
so that we can focus on learning, but still interesting enough to make us excited about it, with potential for 
expanding upon later:

> *"Analyzing real-time road (or train) data to reactively route people optimally"*

It would look like a trajectory planner/scheduler (like Google Maps and/or 9292) but with a twist: instead of
always calculating the shortest trip, the app would take into account traffic patterns (and potentially expandable
to weather data) to spread routes outside of the usual ones (you could imagine this being a system to provide routes 
to self-driving cars that have no choice but to obey).

As a result (for car travel for example), this would **reduce the average travel time** of people globally, 
instead of minimizing distance, and eliminate traffic jams entirely (in an ideal world).

Since this app would have a very variable number of users depending on peak hours, it would need to be (dynamically)
scalable, and this is where we shine with our stateless reactive functional programming.

To achieve this goal we would need to make use of Kafka, reactive streams, Kubernetes, AWS, etc.

And because we are already familiar on how to do this kind of product with Scala, we will try the up-and-coming
Kotlin, which still falls in our domain and has potential with clients (we're not looking to drop Scala,
but having options is nice).

**On the front-end side**, options are still open, but as an initial proposal, to do things differently this time
we could opt not to do things in React/Angular again and explore another framework (Web Components?). And if not,
we could try not using JS/TS and instead try out Reason. 

Or, we could take a radically different front-end approach, and make an Android application
(this still counts as front-end!), **also using Kotlin**. As a bonus this would mean that we could then share
knowledge between front-enders and back-enders on the language!

