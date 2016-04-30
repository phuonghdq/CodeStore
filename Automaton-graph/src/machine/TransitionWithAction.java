package machine;

import automaton.State;
import automaton.Transition;
import automaton.TransitionImpl;

public class TransitionWithAction<T> implements Transition<T> {

	private final TransitionImpl<T> transition;
	private final Action<T> action;

	public TransitionWithAction(TransitionImpl<T> t, Action<T> a) {
		this.transition = t;
		this.action = a;
	}

	public TransitionImpl<T> getTransition() {
		return transition;
	}

	@Override
	public State source() {
		// TODO Auto-generated method stub
		return transition.source();
	}

	@Override
	public State target() {
		// TODO Auto-generated method stub
		return transition.target();
	}

	@Override
	public T label() {
		// TODO Auto-generated method stub
		return transition.label();
	}

	public State cross() {
		action.execute(transition.label());
		return transition.target();
	}

	public void setSource(State s) {
		transition.setSource(s);
	}

	public void setTarget(State t) {
		transition.setTarget(t);
	}

	public void setLabel(T lb) {
		// TODO Auto-generated method stub
		transition.setLabel(lb);
	}
}
