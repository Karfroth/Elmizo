package playground

import elmizo.Slinky

import slinky.core._
import slinky.web.html._
import slinky.core.facade.Hooks._

object Counter {
    case object EmptyCounterProp
    type EmptyCounterProp = EmptyCounterProp.type

    sealed trait Message
    case object Increase extends Message
    case object Decrease extends Message

    type Model = Int

    def init() = 0

    def update(msg: Message)(model: Model): Model = msg match {
        case Increase => model + 1
        case Decrease => model - 1
    }

    def view(model: Model)(dispatch: Message => Unit) =
        div(className := "counter")(
            p(model)
        ,   button(`type` := "button", onClick := (_ => dispatch(Increase)))("+")
        ,   button(`type` := "button", onClick := (_ => dispatch(Decrease)))("-")
        )

    def program = Slinky.mkSimple(init)(update)(view)
}