jStately
========

Author: Brandon Rich

jStately is a state machine written in Java.  There are a few graphic tools out
there that can churn out generated state machine code in a variety of languages
including Java.  But the goal of jStately is to allow developers to "hand craft"
readable, concise, maintainable state graphs in an object-oriented way.

It was written with OOD principles and extensibility in mind.  Users build
StateGraphs made up primarily of States and Transitions, implementing methods
for events like entering a state, exiting a state or following a transition.
jStately relies heavily on interfaces but also provides sensible default
implementations.  For example, a user could write their own Transition
implementation that determines whether it is valid based on arbitrary business
logic.  But the provided EqualityTransition implementation will appeal to users
whose transitions are based simply on whether an input is equal to a particular
value.

The project also contains a (currently less-than-well-tested) Transition
implementation, MvelTransition, that determines its validity for a given input
by evaluating an [MVEL](http://mvel.codehaus.org/) expression.  MvelTransition
was developed while experimenting with the idea of parsing human-readable text
(e.g., XML) to build state graphs that have transitions with arbitrary validity
tests.  I ultimately found XML configuration to be too verbose for my tastes but
a domain-specific language (DSL) for defining state charts may still be worth
looking into.


