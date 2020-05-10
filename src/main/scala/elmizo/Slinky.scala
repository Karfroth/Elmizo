package elmizo

import _root_.slinky.core.facade.ReactElement
import _root_.slinky.core.FunctionalComponent
import _root_.slinky.core.facade.Hooks.{useReducer}

object Slinky extends Elmizo[ReactElement] {

    private[Slinky] case object EmptyProp
    private[Slinky] type EmptyProp = EmptyProp.type

    def mkSimple[Model, Message]
    (init: () => Model)
    (update: Message => Model => Model)
    (view: Model => (Message => Unit) => ReactElement): ReactElement = {
        val program = FunctionalComponent[EmptyProp] { props => 
            def reducer(s: Model, msg: Message) = update(msg)(s)
            val (state, dispatch) = useReducer(reducer, init())
            view(state)(dispatch)
        }
        program(EmptyProp)
    }

    def mkProgram[Model, Message]
    (init: () => (Model, Cmd[Message]))
    (update: Message => Model => (Model, Cmd[Message]))
    (view: Model => (Message => Unit) => ReactElement): ReactElement = {
        val program = FunctionalComponent[EmptyProp] { props => 
            def reducer(s: (Model, Cmd[Message]), msg: Message) = update(msg)(s._1)
            val ((state, cmd), dispatch) = useReducer(reducer, init())
            Cmd.exec[Message](dispatch)(cmd)
            view(state)(dispatch)
        }
        program(EmptyProp)
    }
}