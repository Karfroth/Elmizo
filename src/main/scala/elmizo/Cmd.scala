package elmizo

import scala.concurrent.Future
import scala.util.{Try, Success, Failure}
import scala.concurrent.ExecutionContext

object Cmd {

    /// Execute the commands using the supplied dispatcher
    private[elmizo] def exec[Message](dispatch:Dispatch[Message])(cmd:Cmd[Message]) =
        cmd.foreach(sub => sub(dispatch))

    def none[Message](): Cmd[Message] = List.empty
    def map[A, Message](f: A => Message)(cmd: Cmd[A]): Cmd[Message] = {
        val h = (dispatch: Dispatch[Message]) => dispatch compose f
        cmd.map(g => g compose h)
    }
    def batch[Message](cmds: List[Cmd[Message]]): Cmd[Message] = cmds.flatten
    def ofSub[Message](sub: Sub[Message]): Cmd[Message] = List(sub)

    object OfFunc {
        def either[A, B, Message](f: A => B)(args: A)(onSuccess: B => Message)(onFailure: Throwable => Message): Cmd[Message] = {
            val sub = (dispatch: Dispatch[Message]) =>
                Try(f(args)) match {
                    case Success(v) => dispatch(onSuccess(v))
                    case Failure(e) => dispatch(onFailure(e))
                }
            List(sub)
        }
        // TODO: Implement More
    }

    // TODO: Cats? ZIO? MONIX?
    object OfFuture {
        def either[A, B, Message](f: A => Future[B])(args: A)(onSuccess: B => Message)(onFailure: Throwable => Message)(implicit ec: ExecutionContext): Cmd[Message] = {
            val sub = (dispatch: Dispatch[Message]) =>
                f(args).onComplete{
                case Success(v) => dispatch(onSuccess(v))
                case Failure(e) => dispatch(onFailure(e))
            }
            List(sub)
        }
        // TODO: Implement More
    }
}