jStately
========

By Brandon Rich

jStately is a state machine written in Java. There are a few graphic tools out
there that can churn out generated state machine code in a variety of languages
including Java. But the goal of jStately is to allow developers to "hand craft"
maintainable, readable, concise state graphs in an object-oriented way.

It was written with OOD principles and extensibility in mind. Users build
`StateGraphs` made up primarily of `States` and `Transitions`, implementing
methods for events like entering a state, exiting a state or following a
transition. jStately relies heavily on interfaces but also provides sensible
default implementations. For example, a user could implementation a
`Transition` that determines whether it is valid for a given input based on
arbitrary business logic. But the provided `EqualityTransition` implementation
will appeal to users whose transitions are based simply on whether an input is
equal to a particular expected value.

Although jStately is not directly based on a particular formal definition of a
state machine, some enhancements over traditional finite state machines are
present. For example, a `CompositeState` defines a collection of states and/or
nested `CompositeState`s, similar to hierarchical nested states in UML.
`CompositeState`s can have their own entry and exit behavior, as well as
transitions that can apply from any state contained (directly or indirectly) in
the `CompositeState`.
