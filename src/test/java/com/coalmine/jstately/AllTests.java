package com.coalmine.jstately;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.coalmine.jstately.graph.OldStateGraphTest;
import com.coalmine.jstately.graph.transition.MvelTransitionTest;
import com.coalmine.jstately.machine.CompositeStateTest;
import com.coalmine.jstately.machine.SubmachineStateTest;


@RunWith(Suite.class)
@SuiteClasses({ MvelTransitionTest.class, OldStateGraphTest.class, SubmachineStateTest.class, CompositeStateTest.class })
public class AllTests {

}


