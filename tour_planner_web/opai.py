import os
import openai
from flask import Flask, request, redirect, url_for, render_template
# Set your OpenAI API key
openai.api_key = os.environ.get("OPENAI_API_KEY")

def openai_create(prompt):
    response = openai.Completion.create(
        model="gpt-3.5-turbo-instruct",
        prompt=prompt,
        temperature=0.2,
        max_tokens=150, 
        top_p=1,
        frequency_penalty=0.2,
        presence_penalty=0.6,
        stop=["Client:", "Trainer:"]
    )
    return response.choices[0].text.strip()

# Chatbot logic
def chatbot_response(user_input):
    if user_input.lower() in ["hi", "hello", "hai"]:
        return "Hey there! Whats up?"
    elif user_input.lower() == "exit":
        return "Goodbye!"
    else:
        return openai_create("You: " + user_input + "\nAI Chatbot:")