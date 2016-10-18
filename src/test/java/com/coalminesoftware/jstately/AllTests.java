package com.coalminesoftware.jstately;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.coalminesoftware.jstately.graph.StateGraphTest;
import com.coalminesoftware.jstately.graph.composite.CompositeStateTest;
import com.coalminesoftware.jstately.graph.transition.DisjunctiveEqualityTransitionTest;
import com.coalminesoftware.jstately.graph.transition.EqualityTransitionTest;
import com.coalminesoftware.jstately.integration.IntegrationTest;
import com.coalminesoftware.jstately.integration.SubmachineTransitionTest;
import com.coalminesoftware.jstately.machine.StateMachineTest;

@RunWith(Suite.class)
@SuiteClasses({ CompositeStateTest.class,
		DisjunctiveEqualityTransitionTest.class, EqualityTransitionTest.class, 
		StateGraphTest.class,
		StateMachineTest.class,
		SubmachineTransitionTest.class, StateMachineTest.class, IntegrationTest.class})
public class AllTests { }
