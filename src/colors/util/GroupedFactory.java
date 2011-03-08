package colors.util;

import colors.interfaces.Factory;

public class GroupedFactory<T> implements Factory<T> {
	private final Counter<Factory<? extends T>> factories;
	
	public GroupedFactory(Counter<Factory<? extends T>> factories) {
		this.factories = factories;
	}

	@Override
	public T instantiate() {
		return factories.sample().instantiate();
	}

}
