import zio.macros.accessible
import zio.{Has, UIO}

@accessible
object Srv {
  type Srv = Has[Service]

  trait Service {
    def mPar(s: String): UIO[String]
  }
}
