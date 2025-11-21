#!/usr/bin/python
import logging
import os
from aiohttp import web

app = web.Application()


async def hello(req):
    return web.Response(
        body="Hello World",
        status=200,
    )


async def someexception(req):
    1 / 0


async def health(req):
    return web.Response(status=200, body="OK")


async def app_factory():
    app.router.add_route("*", "/health", health)
    app.router.add_route("*", "/exception", someexception)
    app.router.add_route("*", "/", hello)
    return app


if __name__ == "__main__":
    print(
        f"Running on {os.environ.get('API', f'http://0.0.0.0:{os.environ.get("PORT",8080)}')}"
    )
    web.run_app(app_factory(), port=int(os.environ.get("PORT", 8080)))
