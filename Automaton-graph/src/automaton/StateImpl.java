package automaton;

public class StateImpl implements State {
	private boolean initial, terminal;

	public StateImpl(boolean initial, boolean terminal) {
		this.initial = initial;
		this.terminal = terminal;
	}

	@Override
	public boolean initial() {
		// TODO Auto-generated method stub
		return initial;
	}

	@Override
	public boolean terminal() {
		// TODO Auto-generated method stub
		return terminal;
	}

	public void setInitial(boolean x) {
		initial = x;
	}

	public void setTerminal(boolean x) {
		terminal = x;
	}

}
