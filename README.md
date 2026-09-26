# 🧭 AI Tour Planner

A full-stack tourism platform that connects **travellers, tour providers and local guides**, with AI features for multilingual travel: a chatbot, text translation, and OCR that reads and translates signs or menus from a photo.

It has a **Flask + MySQL** backend and web dashboards, plus a native **Android** app for travellers.

![Python](https://img.shields.io/badge/Python-3776AB?style=flat-square&logo=python&logoColor=white)
![Flask](https://img.shields.io/badge/Flask-000000?style=flat-square&logo=flask&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white)
![OpenAI](https://img.shields.io/badge/OpenAI-412991?style=flat-square&logo=openai&logoColor=white)
![OpenCV](https://img.shields.io/badge/OpenCV-5C3EE8?style=flat-square&logo=opencv&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=flat-square&logo=android&logoColor=white)

---

## ✨ Features

**Four roles, one platform**

| Role | What they can do |
|---|---|
| **Admin** | Approve tour providers & guides, manage place categories, view users, bookings, packages, ratings and complaints |
| **Tour provider** | Register, add places and tour packages, view bookings, answer enquiries |
| **Guide** | Register, mark locations on the map for travellers |
| **Traveller (Android app)** | Search places, save favourites, book tours and pay, rate & review, send enquiries/complaints, chat with guides |

**AI & ML features**

- 🤖 **AI travel chatbot**: an OpenAI-powered assistant inside the app; conversations are stored per user.
- 🌍 **Translate to any language**: in-app text translation via `deep-translator` (Google Translate).
- 📷 **Photo → text → translation**: upload a photo of a sign or menu; OpenCV + Tesseract OCR extract the text so it can be translated.
- 👁️ **Object detection**: YOLOv3 (COCO) experiment for recognising objects in images and video (`tour_planner_web/yolo/`).
- 📝 NLP text similarity with NLTK (stemming, stop-word removal, cosine similarity).

---

## 🏗️ Architecture

```
Android app (Java)  ──HTTP──▶  Flask REST API (/api/*)  ──▶  MySQL
                                     │
Web dashboards (Jinja2) ─────────────┤  admin · provider · guide
                                     │
                                     ├─▶ OpenAI (chatbot)
                                     ├─▶ Google Translate (deep-translator)
                                     └─▶ OpenCV + Tesseract (OCR)
```

```
.
├── tour_planner_web/       # Flask backend + web dashboards
│   ├── main.py             # app entry point (blueprints)
│   ├── api.py              # REST API used by the Android app
│   ├── admin.py · provider.py · guid.py · public.py
│   ├── opai.py             # OpenAI chatbot
│   ├── translatetoanylang.py
│   ├── templates/          # Jinja2 pages
│   └── yolo/               # YOLOv3 object-detection script
├── Tour_planner/           # Android app (Java, Android Studio)
└── database/schema.sql     # MySQL schema (no data)
```

---

## 🚀 Run it locally

**1. Database**

```bash
mysql -u root -p < database/schema.sql
```

The app expects MySQL on `localhost:3307`, user `root`, empty password. Change this in `tour_planner_web/database.py` if yours differs.

**2. Backend**

```bash
cd tour_planner_web
python -m venv .venv && source .venv/bin/activate
pip install -r requirements.txt
brew install tesseract          # for OCR (macOS)
export OPENAI_API_KEY=sk-...    # for the chatbot
python main.py                  # http://localhost:5819
```

**3. Android app**: open `Tour_planner/` in Android Studio and point the base URL at your machine's IP on port `5819`.

> YOLO weights are not in the repo (they're 240 MB). Download [`yolov3.weights`](https://pjreddie.com/media/files/yolov3.weights) into `tour_planner_web/yolo/yolo-coco/` to use object detection.

---

## 👤 Author

**Aloke**, AI Engineer & Full-Stack Developer · Dublin, Ireland
[GitHub](https://github.com/alokekissac) · [LinkedIn](https://www.linkedin.com/in/alokekisssac/)

Built during my Full-Stack Developer internship at Rizz Technologies.
