package elmizo

private[elmizo] trait Elmizo[ViewElement] {
    def mkSimple[Model, Message]
    (init: () => Model)
    (update: Message => Model => Model)
    (view: Model => (Message => Unit) => ViewElement): ViewElement

    def mkProgram[Model, Message]
    (init: () => (Model, Cmd[Message]))
    (update: Message => Model => (Model, Cmd[Message]))
    (view: Model => (Message => Unit) => ViewElement): ViewElement
}