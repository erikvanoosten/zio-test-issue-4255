import StorageService.StorageService
import zio.test.mock.{Mock, Proxy}
import zio.{Has, Task, URLayer, ZLayer}

object StorageSrvMock extends Mock[StorageService] {

  object Fetch extends Method[String, Nothing, String]

  val compose: URLayer[Has[Proxy], StorageService] =
    ZLayer.fromService { proxy =>
      new StorageService.Service {
        override def fetch(key: String): Task[String] = proxy(Fetch, key)
      }
    }

}
