package com.coalmine.jstately;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.coalmine.jstately.graph.StateGraphTest;
import com.coalmine.jstately.graph.composite.CompositeStateTest;
import com.coalmine.jstately.graph.transition.DisjunctiveEqualityTransitionTest;
import com.coalmine.jstately.graph.transition.EqualityTransitionTest;
import com.coalmine.jstately.graph.transition.MvelTransitionTest;
import com.coalmine.jstately.machine.StateMachineTest;


@RunWith(Suite.class)
@SuiteClasses({ CompositeStateTest.class,
		DisjunctiveEqualityTransitionTest.class, EqualityTransitionTest.class, MvelTransitionTest.class, 
		StateGraphTest.class,
		StateMachineTest.class })
public class AllTests { }


