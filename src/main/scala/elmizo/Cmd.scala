package elmizo

import scala.concurrent.Future

sealed trait Cmd[Message]

object Cmd {
    case object none extends Cmd[Nothing]

    final case class ofMsg[Message](msg: Message) extends Cmd[Message]

    private[Cmd] final class ofFunc[A, Message](func: A => Message) extends Cmd[Message]
    object ofFunc {
        def apply[A, Message](func: A => Message) = new ofFunc(func)
    }

    private[Cmd] final class ofFuture[Message](msg: Future[Message]) extends Cmd[Message]
    object ofFuture {
        def apply[Message](func: Future[Message]) = new ofFuture(func)
    }

    private[Cmd] final class map[A, Message](func: A => Message)(cmd: Cmd[A])
    object map {
        def apply[A, Message](func: A => Message)(cmd: Cmd[A]): Cmd.map[A, Message] = new map(func)(cmd)
    }
}