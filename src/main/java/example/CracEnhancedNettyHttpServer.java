package example;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.Internal;
import io.micronaut.http.server.netty.NettyEmbeddedServices;
import io.micronaut.http.server.netty.NettyHttpServer;
import io.micronaut.http.server.netty.configuration.NettyHttpServerConfiguration;
import io.micronaut.http.server.netty.types.NettyCustomizableResponseTypeHandlerRegistry;
import jakarta.inject.Singleton;
import org.crac.Context;
import org.crac.Core;
import org.crac.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Internal
@Replaces(NettyHttpServer.class)
public class CracEnhancedNettyHttpServer extends NettyHttpServer implements Resource {

    private static final Logger LOG = LoggerFactory.getLogger(CracEnhancedNettyHttpServer.class);

    /**
     * @param serverConfiguration   The Netty HTTP server configuration
     * @param nettyEmbeddedServices The embedded server context
     * @param handlerRegistry       The handler registry
     * @param isDefault             Is this the default server
     */
    public CracEnhancedNettyHttpServer(
            NettyHttpServerConfiguration serverConfiguration,
            NettyEmbeddedServices nettyEmbeddedServices,
            NettyCustomizableResponseTypeHandlerRegistry handlerRegistry,
            boolean isDefault
    ) {
        super(serverConfiguration, nettyEmbeddedServices, handlerRegistry, isDefault);
        Core.getGlobalContext().register(this);
    }


    @Override
    @SuppressWarnings("resource")
    public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
        LOG.info("CracEnhancedNettyHttpServer.beforeCheckpoint()");
        if (isRunning()) {
            stop();
        }
    }

    @Override
    @SuppressWarnings("resource")
    public void afterRestore(Context<? extends Resource> context) throws Exception {
        LOG.info("CracEnhancedNettyHttpServer.afterRestore()");
        start();
    }
}
