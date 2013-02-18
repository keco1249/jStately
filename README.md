jStately
========

Author: Brandon Rich

jStately is a basic state machine written in Java.  It does not aim to comply
with any one formalized specification (e.g. UML) or to serve as part of a
documentation or code generation tool.  Instead, jStately was written from a
utilitarian perspective, to be a light-weight, functioning part of Java
applications that need to manage non-trivial internal state.  In other words,
it aims to be "the working man's" state machine.

It was written with extensibility in mind.  Users implement/override methods
for events like entering a state, exiting a state or following a transition.
jStately relies heavily on interfaces but also provides sensible default
implementations.  For example, an API client could write their own Transition
implementation that determines whether it is valid based on arbitrary business
logic.  But the EqualityTransition implementation will appeal to users whose
transitions are based simply on whether an input is equal to a particular
value.

The project also contains a (currently less-than-well-tested) Transition
implementation, MvelTransition, that determines its validity for a given input
by evaluating an MVEL expression string.  MvelTransition was developed while
experimenting with the idea of parsing/unmarhsalling XML (or other human-
readable text) to construct state graphs using transitions with complex
validity tests.  I ultimately found XML configuration to be a little too
verbose for my tastes but a domain-specific language (DSL) for defining state
charts might be worth looking into.


Dependencies:
Apache Commons Lang
Google Guava
MVEL (for MvelTransition)
