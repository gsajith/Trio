from test.test import get_word
from twisted.python import log
from cyclone import template
from cyclone.auth import GoogleMixin
import cyclone.web
import sys
import functools

from twisted.internet import reactor
from twisted.python import log

def authrequired(method):
    def wrapper(self, *args, **kwargs):
        raise cyclone.web.HTTPError(403)

    return wrapper


class GoogleHandler(cyclone.web.RequestHandler, GoogleMixin):
    @cyclone.web.asynchronous
    def get(self):
        if self.get_argument("openid.mode", None):
            self.get_authenticated_user(self.async_callback(self._on_auth))
            return
        self.authenticate_redirect()

    def _on_auth(self, user):
        if not user:
            raise cyclone.web.HTTPError(500, "Google Auth Error")
        log.msg(user)
        

class MainHandler(cyclone.web.RequestHandler):
    #@authpage
    def get(self):
        self.render("templates/test.html", word=get_word())

if __name__ == "__main__":
    application = cyclone.web.Application([
        (r"/", MainHandler),
        (r"/login", GoogleHandler),
    ])

    log.startLogging(sys.stdout)
    reactor.listenTCP(80, application)
    reactor.run()
