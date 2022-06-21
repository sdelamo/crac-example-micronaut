package example;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.http.server.netty.DefaultNettyEmbeddedServerFactory;
import io.micronaut.http.server.netty.NettyHttpServer;
import io.micronaut.http.server.netty.configuration.NettyHttpServerConfiguration;
import io.micronaut.http.server.netty.types.DefaultCustomizableResponseTypeHandlerRegistry;
import io.micronaut.http.server.netty.types.NettyCustomizableResponseTypeHandler;
import io.micronaut.http.server.netty.types.files.FileTypeHandler;
import io.micronaut.runtime.server.EmbeddedServer;
import jakarta.inject.Singleton;
import org.crac.Context;
import org.crac.Core;
import org.crac.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Singleton
@Replaces(EmbeddedServer.class)
public class CracEnhancedNettyHttpServer extends NettyHttpServer implements Resource {

    private static final Logger LOG = LoggerFactory.getLogger(CracEnhancedNettyHttpServer.class);

    /**
     * @param serverConfiguration   The Netty HTTP server configuration
     */
    public CracEnhancedNettyHttpServer(
            DefaultNettyEmbeddedServerFactory serverFactory,
            NettyHttpServerConfiguration serverConfiguration
    ) {
        super(
                serverConfiguration,
                serverFactory,
                new DefaultCustomizableResponseTypeHandlerRegistry(new FileTypeHandler(serverConfiguration.getFileTypeHandlerConfiguration())),
                true
        );
        Core.getGlobalContext().register(this);
    }

    @Override
    @SuppressWarnings("resource")
    public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
        LOG.info("CracEnhancedNettyHttpServer.beforeCheckpoint() {}", context);
        if (isRunning()) {
            stop();
        }
    }

    @Override
    @SuppressWarnings("resource")
    public void afterRestore(Context<? extends Resource> context) throws Exception {
        LOG.info("CracEnhancedNettyHttpServer.afterRestore() {}", context);
        start();
    }
}
