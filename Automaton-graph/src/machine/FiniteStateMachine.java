package machine;

import automaton.DeterministicAutomaton;
import automaton.NotDeterministInitialStateException;
import automaton.NotDeterministTransitionException;
import automaton.State;
import automaton.Transition;
import automaton.UnknownInitialStateException;

public class FiniteStateMachine<T> extends DeterministicAutomaton<T> {

	public FiniteStateMachine(TransitionWithAction<T>[] transitions) throws NotDeterministTransitionException,
			UnknownInitialStateException, NotDeterministInitialStateException {
		super(transitions);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected State changeCurrentState(Transition<T> t) {
		return ((TransitionWithAction<T>) t).cross();
	}
}
