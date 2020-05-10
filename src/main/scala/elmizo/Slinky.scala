package elmizo

import _root_.slinky.core.facade.ReactElement
import _root_.slinky.core.FunctionalComponent
import _root_.slinky.core.facade.Hooks.{useState}

object Slinky extends Elmizo[ReactElement] {
    def mkSimple[Model, Message]
    (init: () => Model)
    (update: Message => Model => Model)
    (view: Model => (Message => Unit) => ReactElement): ReactElement = {
        // TODO: Make dispatch returns Dispatch instance
        val program = FunctionalComponent[EmptyProp] { props => 
            val (state, updateState) = useState(init())
            def dispatch(msg: Message) = {
                updateState(update(msg)(state))
            }
            view(state)(dispatch)
        }
        program(EmptyProp)
    }

    def mkProgram[Model, Message]
    (init: () => (Model, Cmd[Message]))
    (update: Message => Model => (Model, Cmd[Message]))
    (view: Model => (Message => Unit) => ReactElement): ReactElement = {
        val program = FunctionalComponent[EmptyProp] { props => 
            // TODO: Add cmd processor
            val (initModel, cmd) = init()
            val (state, updateState) = useState(initModel)
            def dispatch(msg: Message) = {
                val (updatedModel, cmd) = update(msg)(state)
                updateState(updatedModel)
            }
            view(state)(dispatch)
        }
        program(EmptyProp)
    }
}