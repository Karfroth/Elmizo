package playground

import elmizo.{Cmd, Slinky}

import scala.concurrent.Future
import slinky.core._
import slinky.core.facade.Hooks._
import slinky.web.html._

object Counter {
    case object EmptyCounterProp
    type EmptyCounterProp = EmptyCounterProp.type

    sealed trait Message
    case object Increase extends Message
    case object Decrease extends Message
    final case class Print(value: String) extends Message

    type Model = Int

    def init() = (0, Cmd.none)

    def invokeOfFuture(value: Int): Cmd[Message] = {
        import scala.concurrent.ExecutionContext.Implicits.global
        def toFutureString(v: Int) = Future(v.toString())
        def onSuccess(str: String) = Print(s"Future String: ${str}")
        def onFailure(e: Throwable) = Print("Failed")

        Cmd.OfFuture.either(toFutureString)(value)(onSuccess)(onFailure)
    }

    def invokeOfFunction(value: Int): Cmd[Message] = {
        def toFutureString(v: Int) = v.toString()
        def onSuccess(str: String) = Print(s"Func String: ${str}")
        def onFailure(e: Throwable) = Print("Failed")

        Cmd.OfFunc.either(toFutureString)(value)(onSuccess)(onFailure)
    }

    def update(msg: Message)(model: Model): (Model, Cmd[Message]) = msg match {
        case Increase => (model + 1, (invokeOfFuture(model + 1)))
        case Decrease => (model - 1, (invokeOfFunction(model - 1)))
        case Print(value) => 
            println(s"value received: ${value}")
            (model, Cmd.none)
    }

    def view(model: Model)(dispatch: Message => Unit) =
        div(className := "counter")(
            p(model)
        ,   button(`type` := "button", onClick := (_ => dispatch(Increase)))("+")
        ,   button(`type` := "button", onClick := (_ => dispatch(Decrease)))("-")
        )

    def program = Slinky.mkProgram(init)(update)(view)
}