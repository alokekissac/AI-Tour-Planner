"""Database helpers.

Two modes:
- MySQL (default for local development). Configure with env vars
  DB_HOST, DB_PORT, DB_USER, DB_PASSWORD, DB_NAME, or set DB_ENGINE=mysql.
- SQLite demo mode (used automatically on Vercel, or with DB_ENGINE=sqlite).
  A bundled copy of the Kerala demo data (database/demo.sqlite) is copied to
  /tmp on start-up, so the live demo works with no external database and
  resets itself whenever the serverless instance restarts.
"""
import os
import re
import shutil
import datetime

HERE = os.path.dirname(os.path.abspath(__file__))
ENGINE = os.environ.get("DB_ENGINE") or ("sqlite" if os.environ.get("VERCEL") else "mysql")

# ---------------------------------------------------------------- MySQL
MYSQL = dict(
    user=os.environ.get("DB_USER", "root"),
    password=os.environ.get("DB_PASSWORD", ""),
    host=os.environ.get("DB_HOST", "localhost"),
    database=os.environ.get("DB_NAME", "tour_planner"),
    port=int(os.environ.get("DB_PORT", "3307")),
)

# ---------------------------------------------------------------- SQLite
SQLITE_SRC = os.path.join(HERE, "demo.sqlite")
SQLITE_DB = os.environ.get("SQLITE_PATH", "/tmp/tour_planner.sqlite")


def _sqlite_conn():
    import sqlite3
    if not os.path.exists(SQLITE_DB):
        shutil.copy(SQLITE_SRC, SQLITE_DB)
    con = sqlite3.connect(SQLITE_DB)
    con.row_factory = lambda cur, row: {d[0]: row[i] for i, d in enumerate(cur.description)}
    con.create_function("curdate", 0, lambda: datetime.date.today().isoformat())
    con.create_function("now", 0, lambda: datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
    return con


def _sqlite_sql(q):
    # MySQL escapes quotes with \' ; SQLite uses ''
    return re.sub(r"\\'", "''", q)


def _connect():
    if ENGINE == "sqlite":
        return _sqlite_conn()
    import mysql.connector
    return mysql.connector.connect(**MYSQL)


def _run(q, kind):
    con = _connect()
    if ENGINE == "sqlite":
        q = _sqlite_sql(q)
        cur = con.cursor()
    else:
        cur = con.cursor(dictionary=True)
    cur.execute(q)
    if kind == "select":
        result = cur.fetchall()
    else:
        con.commit()
        result = cur.lastrowid if kind == "insert" else cur.rowcount
    cur.close()
    con.close()
    return result


def select(q):
    return _run(q, "select")


def insert(q):
    return _run(q, "insert")


def update(q):
    return _run(q, "update")


def delete(q):
    return _run(q, "delete")


# ---------------------------------------------------------------- uploads
# Vercel's filesystem is read-only except /tmp, so uploads go there in demo
# mode and are served by the /uploads/<name> route in main.py.
UPLOAD_DIR = "/tmp/uploads" if ENGINE == "sqlite" else os.path.join(HERE, "static")


def save_upload(file_storage, name):
    """Save an uploaded file; return the relative URL path stored in the DB."""
    os.makedirs(UPLOAD_DIR, exist_ok=True)
    file_storage.save(os.path.join(UPLOAD_DIR, name))
    return ("uploads/" if ENGINE == "sqlite" else "static/") + name
