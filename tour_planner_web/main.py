from flask import *
from database import *
from public import public
from admin import admin
from provider import provider
from guid import guid
from api import api
import sys
import io
# from translatetoanylang import translatetoanylang



app=Flask(__name__)
app.secret_key='key'
app.register_blueprint(public)
app.register_blueprint(admin,url_prefix='/admin')
app.register_blueprint(provider,url_prefix='/provider')
app.register_blueprint(guid,url_prefix='/guid')
app.register_blueprint(api,url_prefix='/api')



app.run(debug=True,port=5819,host="0.0.0.0")

