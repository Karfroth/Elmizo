package elmizo

private[elmizo] trait Elmizo[ViewElement] {
    def mkSimple[Model, Message]
    (init: () => Model)
    (update: Message => Model => Model)
    (view: Model => Dispatch[Message] => ViewElement): ViewElement

    def mkProgram[Model, Message]
    (init: () => (Model, Cmd[Message]))
    (update: Message => Model => (Model, Cmd[Message]))
    (view: Model => Dispatch[Message] => ViewElement): ViewElement
}