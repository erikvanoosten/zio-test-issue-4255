import zio._
import zio.test._
import zio.test.Assertion._
import zio.test.mock.Expectation._
import zio.test.mock.mockable

//noinspection TypeAnnotation
object IssueSpec extends DefaultRunnableSpec {
  @mockable[Srv.Service]
  object SrvMock

  def method = Srv.mPar("b").zipPar(Srv.mPar("c"))

  def spec =
    suite("IssueSpec")(
      testM("failing test") {
        val mockEnv =
          SrvMock.MPar(equalTo("b"), value("bRes")) &&
            SrvMock.MPar(equalTo("c"), value("cRes"))

        assertM(method)(equalTo(("bRes", "cRes"))).provideLayer(mockEnv)
      },
      testM("successful test with a nasty hack (atMost(1))") {
        val mockEnv =
          SrvMock.MPar(equalTo("b"), value("bRes")).atMost(1) &&
            SrvMock.MPar(equalTo("c"), value("cRes"))

        assertM(method)(equalTo(("bRes", "cRes"))).provideLayer(mockEnv)
      },
    )
}
