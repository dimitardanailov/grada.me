package libraries.GooglePlus.Enums;

public enum GooglePlusStates {
    /**
     * STATE_DEFAULT:
     * The default state of the application before the user
     * has clicked 'sign in', or after they have clicked
     * 'sign out'.  In this state we will not attempt to
     * resolve sign in errors and so will display our
     * Activity in a signed out state.
     */
    STATE_DEFAULT,

    /**
     * STATE_SIGN_IN:
     * This state indicates that the user has clicked 'sign
     * in', so resolve successive errors preventing sign in
     * until the user has successfully authorized an account
     * for our app.
     */
    STATE_SIGN_IN,

    /**
     * STATE_IN_PROGRESS:
     * This state indicates that we have started an intent to
     * resolve an error, and so we should not start further
     * intents until the current intent completes.
     */
    STATE_IN_PROGRESS,
}
