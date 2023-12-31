package fixture.docker

import io.ktor.http.*
import mu.KotlinLogging
import org.testcontainers.containers.DockerComposeContainer
import java.io.File

private val log = KotlinLogging.logger {}

/**
 * apps - список приложений в docker-compose. Первое приложение - "главное", его url возвращается как inputUrl
 * (например ваш сервис при работе по rest или брокер сообщений при работе с брокером)
 * dockerComposeName - имя docker-compose файла (относительно ok-marketplace-acceptance/docker-compose)
 */
abstract class AbstractDockerCompose (
    private val apps: List<AppInfo>,
    private val dockerComposeName: String
): DockerCompose {

    constructor(service: String, port: Int, dockerComposeName: String)
            : this(listOf(AppInfo(service, port)), dockerComposeName)

    private val compose =
        DockerComposeContainer(getComposeFile()).apply {
            withOptions("--compatibility")
            apps.forEach { (service, port) ->
                withExposedService(
                    service,
                    port,
                )
            }
            withLocalCompose(true)
        }

    override val inputUrl: URLBuilder
        get() = getUrlBuilder(apps[0])

    override fun start() {
        compose.start()

        log.warn("\n=========== $dockerComposeName started =========== \n")
        apps.forEachIndexed { index, _ ->
            log.info { "Started docker-compose with App at: ${getUrlBuilder(apps[index])}" }
        }
    }

    override fun stop() {
        compose.close()
        log.warn("\n=========== $dockerComposeName complete =========== \n")
    }

    override fun clearDb() {
        log.warn("=========== clearDb ===========")
        // TODO сделать очистку БД, когда до этого дойдет
    }

    private fun getComposeFile(): File {
        val file = File("docker-compose/$dockerComposeName")
        if (!file.exists()) throw IllegalArgumentException("file $dockerComposeName not found!")
        return file
    }

    private fun getUrlBuilder(app: AppInfo) = URLBuilder(
        protocol = URLProtocol.HTTP,
        host = app.let { compose.getServiceHost(it.service, it.port) },
        port = app.let { compose.getServicePort(it.service, it.port) },
    )

    data class AppInfo(
        val service: String,
        val port: Int,
    )
}