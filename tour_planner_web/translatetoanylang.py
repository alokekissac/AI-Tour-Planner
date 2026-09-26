# from translate import Translator
# translator= Translator(to_lang="Hindi")
# translation = translator.translate("Good Morning!")
# print (translation)
import sys
import io

from deep_translator import GoogleTranslator

# Set the encoding to UTF-8
if __name__ == '__main__':
    sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

# # Now you can print Malayalam text without errors
# print(malayalam_text)

try:
    from googletrans import Translator  # optional
except ImportError:
    Translator = None

def translate_text(text, dest_language):
    translator = Translator()
    translated_text = translator.translate(text, dest=dest_language)
    return translated_text.text


def transs(text, lan):
    try:
        translated = GoogleTranslator(source='auto', target=lan).translate(text)
        print("Translated:", translated)
        return translated
    except Exception as e:
        print("Error:", e)
        return None

# Example
# out = transs("how are you", "ml")
# print(out)