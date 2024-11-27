package com.cicd_vertx.cicd_vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerRequest;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      String authorization = req.getHeader("Authorization");

      if (authorization == null || !authorization.startsWith("Bearer ")) {
        req.response()
          .setStatusCode(401)
          .putHeader("content-type", "text/plain")
          .end("Unauthorized: Missing or invalid Authorization header");
        return;
      }

      String token = authorization.substring(7);

      if ("my-secret-token".equals(token)) {
        req.response()
          .putHeader("content-type", "text/plain")
          .end("Hello from Vert.x! You are authorized.");
      } else {
        req.response()
          .setStatusCode(401)
          .putHeader("content-type", "text/plain")
          .end("Unauthorized: Invalid token");
      }
    }).listen(8888).onComplete(http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}
