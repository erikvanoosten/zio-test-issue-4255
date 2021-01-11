import StorageService.StorageService
import zio._
import zio.test._
import zio.test.Assertion._
import zio.test.mock.Expectation._
import zio.test.mock.mockable

//noinspection TypeAnnotation
object IssueSpec extends DefaultRunnableSpec {

  val input: Seq[String] = "abcdefghijklmnopqrstuvwxyz".toSeq.map(_.toString)

  val mockEnv: ULayer[StorageService] = input
    .zipWithIndex
    .map { case (c, i) =>
      StorageSrvMock.Fetch(equalTo(c), value(i.toString))
    }
    .reduce(_ && _)

  def spec =
    suite("Issue 4225 Spec")(
      testM("failing test when run in parallel") {
        val program = SearchService.search(input, 20)
        val layers = mockEnv >>> SearchService.live
        assertM(program)(equalTo(26)).provideLayer(layers)
      },
      testM("successful test when run sequentially") {
        val program = SearchService.search(input, 1)
        val layers = mockEnv >>> SearchService.live
        assertM(program)(equalTo(26)).provideLayer(layers)
      },
    )
}
