import os
from flask import Flask, send_from_directory
from database import UPLOAD_DIR, STATIC_DIR
from public import public
from admin import admin
from provider import provider
from guid import guid
from api import api

app = Flask(__name__, static_folder=STATIC_DIR, static_url_path='/static')
app.secret_key = os.environ.get("SECRET_KEY", "dev-only-change-me")
app.register_blueprint(public)
app.register_blueprint(admin, url_prefix='/admin')
app.register_blueprint(provider, url_prefix='/provider')
app.register_blueprint(guid, url_prefix='/guid')
app.register_blueprint(api, url_prefix='/api')


@app.route('/uploads/<path:name>')
def uploads(name):
    """Files uploaded while running in demo mode (stored in /tmp)."""
    return send_from_directory(UPLOAD_DIR, name)


if __name__ == '__main__':
    app.run(debug=True, port=5819, host="0.0.0.0")
