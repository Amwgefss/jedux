package com.example.jedux;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import static trikita.anvil.DSL.*;

import trikita.anvil.RenderableView;
import trikita.jedux.Action;
import trikita.jedux.Logger;
import trikita.jedux.Store;

public class MainActivity extends Activity {

    // Define store type
    class State {
        public final int count;
        public State(int count) {
            this.count = count;
        }
        public String toString() {
            return "count = " + this.count;
        }
    }

    // Define action types
    enum CounterAction {
        INCREMENT,
        PLUS,
    }

    private Store<Action<CounterAction, ?>, State> store;

    // Create views
    public void onCreate(Bundle b) {
        super.onCreate(b);
        store = new Store<>(this::reduce, // reducer function
                new State(0),             // initial state
                new Logger("Counter"));   // Middleware: logger
        setContentView(new RenderableView(this) {
            public void view() {
                linearLayout(() -> {
                    orientation(LinearLayout.VERTICAL);
                    // Bind count value to a text view
                    textView(() -> {
                        size(FILL, WRAP);
                        text("Count: " + store.getState().count);
                    });
                    // Bind actions
                    button(() -> {
                        size(FILL, WRAP);
                        text("+1");
                        // Action without arguments
                        onClick(v -> store.dispatch(new Action<>(CounterAction.INCREMENT)));
                    });
                    button(() -> {
                        size(FILL, WRAP);
                        text("+10");
                        // Action with arguments
                        onClick(v -> store.dispatch(new Action<>(CounterAction.PLUS, 10)));
                    });
                });
            }
        });
    }

    public State reduce(Action<CounterAction, ?> action, State old) {
        switch (action.type) {
            case INCREMENT:
                return new State(old.count + 1);
            case PLUS:
                return new State(old.count + (Integer) action.value);
        }
        return old;
    }
}
