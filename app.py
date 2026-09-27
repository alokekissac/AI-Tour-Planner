"""Vercel entrypoint.

Vercel auto-detects a Flask instance named `app` in app.py at the repo root.
The application itself lives in tour_planner_web/; this file just exposes it.
Run locally with:  python tour_planner_web/main.py
"""
import os
import sys

sys.path.insert(0, os.path.join(os.path.dirname(os.path.abspath(__file__)), "tour_planner_web"))

from main import app  # noqa: E402,F401
