from flask import Flask, request, jsonify
import google.generativeai as genai
import logging

app = Flask(__name__)

# Enable logging
logging.basicConfig(level=logging.DEBUG)

# Configure Gemini API key
GOOGLE_API_KEY = "AIzaSyD61FB27DLnMI0XCrmTeUN4S8g2rRfK97U"
genai.configure(api_key=GOOGLE_API_KEY)

# Load Gemini model
model = genai.GenerativeModel("gemini-1.5-flash")

@app.route("/gemini/alternatives", methods=["POST"])
def get_alternatives():
    data = request.get_json(silent=True)
    app.logger.info("Incoming JSON: %s", data)

    if not data:
        return jsonify({"error": "Missing JSON body or invalid format"}), 400

    product_name = data.get("product_name", "").strip()
    brand = data.get("brand", "").strip()

    if not product_name and not brand:
        return jsonify({"error": "Please provide at least 'product_name' or 'brand'"}), 400

    # 🧠 Build prompt dynamically
    if product_name and brand:
        prompt = (
            f"Suggest 5 eco-friendly alternatives to the product '{product_name}' from brand '{brand}', "
            f"including an estimated carbon footprint in g CO2e per 100g for each. "
            f"Return the results as a plain list like:\n- Product Name (X g CO2e)"
        )
    elif product_name:
        prompt = (
            f"Suggest 5 eco-friendly alternatives to the product '{product_name}', "
            f"including an estimated carbon footprint in g CO2e per 100g for each. "
            f"Return the results as a plain list like:\n- Product Name (X g CO2e)"
        )
    else:
        prompt = (
            f"Suggest 5 eco-friendly products from the brand '{brand}', "
            f"including their estimated carbon footprints in g CO2e per 100g. "
            f"Return the results as a plain list like:\n- Product Name (X g CO2e)"
        )

    try:
        response = model.generate_content(prompt)
        raw_lines = response.text.strip().split("\n")
        alternatives = []

        for line in raw_lines:
            if line.strip():
                clean_line = line.strip("-• ").strip()
                if "(" in clean_line and ")" in clean_line:
                    name_part = clean_line.split("(")[0].strip()
                    carbon_part = clean_line.split("(")[1].replace(")", "").strip()
                else:
                    name_part = clean_line
                    carbon_part = "Unknown"

                alternatives.append({
                    "name": name_part,
                    "carbon_footprint": carbon_part
                })

        return jsonify({"alternatives": alternatives})
    except Exception as e:
        app.logger.error("Gemini API error: %s", str(e))
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5001, debug=True)
