# Jedux - Redux architecture for Android

In the best traditions of [Redux](https://redux.js.org), Jedux is a very small and very flexible implementation.

Best used with [Anvil](https://github.com/zserge/anvil), which is React-like
library for Android.

## Installation

```gradle
repositories {
	maven { url = "https://jitpack.io" }
}

dependencies {
	compile "com.github.trikita:jedux:+"
}
```

## Apps built with Jedux

* [Talalarmo] alarm clock - https://github.com/trikita/talalarmo
* [Slide] text presentation maker - https://github.com/trikita/slide

## Example

```java
// Define state type
class State {
		public final int count;
		public State(int count) {
				this.count = count;
		}
}

// Define action types
enum CounterAction {
		INCREMENT,
		PLUS,
}

// Create store providing reducer and initial state
private Store<Action<CounterAction, ?>, State> store = new Store(this::reduce, new State(0));

// Reducer: transforms old state into a new one depending on the action type and value
public State reduce(Action<CounterAction, ?> action, State old) {
		switch (action.type) {
				case INCREMENT:
						return new State(old.count + 1);
				case PLUS:
						return new State(old.count + (Integer) action.value);
		}
		return old;
}

// Using Anvil you can bind store variables to some view properties
textView(() -> {
	text("Count: " + store.getState().count);
});

// Submit an action (e.g. from your event listeners)
button(() -> {
	onClick(v -> store.dispatch(new Action<>(CounterAction.INCREMENT)));
});
button(() -> {
	onClick(v -> store.dispatch(new Action<>(CounterAction.PLUS, 10)));
});
```

## API

trikita.jedux.Store:

* `new Store(reducer, initialState, middlewares...)` - create new store
* `store.dispatch(action)` - sends action message to the store, returns new state
* `store.getState()` - returns current state

trkita.jedux.Action (you are free to use your own action types!):

* `new Action<>(type, value)` - creates an action with given type (enum) and value (any kind of object).

trikita.jedix.Store.Middleware - implenent this interface to add custom
middleware (e.g. to handle actions with side effects or talk with your
controllers). Here's an example of the builtin Logger middleware, that logs
every incoming action and dumps state after the action is dispatched:

```java
public class Logger<A, S> implements Store.Middleware<A, S> {
    private final String tag;
    public Logger(String tag) {
        this.tag = tag;
    }
    public void dispatch(Store<A, S> store, A action, Store.NextDispatcher<A> next) {
        Log.d(tag, "--> " + action.toString());
        next.dispatch(action);
        Log.d(tag, "<-- " + store.getState().toString());
    }
}
```

To keep the state immutable consider using [Immutables](https://immutables.github.io) or [Dexx](https://github.com/andrewoma/dexx).

## License

Code is distributed under MIT license, feel free to use it in your proprietary
projects as well.
