import StorageService.StorageService
import zio.macros.accessible
import zio.{Has, Task, UIO, ZIO, ZLayer}

@accessible
object StorageService {
  type StorageService = Has[Service]

  trait Service {
    def fetch(key: String): Task[String]
  }
}

@accessible
object SearchService {
  type SearchService = Has[Service]

  trait Service {
    def search(input: Seq[String], parallelism: Int): Task[Int]
  }

  val live: ZLayer[StorageService, Throwable, SearchService] =
    ZLayer.fromService[StorageService.Service, Service] { storageService =>
      new Service {
        override def search(input: Seq[String], parallelism: Int): Task[Int] = {
          Task
            .foreachParN(parallelism)(input) { input =>
              storageService.fetch(input)
            }
            .map(_.size)
        }
      }
    }
}
