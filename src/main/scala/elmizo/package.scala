package object elmizo {

    /// Dispatch - feed new message into the processing loop
    type Dispatch[Message] = Message => Unit
    /// Subscription - return immediately, but may schedule dispatch of a message at any time
    type Sub[Message] = Dispatch[Message] => Unit
    /// Cmd - container for subscriptions that may produce messages
    type Cmd[Message] = List[Sub[Message]]

    //TODO: Add Program Type Definition

}
